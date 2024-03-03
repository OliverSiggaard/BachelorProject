package com.digitalmicrofluidicbiochips.bachelorProject.model.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.DropletStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class SplitAction extends ActionBase {

    private final double ratio;
    private final int posX1;
    private final int posY1;
    private final int posX2;
    private final int posY2;

    @Setter
    private ActionBase nextAction = null;
    @Setter
    private Droplet originDroplet = null;
    @Setter
    private Droplet resultDroplet1 = null;
    @Setter
    private Droplet resultDroplet2 = null;

    public SplitAction(
            String id,
            double ratio,
            int posX1,
            int posY1,
            int posX2,
            int posY2
    ) {
        super(id);
        this.ratio = ratio;
        this.posX1 = posX1;
        this.posY1 = posY1;
        this.posX2 = posX2;
        this.posY2 = posY2;
    }

    @Override
    public Set<Droplet> affectedDroplets() {
        return new HashSet<>(Set.of(originDroplet, resultDroplet1, resultDroplet2));
    }

    @Override
    public void beforeExecution() {
        originDroplet.setStatus(DropletStatus.UNAVAILABLE);
        resultDroplet1.setStatus(DropletStatus.UNAVAILABLE);
        resultDroplet2.setStatus(DropletStatus.UNAVAILABLE);
    }

    @Override
    public void execute() {

    }

    @Override
    public void afterExecution() {
        originDroplet.setStatus(DropletStatus.CONSUMED);
        resultDroplet1.setStatus(DropletStatus.AVAILABLE);
        resultDroplet2.setStatus(DropletStatus.AVAILABLE);
    }
}
