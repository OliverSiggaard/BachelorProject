package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations;

import com.digitalmicrofluidicbiochips.bachelorProject.executor.path_finding.DropletMove;
import com.digitalmicrofluidicbiochips.bachelorProject.executor.path_finding.IPathFinder;
import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionTickResult;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.List;

@Getter
public class MoveAction extends ActionBase {

    private final int posX;
    private final int posY;
    @Setter
    private ActionBase nextAction = null;
    @Setter
    private Droplet droplet = null;

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
    }

    @Override
    public Set<Droplet> affectedDroplets() {
        return new HashSet<>(Set.of(droplet));
    }

    @Override
    public void beforeExecution() {
        droplet.setStatus(DropletStatus.UNAVAILABLE);
        setStatus(ActionStatus.IN_PROGRESS);
    }

    @Override
    public ActionTickResult executeTick(ProgramConfiguration programConfiguration) {

        if(!tickQueue.isEmpty()) {
            ActionTickResult tickResult = tickQueue.poll();
            if(tickQueue.isEmpty()) {
                moveDropletPosition(droplet.getDropletMove());
                droplet.setDropletMove(DropletMove.NONE);
                if(dropletIsAtTargetPosition()) setStatus(ActionStatus.COMPLETED);
            }
            return tickResult;
        }

        DropletMove move = getDropletMove(programConfiguration);
        if(!dropletMoveChangesDropletPosition(move)) return new ActionTickResult(); // Droplet is not moving.

        droplet.setDropletMove(move);

        ElectrodeGrid electrodeGrid = programConfiguration.getElectrodeGrid();
        ActionTickResult thisTickResult = new ActionTickResult(getCommandsToActivateAdjacentElectrodes(electrodeGrid));
        ActionTickResult nextTickResult = new ActionTickResult(getCommandsToDeactivateAbandonedElectrodes(electrodeGrid));

        tickQueue.add(nextTickResult);
        return thisTickResult; // Electrodes have to be enabled
    }

    @Override
    public void afterExecution() {
        droplet.setStatus(DropletStatus.AVAILABLE);
    }

    private List<String> getCommandsToActivateAdjacentElectrodes(ElectrodeGrid electrodeGrid) {
        return droplet.getCoordinatesToEnableBeforeMove().stream()
                .map(p -> electrodeGrid.getElectrode(p.x,p.y).getEnableBioAssemblyCommand())
                .toList();
    }

    private List<String> getCommandsToDeactivateAbandonedElectrodes(ElectrodeGrid electrodeGrid) {
        return droplet.getCoordinatesToDisableAfterMove().stream()
                .map(p -> electrodeGrid.getElectrode(p.x,p.y).getDisableBioAssemblyCommand())
                .toList();
    }

    private DropletMove getDropletMove(ProgramConfiguration programConfiguration) {

        List<Droplet> obstacleDroplets = programConfiguration.getDroplets().stream()
                .filter(droplet -> droplet != this.droplet &&
                        (droplet.getStatus() == DropletStatus.AVAILABLE ||
                                droplet.getStatus() == DropletStatus.UNAVAILABLE))
                .toList();
        ElectrodeGrid electrodeGrid = programConfiguration.getElectrodeGrid();
        ElectrodeGrid availableElectrodeGrid = ElectrodeGridFactory.getAvailableElectrodeGrid(electrodeGrid, droplet, obstacleDroplets);

        IPathFinder pathFinder = programConfiguration.getPathFinder();
        return pathFinder.getMove(droplet, availableElectrodeGrid, posX, posY);
    }

    private void moveDropletPosition(DropletMove dropletMove) {
        switch (dropletMove) {
            case UP -> droplet.setPositionY(droplet.getPositionY() - 1);
            case DOWN -> droplet.setPositionY(droplet.getPositionY() + 1);
            case LEFT -> droplet.setPositionX(droplet.getPositionX() - 1);
            case RIGHT -> droplet.setPositionX(droplet.getPositionX() + 1);
        }
    }

    private boolean dropletMoveChangesDropletPosition(DropletMove dropletMove) {
        return dropletMove == DropletMove.UP ||
                dropletMove == DropletMove.LEFT ||
                dropletMove == DropletMove.DOWN ||
                dropletMove == DropletMove.RIGHT;
    }

    private boolean dropletIsAtTargetPosition() {
        return droplet.getPositionX() == posX && droplet.getPositionY() == posY;
    }


}
