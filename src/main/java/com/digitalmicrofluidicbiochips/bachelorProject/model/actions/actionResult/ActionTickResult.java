package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult;

import java.util.ArrayList;
import java.util.List;

public class ActionTickResult {

    private final List<IDmfCommand> tickCommands;

    public ActionTickResult() {
        this.tickCommands = new ArrayList<>();
    }

    public ActionTickResult(IDmfCommand command) {
        this.tickCommands = new ArrayList<>();
        tickCommands.add(command);
    }

    public ActionTickResult(List<IDmfCommand> commands) {
        this.tickCommands = commands;
    }


    public void addCommand(IDmfCommand command) {
        tickCommands.add(command);
    }

    public void addTickResult(ActionTickResult tickResult) {
        this.tickCommands.addAll(tickResult.getTickCommands());
    }

    public void updateModelWithCommands() {
        for (IDmfCommand command : tickCommands) {
            command.updateModelWithCommand();
        }
    }

    public List<IDmfCommand> getTickCommands() {
        return tickCommands;
    }

    public List<String> getTickCommandsAsStrings() {
        List<String> commands = new ArrayList<>();
        for (IDmfCommand command : tickCommands) {
            commands.add(command.getDmfCommand());
        }
        return commands;
    }
}
