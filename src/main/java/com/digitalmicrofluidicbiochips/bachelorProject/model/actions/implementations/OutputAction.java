package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations;

import com.digitalmicrofluidicbiochips.bachelorProject.executor.path_finding.DropletMove;
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
public class OutputAction extends ActionBase {

    public int posX;
    public int posY;

    @Setter
    public ActionBase nextAction = null;
    @Setter
    private Droplet droplet = null;

    public OutputAction(
            String id,
            int posX,
            int posY
    ) {
        super(id);
        this.posX = posX;
        this.posY = posY;
    }

    @Override
    public Set<Droplet> dropletsRequiredForExecution() {
        return Set.of(droplet);
    }

    @Override
    public void beforeExecution() {
        droplet.setStatus(DropletStatus.UNAVAILABLE);
        setStatus(ActionStatus.IN_PROGRESS);
    }

    @Override
    public ActionTickResult executeTick(ProgramConfiguration programConfiguration) {
        // A OutputAction should always be preceded by a MoveAction moving the droplet to the output position.
        if (droplet.getPositionX() != posX || droplet.getPositionY() != posY) {
            throw new IllegalStateException("Error when outputting droplet. Droplet is not at the output position.");
        }

        droplet.setStatus(DropletStatus.CONSUMED);
        droplet.setDropletMove(DropletMove.NONE);

        setStatus(ActionStatus.COMPLETED);
        return new ActionTickResult();
    }

    @Override
    public void afterExecution() {
        // Nothing to update. Droplet is consumed.
    }
}
