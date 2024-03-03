package com.digitalmicrofluidicbiochips.bachelorProject.model.actions;

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
        return new HashSet<>(Set.of(resultDroplet, droplet1, droplet2));
    }

    @Override
    public void beforeExecution() {
        droplet1.setStatus(DropletStatus.UNAVAILABLE);
        droplet2.setStatus(DropletStatus.UNAVAILABLE);
        resultDroplet.setStatus(DropletStatus.UNAVAILABLE);
    }

    @Override
    public void execute() {

    }

    @Override
    public void afterExecution() {
        droplet1.setStatus(DropletStatus.CONSUMED);
        droplet2.setStatus(DropletStatus.CONSUMED);
        resultDroplet.setStatus(DropletStatus.AVAILABLE);
    }
}
