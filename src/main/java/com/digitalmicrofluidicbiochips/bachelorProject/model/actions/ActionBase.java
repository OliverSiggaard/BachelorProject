package com.digitalmicrofluidicbiochips.bachelorProject.model.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import lombok.Getter;

import java.util.Set;

@Getter
public abstract class ActionBase {

    private final String id;

    public ActionBase(String id) {
        this.id = id;
    }

    /**
     * Returns the set of droplets that are affected by this action.
     * @return the set of droplets that are affected by this action.
     */
    public abstract Set<Droplet> affectedDroplets();

    /**
     * Executes before the action is executed.
     */
    public abstract void beforeExecution();

    /**
     * Executes the action.
     */
    public abstract void execute();

    /**
     * Executes after the action is executed.
     */
    public abstract void afterExecution();



}
