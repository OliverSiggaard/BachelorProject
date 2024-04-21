package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations;

import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.ActionTickResult;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.DropletStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class StoreAction extends ActionBase {

    private final int posX;
    private final int posY;
    private int ticksLeft;

    @Setter
    private ActionBase nextAction = null;
    @Setter
    private Droplet droplet = null;
    public StoreAction(
            String id,
            int posX,
            int posY,
            int time
    ) {
        super(id);
        this.posX = posX;
        this.posY = posY;
        this.ticksLeft = time;
    }

    @Override
    public Set<Droplet> dropletsRequiredForExecution() {
        return Set.of(droplet);
    }

    @Override
    public void beforeExecution() {
        setStatus(ActionStatus.IN_PROGRESS);
        droplet.setStatus(DropletStatus.UNAVAILABLE);
    }

    @Override
    public ActionTickResult executeTick(ProgramConfiguration programConfiguration) {
        // A OutputAction should always be preceded by a MoveAction moving the droplet to the output position.
        if (droplet.getPositionX() != posX || droplet.getPositionY() != posY) {
            throw new IllegalStateException("Error when storing droplet. Droplet is not at the store position.");
        }

        if(--ticksLeft <= 0){
            setStatus(ActionStatus.COMPLETED);
        }

        ActionTickResult actionTickResult = new ActionTickResult();
        actionTickResult.setTickShouldBeExecuted(true);
        return actionTickResult;
    }

    @Override
    public void afterExecution() {
        droplet.setStatus(DropletStatus.AVAILABLE);
    }
}
