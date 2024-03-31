package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations;

import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionTickResult;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ActionQueue extends ActionBase {

    @Getter
    private final List<ActionBase> actions;
    private int currentActionIndex = 0;

    @Getter @Setter
    private ActionBase nextAction = null;

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
    public ActionTickResult executeTick(ProgramConfiguration programConfiguration) {
        if(currentActionIndex >= actions.size()) return new ActionTickResult();

        ActionBase currentAction = actions.get(currentActionIndex);
        if(currentAction.getStatus() == ActionStatus.COMPLETED) {
            throw new IllegalStateException("Current action can not be completed. Has to be Not Started or In Progress.");
        }

        switch (currentAction.getStatus()) {
            case NOT_STARTED -> {
                currentAction.executeTick(programConfiguration);
            }
            case IN_PROGRESS -> currentAction.executeTick(programConfiguration);
            case FAILED -> {
                setStatus(ActionStatus.FAILED);
                return new ActionTickResult();
            }
        }

        //Was the action completed in this tick?
        if(currentAction.getStatus() == ActionStatus.COMPLETED) {
            currentAction.afterExecution();
            currentActionIndex++;
            if (currentActionIndex < actions.size()) return new ActionTickResult();;

            //If the last action was completed, the action list is also completed.
            setStatus(ActionStatus.COMPLETED);
        }
        return new ActionTickResult();
    }

    @Override
    public void afterExecution() {

    }
}
