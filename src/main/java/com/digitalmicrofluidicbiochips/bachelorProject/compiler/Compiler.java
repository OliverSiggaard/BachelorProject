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

            if(action instanceof InputAction inputAction) {
                Droplet droplet = inputAction.getDroplet();
                if (!dropletActions.containsKey(droplet)) {
                    dropletActions.put(droplet, new LinkedList<>());
                }
                dropletActions.get(droplet).add(action);
            }

            if(action instanceof SplitAction splitAction) {
                compileSplitAction(splitAction, dropletActions);
            }

        });

        return new Schedule(dropletActions);
    }


    private static void compileSplitAction(SplitAction splitAction, Map<Droplet, Queue<ActionBase>> dropletActions) {
        Droplet droplet1 = splitAction.getResultDroplet1();
        Droplet droplet2 = splitAction.getResultDroplet2();
        MoveAction moveAction1 = new MoveAction(null, splitAction.getPosX1(), splitAction.getPosY1());
        moveAction1.setDroplet(droplet1);
        MoveAction moveAction2 = new MoveAction(null, splitAction.getPosX2(), splitAction.getPosY2());
        moveAction2.setDroplet(droplet2);

        if (!dropletActions.containsKey(droplet1)) {
            dropletActions.put(droplet1, new LinkedList<>());
        }
        dropletActions.get(droplet1).add(moveAction1);

        if (!dropletActions.containsKey(droplet2)) {
            dropletActions.put(droplet2, new LinkedList<>());
        }
        dropletActions.get(droplet2).add(moveAction2);
    }


}
