package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations;

import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionStatus;
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
    private final int volume;

    @Setter
    private ActionBase nextAction = null;

    @Setter
    private Droplet droplet = null;

    public InputAction(
            String id,
            int posX,
            int posY,
            int volume
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
    public void executeTick(ProgramConfiguration programConfiguration) {
        //TODO: Figure out how to write this to file? BioAssembly Code? Is it done in 1 tick? Figure out if actuator exists?
    }

    @Override
    public void afterExecution() {
        droplet.setPositionX(posX);
        droplet.setPositionY(posY);
        droplet.setStatus(DropletStatus.AVAILABLE);
        setStatus(ActionStatus.COMPLETED);
    }
}
