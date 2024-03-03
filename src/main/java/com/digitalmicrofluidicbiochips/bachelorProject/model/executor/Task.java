package com.digitalmicrofluidicbiochips.bachelorProject.model.executor;

import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import lombok.Setter;

/**
 * A task encapsulates an action, allowing it to be executed by the executor.
 */
public class Task {

    private final ActionBase action;

    @Setter
    private TaskStatus status;

    public Task(ActionBase action) {
        this.action = action;
        this.status = TaskStatus.UNAVAILABLE;
    }





}
