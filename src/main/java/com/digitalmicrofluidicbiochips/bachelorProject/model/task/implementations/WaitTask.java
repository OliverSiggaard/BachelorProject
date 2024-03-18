package com.digitalmicrofluidicbiochips.bachelorProject.model.task.implementations;

import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.task.TaskBase;

import java.util.Set;

/**
 * This class represents a wait task, which is a task that waits for a certain amount of time.
 * Time is represented in ticks, hence the amount of ticks to wait should be calculated by the compiler,
 * utilizing the time between each tick on the DMF platform multiplied by the amount of time to wait in seconds.
 */
public class WaitTask extends TaskBase {
    @Override
    public Set<Droplet> affectedDroplets() {
        return null;
    }

    @Override
    public void beforeExecution() {

    }

    @Override
    public void executeTick() {

    }

    @Override
    public void afterExecution() {

    }
}
