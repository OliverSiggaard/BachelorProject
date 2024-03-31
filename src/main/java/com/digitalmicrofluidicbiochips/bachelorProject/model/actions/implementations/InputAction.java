package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations;

import com.digitalmicrofluidicbiochips.bachelorProject.executor.path_finding.DropletMove;
import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionTickResult;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.DropletStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class InputAction extends ActionBase {


    private final int posX;
    private final int posY;
    private final double volume;

    @Setter
    private ActionBase nextAction = null;

    @Setter
    private Droplet droplet = null;

    public InputAction(
            String id,
            int posX,
            int posY,
            double volume
    ) {
        super(id);
        this.posX = posX;
        this.posY = posY;
        this.volume = volume;
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
        droplet.setPositionX(posX);
        droplet.setPositionY(posY);
        droplet.setDropletMove(DropletMove.NONE);
        droplet.setStatus(DropletStatus.AVAILABLE);

        setStatus(ActionStatus.COMPLETED);
        return new ActionTickResult();
    }

    @Override
    public void afterExecution() {

    }
}
