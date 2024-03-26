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
public class MergeAction extends ActionBase {

    private final int posX;
    private final int posY;

    @Setter
    private ActionBase nextAction = null;

    @Setter
    private Droplet droplet1 = null;

    @Setter
    private Droplet droplet2 = null;
    @Setter
    private Droplet resultDroplet = null;

    public MergeAction(
            String id,
            int posX,
            int posY
    ) {
        super(id);
        this.posX = posX;
        this.posY = posY;
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
