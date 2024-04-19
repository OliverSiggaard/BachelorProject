package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations;

import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.ActionTickResult;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.DropletStatus;
import lombok.Getter;
import lombok.Setter;

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

        // TODO: Ensure that there is space for the input droplet before running inputAction.
        //  (Input is larger than the 2 droplets, so it could in theory overlap with other things that the 2 origin droplets do not)
        // TODO: Ensure that 1 of the droplets safe-area is touching the target position, before allowing the merge.
        //  Currently, the droplets will be able to merge far away from the target position, if they cant pathfind to the target position, with eachother as obstacles.
        // TODO: When above is fixed. What to do if they are blocking each-other, on the way to the target position?
        //  They should still not be able to merge before they are on the target position. (One of them should move away?)

        ActionTickResult actionTickResult = new ActionTickResult();
        if(moveAction1.getStatus() == ActionStatus.COMPLETED && moveAction2.getStatus() == ActionStatus.COMPLETED) {
            // Consume both droplets.
            droplet1.setStatus(DropletStatus.CONSUMED);
            droplet2.setStatus(DropletStatus.CONSUMED);

            // Create the result droplet.
            inputAction.beforeExecution();
            actionTickResult = inputAction.executeTick(programConfiguration);
            inputAction.afterExecution();

            // Set the status of this action to completed.
            setStatus(ActionStatus.COMPLETED);
            return actionTickResult;
        }


        if(moveAction1.getStatus() == ActionStatus.IN_PROGRESS) {
            actionTickResult.addTickResult(moveAction1.executeTick(programConfiguration));
        }
        if(moveAction2.getStatus() == ActionStatus.IN_PROGRESS) {
            actionTickResult.addTickResult(moveAction2.executeTick(programConfiguration));
        }

        // Check if one or both droplets are able to move to towards the target position.
        if(!actionTickResult.getTickCommandsAsStrings().isEmpty()) {
            return actionTickResult;
        }

        // If this point is reached, neither of the droplets are able to move towards the target position.
        // This means that there are either no paths to the target position, or that they are blocked by each-other.
        // In this case, all droplets to merge, by not seeing each-other as obstacles.
        moveAction1.addExemptObstacleDroplet(droplet2);
        moveAction2.addExemptObstacleDroplet(droplet1);

        if(moveAction1.getStatus() == ActionStatus.IN_PROGRESS) {
            actionTickResult.addTickResult(moveAction1.executeTick(programConfiguration));
        }
        if(moveAction2.getStatus() == ActionStatus.IN_PROGRESS) {
            actionTickResult.addTickResult(moveAction2.executeTick(programConfiguration));
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
}
