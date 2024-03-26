package com.digitalmicrofluidicbiochips.bachelorProject.model.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import lombok.Getter;

import java.util.Set;

@Getter
public abstract class ActionBase {

    private final String id;

    public ActionBase(String id) {
        this.id = id;
    }

    private ActionStatus status = ActionStatus.NOT_STARTED;

    public abstract Set<Droplet> affectedDroplets();

    public abstract void beforeExecution();

    public abstract void executeTick(ProgramConfiguration programConfiguration);

    public abstract void afterExecution();

    protected void setStatus(ActionStatus status) {
        this.status = status;
    }

    public ActionStatus getStatus() {
        return status;
    }


}
