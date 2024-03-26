package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations;

import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
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
    private final int time;

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
        this.time = time;
    }

    @Override
    public Set<Droplet> affectedDroplets() {
        return null;
    }

    @Override
    public void beforeExecution() {

    }

    @Override
    public void executeTick(ProgramConfiguration programConfiguration) {

    }

    @Override
    public void afterExecution() {

    }
}
