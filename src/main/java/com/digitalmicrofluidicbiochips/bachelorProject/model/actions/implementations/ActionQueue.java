package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations;

import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ActionQueue extends ActionBase {


    private final List<ActionBase> actions;
    private int currentActionIndex = 0;

    public ActionQueue(String id, List<ActionBase> actions) {
        super(id);
        this.actions = actions;
    }

    @Override
    public Set<Droplet> affectedDroplets() {
        return actions.stream()
                .flatMap(action -> action.affectedDroplets().stream())
                .collect(Collectors.toSet());
    }

    @Override
    public void beforeExecution() {
        setStatus(ActionStatus.IN_PROGRESS);
    }

    @Override
    public void executeTick(ProgramConfiguration programConfiguration) {
        if(currentActionIndex >= actions.size()) return;

        ActionBase currentTask = actions.get(currentActionIndex);
        if(currentTask.getStatus() == ActionStatus.COMPLETED) {
            throw new IllegalStateException("Current task can not be completed. Has to be Not Started or In Progress.");
        }

        switch (currentTask.getStatus()) {
            case NOT_STARTED -> {
                currentTask.beforeExecution();
                currentTask.executeTick(programConfiguration);
            }
            case IN_PROGRESS -> currentTask.executeTick(programConfiguration);
            case FAILED -> {
                setStatus(ActionStatus.FAILED);
                return;
            }
        }

        //Was the task completed in this tick?
        if(currentTask.getStatus() == ActionStatus.COMPLETED) {
            currentTask.afterExecution();
            currentActionIndex++;
            if (currentActionIndex < actions.size()) return;

            //If the last task was completed, the task list is also completed.
            setStatus(ActionStatus.COMPLETED);
        }
    }

    @Override
    public void afterExecution() {

    }
}
