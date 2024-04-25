package com.digitalmicrofluidicbiochips.bachelorProject.model.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.ActionTickResult;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import lombok.Getter;

import java.util.*;

@Getter
public abstract class ActionBase {

    private final String id;

    @Getter
    private ActionStatus status;

    public ActionBase(String id) {
        this.id = id;
        status = ActionStatus.NOT_STARTED;
    }

    public abstract Set<Droplet> dropletsRequiredForExecution();

    public abstract Set<Droplet> dropletsProducedByExecution();

    public abstract void beforeExecution(ProgramConfiguration programConfiguration);

    public abstract ActionTickResult executeTick(ProgramConfiguration programConfiguration);

    public abstract void afterExecution(ProgramConfiguration programConfiguration);

    protected void setStatus(ActionStatus status) {
        this.status = status;
    }

}
