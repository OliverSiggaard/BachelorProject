package com.digitalmicrofluidicbiochips.bachelorProject.model.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.ActionTickResult;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
public abstract class ActionBase {

    private final String id;

    @Getter
    private ActionStatus status;

    @Getter @Setter
    private boolean attemptToResolveDeadlock;

    public ActionBase(String id) {
        this.id = id;
        status = ActionStatus.NOT_STARTED;
        this.attemptToResolveDeadlock = false;
    }

    public abstract Set<Droplet> dropletsRequiredForExecution();

    public abstract Set<Droplet> dropletsProducedByExecution();

    public abstract void beforeExecution(ProgramConfiguration programConfiguration);

    public abstract ActionTickResult executeTick(ProgramConfiguration programConfiguration);

    public abstract void afterExecution(ProgramConfiguration programConfiguration);

    protected void setStatus(ActionStatus status) {
        this.status = status;
    }

    public ActionTickResult executeTickAttemptToResolveDeadlock(ProgramConfiguration programConfiguration) {
        attemptToResolveDeadlock = true;
        ActionTickResult actionTickResult = executeTick(programConfiguration);
        attemptToResolveDeadlock = false;
        return actionTickResult;
    }

}
