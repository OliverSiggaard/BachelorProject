package com.digitalmicrofluidicbiochips.bachelorProject.executor;

import com.digitalmicrofluidicbiochips.bachelorProject.compiler.Schedule;
import com.digitalmicrofluidicbiochips.bachelorProject.errors.DmfException;
import com.digitalmicrofluidicbiochips.bachelorProject.errors.ExceptionHandler;
import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.ActionTickResult;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.utils.ProgramConfigurationToDmfAsJson;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The executor is responsible for executing the program, by ticking the actions as they are
 * received by the schedule. The executor will continue ticking the actions until all actions
 * are completed, or the program is stuck in a deadlock.
 */
public class Executor {

    private final ProgramConfiguration programConfiguration;

    private final Schedule schedule;

    private final List<ActionTickResult> tickResults = new ArrayList<>();

    // Deadlock resolution variables.

    // The number of times the program will attempt to resolve a deadlock before giving up.
    // Since deadlocks are attempted to be resolved in a random order, multiple attempts might be needed.
    private final int MAX_ATTEMPTS_AT_RESOLVING_DEADLOCK = 1000;
    private int attemptsAtResolvingDeadlock;
    private List<ActionBase> actionsDuringLatestDeadlockAttempt;
    private List<Droplet> dropletsBeforeDeadlockAttempt;
    private final List<ActionTickResult> ticksFromDeadlockAttempt;

    
    public Executor(ProgramConfiguration programConfiguration) {
        this.programConfiguration = programConfiguration;
        this.schedule = programConfiguration.getScheduleFromActions();

        // Initialize deadlock resolution variables.
        this.attemptsAtResolvingDeadlock = 0;
        this.actionsDuringLatestDeadlockAttempt = new ArrayList<>();
        this.dropletsBeforeDeadlockAttempt = new ArrayList<>();
        this.ticksFromDeadlockAttempt = new ArrayList<>();
    }

    public ExecutionResult compileProgramToDmf() {
        // Create a JSON file containing the droplets initially placed on the grid. (for the simulator)
        JsonNode dmfConfiguration = ProgramConfigurationToDmfAsJson.convertProgramConfigurationToDmfAsJson(programConfiguration);

        try {
            tickResults.addAll(runExecutionLoopTillAllActionsAreCompleted());
        } catch (DmfException e) {
            // If an error occurs, catch it and return an ExecutionResult with the error message.
            // The ticks that have been compiled to this point will also be returned, in case a partial execution is wanted.
            ExecutionResult executionResult = new ExecutionResult(tickResults, dmfConfiguration);
            String errorMessage = ExceptionHandler.getErrorMessage(e);
            executionResult.setErrorMessage(errorMessage);
            return executionResult;
        }

        return new ExecutionResult(tickResults, dmfConfiguration);
    }

    /**
     * This method mutates tickResults, as it is a side-effect of the execution loop.
     */
    private List<ActionTickResult> runExecutionLoopTillAllActionsAreCompleted() throws DmfException {
        List<ActionTickResult> tickResults = new ArrayList<>();

        // The execution loop. This loop will continue until all actions are completed, or the program is stuck.
        while(true) {
            // Get all actions that are to be ticked in this tick.
            List<ActionBase> actionsToBeTicked = schedule.getActionsToBeTicked();

            boolean allActionsAreCompleted = actionsToBeTicked.isEmpty();
            if(allActionsAreCompleted) break;

            // To ensure that unnecessary ticks are not added to the tickResults, ticks from a deadlock attempts are
            // only added to the tickResults if the actions change (meaning that the deadlock was resolved)
            boolean deadlockResolved = attemptsAtResolvingDeadlock > 0 && !actionsDuringLatestDeadlockAttempt.equals(actionsToBeTicked);
            if(deadlockResolved) {
                tickResults.addAll(ticksFromDeadlockAttempt);
                ticksFromDeadlockAttempt.clear();
                attemptsAtResolvingDeadlock = 0;
            }

            boolean duplicateStateFoundDuringDeadlockResolution = attemptsAtResolvingDeadlock > 0 && dropletsBeforeDeadlockAttempt.equals(programConfiguration.getDroplets());
            if(duplicateStateFoundDuringDeadlockResolution) {
                // Start over. No point in keeping ticks that starts and ends in the same state.
                ticksFromDeadlockAttempt.clear();
            }

            // Execute the tick.
            ActionTickResult tickResult = executeTick(actionsToBeTicked);

            if(attemptsAtResolvingDeadlock > MAX_ATTEMPTS_AT_RESOLVING_DEADLOCK) {
                throw new DmfException("The program got stuck. We attempted to resolve the deadlock without success.");
            }

            if(isAttemptToResolveDeadlock()) {
                ticksFromDeadlockAttempt.add(tickResult);
                continue;
            }

            tickResults.add(tickResult);
        }

        return tickResults;
    }

    private ActionTickResult executeTick(List<ActionBase> actionsToBeTicked) throws DmfException {
        ActionTickResult tickResult = new ActionTickResult();

        // Tick all actions that are to be ticked.
        for(ActionBase action : actionsToBeTicked) {
            ActionTickResult actionResult = tickAction(action);
            tickResult.addTickResult(actionResult);
        }

        for(ActionBase action : actionsToBeTicked) {
            if(action.getStatus() == ActionStatus.COMPLETED) {
                action.afterExecution(programConfiguration);
                schedule.updateSchedule();
            }
        }

        if(tickResult.somethingHappenedInTick()) {
            return tickResult;
        }

        // If nothing happened when executing the tick normally, attempt to resolve the deadlock situation.
        boolean firstAttemptAtResolvingDeadlock = attemptsAtResolvingDeadlock == 0;
        if(firstAttemptAtResolvingDeadlock) {
            actionsDuringLatestDeadlockAttempt = actionsToBeTicked;
            dropletsBeforeDeadlockAttempt = programConfiguration.getDroplets().stream()
                    .map(Droplet::clone)
                    .collect(Collectors.toList());
        }
        attemptsAtResolvingDeadlock++;

        return executeTickAttemptToResolveDeadlock(actionsToBeTicked);
    }

    /**
     * This method is used to attempt to resolve a deadlock, by breaking the usual order of which
     * the actions are ticked. The actions are shuffled, and the first action that is able to execute
     * WITH the attemptToResolveDeadlock flag set to true, is allowed to execute.
     * @param actionsToBeTicked The actions that are to be ticked.
     * @return The tick result of a single action (if any) that was able to execute.
     */
    private ActionTickResult executeTickAttemptToResolveDeadlock(List<ActionBase> actionsToBeTicked) {
        ActionTickResult tickResult = new ActionTickResult();

        // Shuffle the actions to be ticked, in an attempt to break the symmetry.
        List<ActionBase> mutableActions = new ArrayList<>(actionsToBeTicked);
        Collections.shuffle(mutableActions);

        for(ActionBase action : mutableActions) {
            ActionTickResult actionResult = action.executeTickAttemptToResolveDeadlock(programConfiguration);
            tickResult.addTickResult(actionResult);

            // As soon as an action is able to execute, the program should skip to the next tick.
            // If all actions are allowed run with the attemptToResolveDeadlock flag set to true,
            // the program might enter an infinite mirror-loop. By only allowing the first action to execute,
            // we break the symmetry, and the program is hopefully able to continue.
            if(tickResult.somethingHappenedInTick()) break;
        }

        return tickResult;
    }


    private ActionTickResult tickAction(ActionBase action) {
        if(action.getStatus() == ActionStatus.NOT_STARTED) {
            action.beforeExecution(programConfiguration);
        }

        return action.executeTick(programConfiguration);
    }

    private boolean isAttemptToResolveDeadlock() {
        return attemptsAtResolvingDeadlock > 0;
    }

}
