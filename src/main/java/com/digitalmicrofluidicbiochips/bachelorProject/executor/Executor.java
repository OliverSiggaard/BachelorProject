package com.digitalmicrofluidicbiochips.bachelorProject.executor;

import com.digitalmicrofluidicbiochips.bachelorProject.compiler.Compiler;
import com.digitalmicrofluidicbiochips.bachelorProject.compiler.Schedule;
import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.ActionTickResult;
import com.digitalmicrofluidicbiochips.bachelorProject.utils.ProgramConfigurationToDmfAsJson;
import com.digitalmicrofluidicbiochips.bachelorProject.utils.TickResultsToStringConverter;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

public class Executor {

    private final ProgramConfiguration programConfiguration;

    private final Schedule schedule;

    public Executor(ProgramConfiguration programConfiguration) {
        this.programConfiguration = programConfiguration;
        this.schedule = Compiler.compile(programConfiguration.getProgramActions());
    }

    public ExecutionResult startExecution() {
        JsonNode dmfConfiguration = ProgramConfigurationToDmfAsJson.convertProgramConfigurationToDmfAsJson(programConfiguration);

        List<ActionTickResult> tickResults = runExecutionLoop();
        String stringTickResults = TickResultsToStringConverter.convertTickResultsToString(tickResults);

        return new ExecutionResult(stringTickResults, dmfConfiguration);
    }

    public List<ActionTickResult> runExecutionLoop() {
        List<ActionTickResult> tickResults = new ArrayList<>();
        int tick = 0;
        while(true) {
            ActionTickResult tickResult = new ActionTickResult();

            // Get all actions that are to be ticked in this tick.
            List<ActionBase> actionsToBeTicked = schedule.getActionsToBeTicked();

            // If there are no actions to be ticked, The program is done. Either it has completed or it is stuck.
            if(actionsToBeTicked.isEmpty()) break;

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

            // If the tickResult is not able to execute any actions, then the program is stuck.
            // In this case, the actions are attempted again, but with the attemptToResolveDeadlock flag set to true.
            if(!tickResult.isTickShouldBeExecuted()) {
                for(ActionBase action : actionsToBeTicked) {
                    action.setAttemptToResolveDeadlock(true);
                    ActionTickResult actionResult = tickAction(action);
                    action.setAttemptToResolveDeadlock(false);
                    tickResult.addTickResult(actionResult);

                    // As soon as an action is able to execute, the program should skip to the next tick.
                    // If all actions are allowed run with the attemptToResolveDeadlock flag set to true,
                    // the program might enter an infinite mirror-loop. By only allowing the first action to execute,
                    // we break the symmetry, and the program is hopefully able to continue.
                    if(tickResult.isTickShouldBeExecuted()) break;
                }
            }

            // If the tickResult is still not able to execute any actions, then the program is stuck.
            if(!tickResult.isTickShouldBeExecuted()) {
                //return tickResults;
                throw new RuntimeException("The program got stuck. A tick was reached, that was not able to execute any actions.");
            }

            tickResults.add(tickResult);
            tick++;

            if(tick > 1000) {
                return tickResults;
                //throw new RuntimeException("The program took too long to execute. It was stopped after 1000 ticks.");
            }
        }
        return tickResults;
    }


    private ActionTickResult tickAction(ActionBase action) {
        if(action.getStatus() == ActionStatus.NOT_STARTED) {
            action.beforeExecution(programConfiguration);
        }

        return action.executeTick(programConfiguration);
    }
}
