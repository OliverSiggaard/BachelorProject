package com.digitalmicrofluidicbiochips.bachelorProject.executor;

import com.digitalmicrofluidicbiochips.bachelorProject.compiler.Compiler;
import com.digitalmicrofluidicbiochips.bachelorProject.compiler.Schedule;
import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionStatus;

import java.util.List;

public class Executor {

    private final ProgramConfiguration programConfiguration;

    private final Schedule schedule;

    public Executor(ProgramConfiguration programConfiguration) {
        this.programConfiguration = programConfiguration;
        this.schedule = Compiler.compile(programConfiguration.getProgramActions());


        while(true) {
            List<ActionBase> actionsToBeTicked = schedule.getActionsToBeTicked();

            // If there are no actions to be ticked, The program is done.
            if(actionsToBeTicked.isEmpty()) break;

            for(ActionBase action : actionsToBeTicked) {
                action.executeTick(programConfiguration);

                if(action.getStatus() == ActionStatus.COMPLETED) {
                    action.afterExecution();
                    schedule.updateSchedule();
                }
            }
        }
    }

    public void startExecution() {
        throw new UnsupportedOperationException("Not implemented yet");
    }









}
