package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations;

import com.digitalmicrofluidicbiochips.bachelorProject.errors.DmfExceptionMessage;
import com.digitalmicrofluidicbiochips.bachelorProject.errors.DmfInvalidInputException;
import com.digitalmicrofluidicbiochips.bachelorProject.errors.ExceptionHandler;
import com.digitalmicrofluidicbiochips.bachelorProject.executor.path_finding.AStar;
import com.digitalmicrofluidicbiochips.bachelorProject.executor.path_finding.DropletMove;
import com.digitalmicrofluidicbiochips.bachelorProject.executor.path_finding.IPathFinder;
import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.ActionTickResult;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.ClearElectrodeCommand;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.IDmfCommand;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.SetElectrodeCommand;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.*;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

public class MoveAction extends ActionBase {
    @Getter
    private final int posX;
    @Getter
    private final int posY;
    @Getter @Setter
    private ActionBase nextAction = null;
    @Getter @Setter
    private Droplet droplet = null;

    private final Set<Droplet> ExemptObstacleDroplets;
    private final Queue<ActionTickResult> tickQueue;

    public MoveAction(
            String id,
            int posX,
            int posY
    ) {
        super(id);
        this.posX = posX;
        this.posY = posY;

        this.tickQueue = new LinkedList<>();
        ExemptObstacleDroplets = new HashSet<>();
    }

    @Override
    public Set<Droplet> dropletsRequiredForExecution() {
        return Set.of(droplet);
    }

    @Override
    public void beforeExecution(ProgramConfiguration programConfiguration) {
        droplet.setStatus(DropletStatus.UNAVAILABLE);
        setStatus(ActionStatus.IN_PROGRESS);
    }

    @Override
    public Set<Droplet> dropletsProducedByExecution() {
        return Collections.emptySet();
    }

    @Override
    public ActionTickResult executeTick(ProgramConfiguration programConfiguration) {

        // If the droplet is already at the target position, then it is completed.
        if(droplet.getPositionX() == posX && droplet.getPositionY() == posY) {
            setStatus(ActionStatus.COMPLETED);
            return new ActionTickResult();
        }

        if(!tickQueue.isEmpty()) {
            ActionTickResult tickResult = tickQueue.poll();
            if(tickQueue.isEmpty()) {
                droplet.moveDropletInDirection(droplet.getDropletMove());
                droplet.setDropletMove(DropletMove.NONE);
                if(dropletIsAtTargetPosition()) setStatus(ActionStatus.COMPLETED);
            }
            return tickResult;
        }

        // get droplet move
        List<Droplet> obstacleDroplets = getObstacleDroplets(programConfiguration);
        ElectrodeGrid electrodeGrid = programConfiguration.getElectrodeGrid();
        ElectrodeGrid availableElectrodeGrid = ElectrodeGridFactory.getAvailableElectrodeGrid(electrodeGrid, droplet, obstacleDroplets);
        DropletMove move = getDropletMove(programConfiguration, availableElectrodeGrid);

        // If the droplet is blocked, attempt
        if(move == DropletMove.BLOCKED && isAttemptToResolveDeadlock()) {
            move = getYieldDropletMove(programConfiguration, availableElectrodeGrid);
        }

        // If the droplet is still blocked, then the droplet can't move.
        if(!Droplet.dropletMoveChangesDropletPosition(move)) {
            return new ActionTickResult(); // Droplet is not moving.
        }

        droplet.setDropletMove(move);

        ActionTickResult thisTickResult = new ActionTickResult(getCommandsToActivateAdjacentElectrodes(electrodeGrid));
        ActionTickResult nextTickResult = new ActionTickResult(getCommandsToDeactivateAbandonedElectrodes(electrodeGrid));

        tickQueue.add(nextTickResult);
        return thisTickResult; // Electrodes have to be enabled
    }

    @Override
    public void afterExecution(ProgramConfiguration programConfiguration) {
        droplet.setStatus(DropletStatus.AVAILABLE);
    }

    private List<IDmfCommand> getCommandsToActivateAdjacentElectrodes(ElectrodeGrid electrodeGrid) {
        List<Electrode> electrodesToEnable =  droplet.getElectrodesToEnableDuringDropletMove(electrodeGrid);
        return electrodesToEnable.stream()
                .map(e -> (IDmfCommand) new SetElectrodeCommand(e))
                .toList();
    }

    private List<IDmfCommand> getCommandsToDeactivateAbandonedElectrodes(ElectrodeGrid electrodeGrid) {
        List<Electrode> electrodesToEnable =  droplet.getElectrodesToDisableDuringDropletMove(electrodeGrid);
        return electrodesToEnable.stream()
                .map(e -> (IDmfCommand) new ClearElectrodeCommand(e))
                .toList();
    }

    private DropletMove getDropletMove(ProgramConfiguration programConfiguration, ElectrodeGrid availableElectrodeGrid) {
        IPathFinder pathFinder = programConfiguration.getPathFinder();
        return pathFinder.getMove(droplet, availableElectrodeGrid, posX, posY);
    }


    /**
     * In case of a deadlock, where all droplets are stuck, this method can be used to attempt to move
     * the droplet in a random direction. There is no guarantee that a solution will be found using this method,
     * but it can be used as a last resort.
     * @return The move that the droplet should make.
     */
    private DropletMove getYieldDropletMove(ProgramConfiguration programConfiguration, ElectrodeGrid availableElectrodeGrid) {
        IPathFinder pathFinder = programConfiguration.getPathFinder();
        List<Point> directions = new ArrayList<>(List.of(
                new Point(1, 0),
                new Point(0, 1),
                new Point(-1, 0),
                new Point(0, -1)));
        Collections.shuffle(directions);

        // Try to move the droplet in a random direction. First direction that is valid is used.
        while (!directions.isEmpty()) {
            Point direction = directions.remove(0);
            int x = droplet.getPositionX() + direction.x;
            int y = droplet.getPositionY() + direction.y;

            DropletMove move = pathFinder.getMove(droplet, availableElectrodeGrid, x, y);
            if(Droplet.dropletMoveChangesDropletPosition(move)) {
                return move;
            }
        }

        // If no direction is valid, then the droplet is (still) blocked.
        return DropletMove.BLOCKED;
    }

    private boolean dropletIsAtTargetPosition() {
        return droplet.getPositionX() == posX && droplet.getPositionY() == posY;
    }

    public void addExemptObstacleDroplet(Droplet ExemptObstacleDroplet) {
        ExemptObstacleDroplets.add(ExemptObstacleDroplet);
    }

    private List<Droplet> getObstacleDroplets(ProgramConfiguration programConfiguration) {
        List<Droplet> droplets = new ArrayList<>(programConfiguration.getDropletsOnDmfPlatform().stream()
                .filter(d -> !d.equals(droplet))
                .toList());
        droplets.removeAll(ExemptObstacleDroplets);
        return droplets;
    }

    @Override
    public boolean verifyProperties(ProgramConfiguration programConfiguration) {
        if (droplet == null) {
            throw new DmfInvalidInputException(DmfExceptionMessage.DROPLET_NOT_DEFINED_ON_ACTION.getMessage());
        }
        if (!programConfiguration.getElectrodeGrid().isWithinBounds(posX, posY)) {
            int maxX = programConfiguration.getElectrodeGrid().getXSize();
            int maxY = programConfiguration.getElectrodeGrid().getYSize();
            throw new DmfInvalidInputException(DmfExceptionMessage.POSITION_OUT_OF_BOUND.getMessage(posX, posY, maxX, maxY));
        }

        return true;
    }


}
