package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations;

import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.ActionTickResult;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.DropletStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.ElectrodeGrid;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.ElectrodeGridFactory;
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
    public void beforeExecution() {
        if(resultDroplet.getStatus() != DropletStatus.NOT_CREATED) {
            throw new IllegalStateException("Result droplet must be in NOT_CREATED state.");
        }

        moveAction1.beforeExecution();
        moveAction2.beforeExecution();

        resultDroplet.setVolume(droplet1.getVolume() + droplet2.getVolume());
        inputAction = new InputAction(null, posX, posY, resultDroplet.getVolume());
        inputAction.setDroplet(resultDroplet);

        setStatus(ActionStatus.IN_PROGRESS);
    }

    @Override
    public ActionTickResult executeTick(ProgramConfiguration programConfiguration) {

        // TODO: Ensure that 1 of the droplets safe-area is touching the target position, before allowing the merge.
        //  Currently, the droplets will be able to merge far away from the target position, if they cant pathfind to the target position, with eachother as obstacles.
        // TODO: When above is fixed. What to do if they are blocking each-other, on the way to the target position?
        //  They should still not be able to merge before they are on the target position. (One of them should move away?)

        ActionTickResult actionTickResult = new ActionTickResult();

        if(moveAction1.getStatus() == ActionStatus.COMPLETED && moveAction2.getStatus() == ActionStatus.COMPLETED) {
            // Consume both droplets.
            droplet1.setStatus(DropletStatus.CONSUMED);
            droplet2.setStatus(DropletStatus.CONSUMED);

            // At this point, the result droplet can be safely inserted.
            inputAction.beforeExecution();
            actionTickResult = inputAction.executeTick(programConfiguration);
            inputAction.afterExecution();

            // Set the status of this action to completed.
            setStatus(ActionStatus.COMPLETED);
            return actionTickResult;
        }

        actionTickResult.addTickResult(attemptToMoveDroplets(programConfiguration));

        // Check if one or both droplets are able to move to towards the target position.
        if(!actionTickResult.getTickCommandsAsStrings().isEmpty()) {
            return actionTickResult;
        }

        // At this point, merging of the droplets is allowed, but only if the result droplet can be safely inserted.
        if(!resultDropletCanBeSafelyInserted(programConfiguration)) {
            return actionTickResult; // The result droplet cannot be safely inserted. Do not merge (yet).
        }

        if(!resultDropletInserted) {
            resultDroplet.setStatus(DropletStatus.UNAVAILABLE); // The result droplet is not yet physically available, till merge is completed.
            resultDroplet.setPositionX(posX);
            resultDroplet.setPositionY(posY);

            // Allow droplets to merge internally between each-other.
            moveAction1.addExemptObstacleDroplet(droplet2);
            moveAction1.addExemptObstacleDroplet(resultDroplet);
            moveAction2.addExemptObstacleDroplet(droplet1);
            moveAction2.addExemptObstacleDroplet(resultDroplet);

            resultDropletInserted = true;
            actionTickResult.setTickShouldBeExecuted(true);
        }

        return actionTickResult;
    }

    @Override
    public void afterExecution() {
        droplet1.setStatus(DropletStatus.CONSUMED);
        droplet2.setStatus(DropletStatus.CONSUMED);
        resultDroplet.setStatus(DropletStatus.AVAILABLE);
    }

    private boolean dropletIsAtTargetPosition(Droplet droplet) {
        return droplet.getPositionX() == posX && droplet.getPositionY() == posY;
    }




    public void setDroplet1(Droplet droplet1) {
        this.droplet1 = droplet1;
        moveAction1.setDroplet(droplet1);
    }

    public void setDroplet2(Droplet droplet2) {
        this.droplet2 = droplet2;
        moveAction2.setDroplet(droplet2);
    }

    private boolean resultDropletCanBeSafelyInserted(ProgramConfiguration programConfiguration) {
        List<Droplet> obstacleDroplets = new ArrayList<>(
                programConfiguration.getDropletsOnDmfPlatform().stream()
                        .filter(d -> !d.equals(resultDroplet) && !d.equals(droplet1) && !d.equals(droplet2))
                        .toList());
        ElectrodeGrid electrodeGrid = programConfiguration.getElectrodeGrid();
        ElectrodeGrid availableElectrodeGrid = ElectrodeGridFactory.getAvailableElectrodeGrid(electrodeGrid, resultDroplet, obstacleDroplets);
        return availableElectrodeGrid.getElectrode(posX, posY) != null;
    }

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
}
