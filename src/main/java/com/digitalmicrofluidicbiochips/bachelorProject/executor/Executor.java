package com.digitalmicrofluidicbiochips.bachelorProject.executor;

import com.digitalmicrofluidicbiochips.bachelorProject.compiler.Compiler;
import com.digitalmicrofluidicbiochips.bachelorProject.compiler.Schedule;
import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.ActionTickResult;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Executor {

    private final ProgramConfiguration programConfiguration;

    private final Schedule schedule;

    public Executor(ProgramConfiguration programConfiguration) {
        this.programConfiguration = programConfiguration;
        this.schedule = Compiler.compile(programConfiguration.getProgramActions());
    }

    public void startExecution() {
        List<ActionTickResult> tickResults = runExecutionLoop();

        writeTickResultsToBioAssemblyFile(tickResults);
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
                actionResult.updateModelWithCommands();
            }

            for(ActionBase action : actionsToBeTicked) {
                if(action.getStatus() == ActionStatus.COMPLETED) {
                    action.afterExecution();
                    schedule.updateSchedule();
                }
            }

            if(!tickResult.getTickCommands().isEmpty()) tickResults.add(tickResult);
        }
        return tickResults;
    }


    private ActionTickResult tickAction(ActionBase action) {
        if(action.getStatus() == ActionStatus.NOT_STARTED) {
            action.beforeExecution();
        }

        return action.executeTick(programConfiguration);
    }

    private void writeTickResultsToBioAssemblyFile(List<ActionTickResult> tickResults) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timeStamp = dateFormat.format(new Date());
        String fileName = "src/main/resources/output/" + timeStamp + ".basm";
        File outputFile = new File(fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (ActionTickResult tickResult : tickResults) {
                for (String command : tickResult.getTickCommandsAsStrings()) {
                    writer.write(command);
                    writer.newLine();
                }
                writer.write("TICK;");
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }










}
