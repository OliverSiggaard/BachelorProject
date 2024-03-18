package com.digitalmicrofluidicbiochips.bachelorProject.model.task.implementations;

import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.DropletStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.task.TaskBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.task.TaskStatus;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class InputTask extends TaskBase {

    private final Droplet droplet;
    private final int posX;
    private final int posY;
    private final int volume;

    public InputTask(Droplet droplet, int x, int y, int volume) {
        this.droplet = droplet;
        this.posX = x;
        this.posY = y;
        this.volume = volume;
    }

    @Override
    public Set<Droplet> affectedDroplets() {
        return new HashSet<>(Set.of(droplet));
    }

    @Override
    public void beforeExecution() {
        droplet.setStatus(DropletStatus.UNAVAILABLE);
        setStatus(TaskStatus.IN_PROGRESS);
    }

    @Override
    public void executeTick() {
        //TODO: Figure out how to write this to file? BioAssembly Code? Is it done in 1 tick? Figure out if actuator exists?
    }

    @Override
    public void afterExecution() {
        droplet.setPositionX(posX);
        droplet.setPositionY(posY);
        droplet.setStatus(DropletStatus.AVAILABLE);
        setStatus(TaskStatus.COMPLETED);
    }
}
