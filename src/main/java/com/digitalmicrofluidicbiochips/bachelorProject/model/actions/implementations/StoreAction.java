package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations;

import com.digitalmicrofluidicbiochips.bachelorProject.errors.DmfExceptionMessage;
import com.digitalmicrofluidicbiochips.bachelorProject.errors.DmfInvalidInputException;
import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.ActionTickResult;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.DropletStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.Set;

@Getter
public class StoreAction extends ActionBase {

    private final int posX;
    private final int posY;
    private final int ticksToWait;
    private int ticksLeft;

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
        this.ticksToWait = time;
        this.ticksLeft = ticksToWait;
    }

    @Override
    public Set<Droplet> dropletsRequiredForExecution() {
        return Set.of(droplet);
    }

    @Override
    public Set<Droplet> dropletsProducedByExecution() {
        return Collections.emptySet();
    }

    @Override
    public void beforeExecution(ProgramConfiguration programConfiguration) {
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
        actionTickResult.setSomethingHappenedInTick(true);
        return actionTickResult;
    }

    @Override
    public void afterExecution(ProgramConfiguration programConfiguration) {
        droplet.setStatus(DropletStatus.AVAILABLE);
    }

    @Override
    public boolean verifyProperties(ProgramConfiguration programConfiguration) {
        if (droplet == null) {
            throw new DmfInvalidInputException(DmfExceptionMessage.DROPLET_NOT_DEFINED_ON_ACTION.getMessage());
        }
        if(ticksToWait <= 0){
            throw new DmfInvalidInputException(DmfExceptionMessage.STORE_ACTION_INVALID_TIME.getMessage());
        }
        if (!programConfiguration.getElectrodeGrid().isWithinBounds(posX, posY)) {
            int maxX = programConfiguration.getElectrodeGrid().getXSize();
            int maxY = programConfiguration.getElectrodeGrid().getYSize();
            throw new DmfInvalidInputException(DmfExceptionMessage.POSITION_OUT_OF_BOUND.getMessage(posX, posY, maxX, maxY));
        }

        return true;
    }
}
