package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ActionTickResult {
    private final List<IDmfCommand> tickCommands;
    private boolean tickShouldBeExecuted;

    public ActionTickResult() {
        this.tickCommands = new ArrayList<>();
        this.tickShouldBeExecuted = false;
    }

    public ActionTickResult(IDmfCommand command) {
        this.tickCommands = new ArrayList<>();
        tickCommands.add(command);
        tickShouldBeExecuted = true;
    }

    public ActionTickResult(List<IDmfCommand> commands) {
        this.tickCommands = commands;
        tickShouldBeExecuted = true;
    }


    public void addCommand(IDmfCommand command) {
        tickCommands.add(command);
        tickShouldBeExecuted = true;
    }

    public void addCommands(List<IDmfCommand> commands) {
        if(commands.isEmpty()) {
            return;
        }

        tickCommands.addAll(commands);
        tickShouldBeExecuted = true;
    }

    public void addTickResult(ActionTickResult tickResult) {
        this.tickCommands.addAll(tickResult.getTickCommands());
        this.tickShouldBeExecuted = tickShouldBeExecuted || tickResult.tickShouldBeExecuted;
    }

    public void updateModelWithCommands() {
        for (IDmfCommand command : tickCommands) {
            command.updateModelWithCommand();
        }
    }

    public List<String> getTickCommandsAsStrings() {
        List<String> commands = new ArrayList<>();
        for (IDmfCommand command : tickCommands) {
            commands.add(command.getDmfCommand());
        }
        return commands;
    }

    public void setTickShouldBeExecuted(boolean tickShouldBeExecuted) {
        this.tickShouldBeExecuted = tickShouldBeExecuted;
    }
}
