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
                    action.afterExecution();
                    schedule.updateSchedule();
                }
            }

            if(!tickResult.isTickShouldBeExecuted()) {
                return tickResults;
                //throw new RuntimeException("The program got stuck. A tick was reached, that was not able to execute any actions.");
            }

            tickResults.add(tickResult);
        }
        return tickResults;
    }


    private ActionTickResult tickAction(ActionBase action) {
        if(action.getStatus() == ActionStatus.NOT_STARTED) {
            action.beforeExecution();
        }

        return action.executeTick(programConfiguration);
    }
}
