package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations;

import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.ActionTickResult;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
public class MergeAction extends ActionBase {

    private final int posX;
    private final int posY;

    @Setter
    private ActionBase nextAction = null;

    private Droplet droplet1 = null;

    private Droplet droplet2 = null;
    @Setter
    private Droplet resultDroplet = null;

    private final MoveAction moveAction1;
    private final MoveAction moveAction2;
    private InputAction inputAction; // Not final. Volume of origin droplets is only known when action is reached.

    private boolean resultDropletInserted;

    public MergeAction(
            String id,
            int posX,
            int posY
    ) {
        super(id);
        this.posX = posX;
        this.posY = posY;

        this.moveAction1 = new MoveAction(id, posX, posY);
        this.moveAction2 = new MoveAction(id, posX, posY);
        this.resultDropletInserted = false;
    }

    @Override
    public Set<Droplet> dropletsRequiredForExecution() {
        return Set.of(droplet1, droplet2);
    }

    @Override
    public void beforeExecution(ProgramConfiguration programConfiguration) {
        if(resultDroplet.getStatus() != DropletStatus.NOT_CREATED) {
            throw new IllegalStateException("Result droplet must be in NOT_CREATED state.");
        }

        moveAction1.beforeExecution(programConfiguration);
        moveAction2.beforeExecution(programConfiguration);

        resultDroplet.setVolume(droplet1.getVolume() + droplet2.getVolume());
        inputAction = new InputAction(null, posX, posY, resultDroplet.getVolume());
        inputAction.setDroplet(resultDroplet);

        setStatus(ActionStatus.IN_PROGRESS);
    }

    @Override
    public ActionTickResult executeTick(ProgramConfiguration programConfiguration) {
        ActionTickResult actionTickResult = new ActionTickResult();

        if(moveAction1.getStatus() == ActionStatus.COMPLETED && moveAction2.getStatus() == ActionStatus.COMPLETED) {
            // Consume both droplets.
            droplet1.setStatus(DropletStatus.CONSUMED);
            droplet2.setStatus(DropletStatus.CONSUMED);

            // At this point, the result droplet can be safely inserted.
            inputAction.beforeExecution(programConfiguration);
            actionTickResult = inputAction.executeTick(programConfiguration);
            inputAction.afterExecution(programConfiguration);

            // Set the status of this action as completed.
            setStatus(ActionStatus.COMPLETED);
            return actionTickResult;
        }

        if(!resultDropletInserted && isDropletsAllowedToMerge(programConfiguration)) {
            resultDroplet.setStatus(DropletStatus.UNAVAILABLE); // The result droplet is not yet physically available, till merge is completed.
            resultDroplet.setPositionX(posX);
            resultDroplet.setPositionY(posY);

            // Allow droplets to merge internally between each-other.
            moveAction1.addExemptObstacleDroplet(droplet2);
            moveAction1.addExemptObstacleDroplet(resultDroplet);
            moveAction2.addExemptObstacleDroplet(droplet1);
            moveAction2.addExemptObstacleDroplet(resultDroplet);

            resultDropletInserted = true;
        }

        actionTickResult.addTickResult(attemptToMoveDroplets(programConfiguration));

        return actionTickResult;
    }

    @Override
    public void afterExecution(ProgramConfiguration programConfiguration) {
        droplet1.setStatus(DropletStatus.CONSUMED);
        droplet2.setStatus(DropletStatus.CONSUMED);
        resultDroplet.setStatus(DropletStatus.AVAILABLE);
    }

    /**
     * The droplets are allowed to merge if:
     * 1. The result droplet can be inserted at the target position, without touching any other obstacle droplets.
     * 2. The safe area of either droplet1 or droplet2 contains the target position.
     * @param programConfiguration Program configuration.
     * @return True if droplets are allowed to merge.
     */
    private boolean isDropletsAllowedToMerge(ProgramConfiguration programConfiguration) {
        List<Droplet> obstacleDroplets = new ArrayList<>(
                programConfiguration.getDropletsOnDmfPlatform().stream()
                        .filter(d -> !Set.of(resultDroplet, droplet1, droplet2).contains(d))
                        .toList());
        ElectrodeGrid electrodeGrid = programConfiguration.getElectrodeGrid();
        ElectrodeGrid resultAvailableGrid = ElectrodeGridFactory.getAvailableElectrodeGrid(electrodeGrid, resultDroplet, obstacleDroplets);
        boolean resultDropletCanBeInserted = resultAvailableGrid.getElectrode(posX, posY) != null;

        GridArea droplet1SafeArea = droplet1.getDropletSafeArea(programConfiguration.getElectrodeGrid(), droplet2);
        GridArea droplet2SafeArea = droplet2.getDropletSafeArea(programConfiguration.getElectrodeGrid(), droplet1);
        boolean eitherDropletSafeAreaContainsTargetPosition = droplet1SafeArea.contains(posX, posY) || droplet2SafeArea.contains(posX, posY);

        return resultDropletCanBeInserted && eitherDropletSafeAreaContainsTargetPosition;
    }

    /**
     * If either of the two move actions are in progress, attempt to move the droplets,
     * by executing a tick on the move actions, and return the combined tick results from both.
     * Droplet1 will be prioritized over droplet2, in case the movement of droplet 1 blocks the movement of droplet 2.
     * @param programConfiguration Program configuration.
     * @return Action tick result.
     */
    private ActionTickResult attemptToMoveDroplets(ProgramConfiguration programConfiguration) {
        ActionTickResult actionTickResult = new ActionTickResult();
        if(moveAction1.getStatus() == ActionStatus.IN_PROGRESS) {
            actionTickResult.addTickResult(moveAction1.executeTick(programConfiguration));
        }
        if(moveAction2.getStatus() == ActionStatus.IN_PROGRESS) {
            actionTickResult.addTickResult(moveAction2.executeTick(programConfiguration));
        }
        return actionTickResult;
    }

    /**
     * Set droplet1 that will be merged.
     * This method is used in the object mapper, when mapping from JSON to Java objects.
     * @param droplet Droplet to be merged.
     */
    public void setDroplet1(Droplet droplet) {
        this.droplet1 = droplet;
        moveAction1.setDroplet(droplet);
    }

    /**
     * Set droplet2 that will be merged.
     * This method is used in the object mapper, when mapping from JSON to Java objects.
     * @param droplet (other) Droplet to be merged.
     */
    public void setDroplet2(Droplet droplet) {
        this.droplet2 = droplet;
        moveAction2.setDroplet(droplet);
    }
}
