package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations;

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

        DropletMove move = getDropletMove(programConfiguration);
        if(!Droplet.dropletMoveChangesDropletPosition(move)) return new ActionTickResult(); // Droplet is not moving.

        droplet.setDropletMove(move);

        ElectrodeGrid electrodeGrid = programConfiguration.getElectrodeGrid();
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

    private DropletMove getDropletMove(ProgramConfiguration programConfiguration) {
        List<Droplet> obstacleDroplets = new ArrayList<>(
                programConfiguration.getDropletsOnDmfPlatform().stream()
                     .filter(d -> !d.equals(droplet))
                     .toList());
        obstacleDroplets.removeAll(ExemptObstacleDroplets);

        ElectrodeGrid electrodeGrid = programConfiguration.getElectrodeGrid();
        ElectrodeGrid availableElectrodeGrid = ElectrodeGridFactory.getAvailableElectrodeGrid(electrodeGrid, droplet, obstacleDroplets);

        IPathFinder pathFinder = programConfiguration.getPathFinder();
        return pathFinder.getMove(droplet, availableElectrodeGrid, posX, posY);
    }

    private boolean dropletIsAtTargetPosition() {
        return droplet.getPositionX() == posX && droplet.getPositionY() == posY;
    }

    public void addExemptObstacleDroplet(Droplet ExemptObstacleDroplet) {
        ExemptObstacleDroplets.add(ExemptObstacleDroplet);
    }


}
