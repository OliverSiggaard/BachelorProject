package com.digitalmicrofluidicbiochips.bachelorProject.compiler;

import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;

import com.digitalmicrofluidicbiochips.bachelorProject.model.task.TaskBase;

import java.util.*;

/**
 * A schedule is a collection of tasks that are to be executed by the executor. The schedule is responsible for
 * ensuring that tasks are executed in the correct order, and that that only available tasks are executable.
 */
public class Schedule {

    private final Map<Droplet, Queue<ActionBase>> dropletActions;

    public Schedule(Map<Droplet, Queue<ActionBase>> dropletActions) {
        this.dropletActions = dropletActions;
    }

    public void updateSchedule() {
        for ( Queue<ActionBase> actions : dropletActions.values() ) {
            if(actions.isEmpty()) continue;
            if(actions.peek().getStatus() == ActionStatus.COMPLETED) {
                actions.poll();
            }
        }
    }

    public List<ActionBase> getActionsToBeTicked() {
        List<ActionBase> actionsToBeTicked = new ArrayList<>();
        for ( Queue<ActionBase> actions : dropletActions.values() ) {
            if(actions.isEmpty()) continue;
            actionsToBeTicked.add(actions.peek());
        }

        return actionsToBeTicked;
    }

















}
