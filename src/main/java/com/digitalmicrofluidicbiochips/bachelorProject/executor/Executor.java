package com.digitalmicrofluidicbiochips.bachelorProject.executor;

import com.digitalmicrofluidicbiochips.bachelorProject.compiler.Compiler;
import com.digitalmicrofluidicbiochips.bachelorProject.compiler.Schedule;
import com.digitalmicrofluidicbiochips.bachelorProject.errors.DmfException;
import com.digitalmicrofluidicbiochips.bachelorProject.errors.ExceptionHandler;
import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.ActionTickResult;
import com.digitalmicrofluidicbiochips.bachelorProject.utils.ProgramConfigurationToDmfAsJson;
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

    public ExecutionResult compileProgramToDmf() {
        // Create a JSON file containing the droplets initially placed on the grid. (for the simulator)
        JsonNode dmfConfiguration = ProgramConfigurationToDmfAsJson.convertProgramConfigurationToDmfAsJson(programConfiguration);

        List<ActionTickResult> tickResults = new ArrayList<>();
        try {
            while(true) {
                // Get all actions that are to be ticked in this tick.
                List<ActionBase> actionsToBeTicked = schedule.getActionsToBeTicked();

                // If there are no actions to be ticked, The program is done. Either it has completed or it is stuck.
                if(actionsToBeTicked.isEmpty()) break;

                // Execute the tick.
                ActionTickResult tickResult = executeTick(actionsToBeTicked);
                tickResults.add(tickResult);
            }

        // If an error occurs, catch it and return an ExecutionResult with the error message.
        // The ticks that have been compiled to this point will also be returned, in case a partial execution is wanted.
        } catch (DmfException e) {
            ExecutionResult executionResult = new ExecutionResult(tickResults, dmfConfiguration);
            String errorMessage = ExceptionHandler.getErrorMessage(e);
            executionResult.setErrorMessage(errorMessage);
            return executionResult;
        }

        return new ExecutionResult(tickResults, dmfConfiguration);
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

        if(!tickResult.isTickShouldBeExecuted()) {
            //return tickResults;
            throw new DmfException("The program got stuck. A tick was reached, that was not able to execute any actions.");
        }

        return tickResult;
    }


    private ActionTickResult tickAction(ActionBase action) {
        if(action.getStatus() == ActionStatus.NOT_STARTED) {
            action.beforeExecution(programConfiguration);
        }

        return action.executeTick(programConfiguration);
    }
}
