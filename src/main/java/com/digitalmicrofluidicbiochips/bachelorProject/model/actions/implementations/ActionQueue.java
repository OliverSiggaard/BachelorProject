package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations;

import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.ActionTickResult;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
    public Set<Droplet> dropletsRequiredForExecution() {
        return actions.stream()
                .flatMap(action -> action.dropletsRequiredForExecution().stream())
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Droplet> dropletsProducedByExecution() {
        return Collections.emptySet();
    }

    @Override
    public void beforeExecution(ProgramConfiguration programConfiguration) {
        setStatus(ActionStatus.IN_PROGRESS);
        // Droplets status is handled in the individual actions in the queue.
    }

    @Override
    public ActionTickResult executeTick(ProgramConfiguration programConfiguration) {
        ActionTickResult actionTickResult = new ActionTickResult();
        if(currentActionIndex >= actions.size()) return new ActionTickResult();

        ActionBase action = actions.get(currentActionIndex);
        ActionStatus actionStatus = action.getStatus();
        // Start or tick already running action
        if(actionStatus == ActionStatus.NOT_STARTED) {
            action.beforeExecution(programConfiguration);
            actionTickResult = action.executeTick(programConfiguration);
        } else if (actionStatus == ActionStatus.IN_PROGRESS) {
            actionTickResult = action.executeTick(programConfiguration);
        }

        // Check if it is completed
        if(action.getStatus() == ActionStatus.COMPLETED) {
            action.afterExecution(programConfiguration);
            currentActionIndex++;
            if (currentActionIndex == actions.size()) {
                setStatus(ActionStatus.COMPLETED);
            };

            return actionTickResult;
        }

        // Check if it failed
        if(action.getStatus() == ActionStatus.FAILED) {
            setStatus(ActionStatus.FAILED);
            return new ActionTickResult();
        }

        return actionTickResult;
    }

    @Override
    public void afterExecution(ProgramConfiguration programConfiguration) {
        // Droplets status is handled in the individual actions in the queue.
    }
}
