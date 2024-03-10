package com.digitalmicrofluidicbiochips.bachelorProject.model.task;

import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;

import java.util.Set;

public abstract class TaskBase {

    private TaskStatus status = TaskStatus.NOT_STARTED;

    public abstract Set<Droplet> affectedDroplets();

    public abstract void beforeExecution();

    public abstract void executeTick();

    public abstract void afterExecution();

    protected void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskStatus getStatus() {
        return status;
    }
}
