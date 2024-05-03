package com.digitalmicrofluidicbiochips.bachelorProject.compiler;

import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations.InputAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations.MoveAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations.SplitAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;

import java.util.*;

/**
 * The Compiler class is responsible for compiling high-level actions into Tasks, as well as
 * ensuring proper scheduling of tasks,
 * Tasks can contain 1 or more operations, which, when executed, will
 */
public class Compiler {

    public static Schedule compile(List<ActionBase> actions) {

        Map<String, Queue<ActionBase>> dropletActions = new HashMap<>();

        actions.forEach(action -> {
            Set<Droplet> affectedDroplets = action.dropletsRequiredForExecution();
            if(affectedDroplets == null) return;
            affectedDroplets.forEach(droplet -> {
                if (!dropletActions.containsKey(droplet.getID())) {
                    dropletActions.put(droplet.getID(), new LinkedList<>());
                }
                dropletActions.get(droplet.getID()).add(action);
            });

            if(action instanceof InputAction inputAction) {
                Droplet droplet = inputAction.getDroplet();
                if (!dropletActions.containsKey(droplet.getID())) {
                    dropletActions.put(droplet.getID(), new LinkedList<>());
                }
                dropletActions.get(droplet.getID()).add(action);
            }

        });

        return new Schedule(dropletActions);
    }

}
