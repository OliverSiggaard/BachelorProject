package com.digitalmicrofluidicbiochips.bachelorProject.compiler;

import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations.InputAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;

import java.util.*;

/**
 * The Compiler class is responsible for compiling high-level actions into Tasks, as well as
 * ensuring proper scheduling of tasks,
 * Tasks can contain 1 or more operations, which, when executed, will
 */
public class Compiler {

    public static Schedule compile(List<ActionBase> actions) {

        Map<Droplet, Queue<ActionBase>> dropletActions = new HashMap<>();

        actions.forEach(action -> {
            Set<Droplet> affectedDroplets = action.dropletsRequiredForExecution();
            if(affectedDroplets == null) return;
            affectedDroplets.forEach(droplet -> {
                if (!dropletActions.containsKey(droplet)) {
                    dropletActions.put(droplet, new LinkedList<>());
                }
                dropletActions.get(droplet).add(action);
            });

            if(action instanceof InputAction) {
                InputAction inputAction = (InputAction) action;
                Droplet droplet = inputAction.getDroplet();
                if (!dropletActions.containsKey(droplet)) {
                    dropletActions.put(droplet, new LinkedList<>());
                }
                dropletActions.get(droplet).add(action);
            }

        });

        return new Schedule(dropletActions);
    }
}
