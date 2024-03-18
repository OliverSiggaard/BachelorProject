package com.digitalmicrofluidicbiochips.bachelorProject.model.task.implementations;

import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.DropletStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.task.TaskBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.task.TaskStatus;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class MoveTask extends TaskBase {

    private final Droplet droplet;
    private final int posX;
    private final int posY;


    public MoveTask(Droplet droplet, int x, int y) {
        this.droplet = droplet;
        this.posX = x;
        this.posY = y;
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
        // TODO: ExecuteTick should probably take the current state of the board as input.

        if(droplet.getPositionX() == posX && droplet.getPositionY() == posY) {
            setStatus(TaskStatus.COMPLETED);
        }
    }

    @Override
    public void afterExecution() {
        droplet.setStatus(DropletStatus.AVAILABLE);
    }
}
