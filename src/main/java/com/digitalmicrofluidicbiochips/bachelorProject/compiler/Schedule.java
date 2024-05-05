package com.digitalmicrofluidicbiochips.bachelorProject.compiler;

import com.digitalmicrofluidicbiochips.bachelorProject.errors.DmfExceptionMessage;
import com.digitalmicrofluidicbiochips.bachelorProject.errors.DmfInvalidInputException;
import com.digitalmicrofluidicbiochips.bachelorProject.errors.ExceptionHandler;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations.InputAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.DropletStatus;

import java.util.*;

/**
 * A schedule is a collection of tasks that are to be executed by the executor. The schedule is responsible for
 * ensuring that tasks are executed in the correct order, and that that only available tasks are executable.
 */
public class Schedule {

    private final Map<String, Queue<ActionBase>> dropletActions;

    public Schedule(List<ActionBase> actions) {
        dropletActions = new HashMap<>();

        actions.forEach(action -> {
            Set<Droplet> producedByExecution = action.dropletsProducedByExecution();

            for (Droplet droplet : producedByExecution) {
                if(droplet == null) {
                    throw new DmfInvalidInputException(DmfExceptionMessage.DROPLET_NOT_DEFINED_ON_ACTION.getMessage());
                }

                if (dropletActions.containsKey(droplet.getID())) {
                    throw new DmfInvalidInputException(DmfExceptionMessage.DROPLET_PRODUCED_BY_MULTIPLE_ACTIONS.getMessage(droplet));
                }

                dropletActions.put(droplet.getID(), new LinkedList<>());

                if(action instanceof InputAction) {
                    dropletActions.get(droplet.getID()).add(action);
                }
            }

            Set<Droplet> dropletsRequiredForExecution = action.dropletsRequiredForExecution();
            if(dropletsRequiredForExecution == null) return;

            for (Droplet droplet : dropletsRequiredForExecution) {
                if(droplet == null) {
                    throw new DmfInvalidInputException(DmfExceptionMessage.DROPLET_NOT_DEFINED_ON_ACTION.getMessage());
                }

                if (!dropletActions.containsKey(droplet.getID())) {
                    throw new DmfInvalidInputException(DmfExceptionMessage.DROPLET_USED_BEFORE_PRODUCED_BY_ACTION.getMessage(droplet));
                }

                dropletActions.get(droplet.getID()).add(action);
            }
        });
    }

    public void updateSchedule() {
        for ( Queue<ActionBase> actions : dropletActions.values() ) {
            while(!actions.isEmpty() && actions.peek().getStatus() == ActionStatus.COMPLETED) {
                actions.poll();
            }
        }
    }

    public List<ActionBase> getActionsToBeTicked() {
        List<ActionBase> actionsToBeTicked = new ArrayList<>();
        for ( Queue<ActionBase> actions : dropletActions.values() ) {
            if(actions.isEmpty()) {
                continue;
            }
            ActionBase action = actions.peek();

            if(action.getStatus() == ActionStatus.COMPLETED) {
                throw new IllegalStateException("Action should have been removed from queue");
            }

            if(action.getStatus() == ActionStatus.NOT_STARTED && !allDropletsAvailable(action.dropletsRequiredForExecution())) {
                continue;
            }

            if(!allRequiredDropletsHasActionAsCurrentAction(action)) {
                continue;
            }

            actionsToBeTicked.add(actions.peek());
        }

        return actionsToBeTicked.stream().distinct().toList();
    }

    private boolean allRequiredDropletsHasActionAsCurrentAction(ActionBase actionBase) {
        for (Droplet droplet : actionBase.dropletsRequiredForExecution()) {
            if(!dropletActions.containsKey(droplet.getID())) {
                throw new IllegalStateException("Droplet should have been in the map");
            }

            if(dropletActions.get(droplet.getID()).isEmpty()) {
                throw new IllegalStateException("Droplet should have an action in the queue");
            }

            if(!dropletActions.get(droplet.getID()).peek().equals(actionBase)) {
                return false;
            }
        }

        return true;
    }

    private boolean allDropletsAvailable(Set<Droplet> droplets) {
        return droplets.stream().allMatch(droplet -> droplet.getStatus().equals(DropletStatus.AVAILABLE));
    }
}
