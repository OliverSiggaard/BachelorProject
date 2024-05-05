package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


public class ActionTickResult {
    @Getter
    private final List<IDmfCommand> tickCommands;
    private boolean somethingHappenedInTick;

    public ActionTickResult() {
        this.tickCommands = new ArrayList<>();
        this.somethingHappenedInTick = false;
    }

    public ActionTickResult(IDmfCommand command) {
        this.tickCommands = new ArrayList<>();
        tickCommands.add(command);
        somethingHappenedInTick = true;
    }

    public ActionTickResult(List<IDmfCommand> commands) {
        this.tickCommands = commands;
        somethingHappenedInTick = true;
    }


    public void addCommand(IDmfCommand command) {
        tickCommands.add(command);
        somethingHappenedInTick = true;
    }

    public void addCommands(List<IDmfCommand> commands) {
        if(commands.isEmpty()) {
            return;
        }

        tickCommands.addAll(commands);
        somethingHappenedInTick = true;
    }

    public void addTickResult(ActionTickResult tickResult) {
        if(tickCommands == null ||tickResult == null) {
            throw new IllegalArgumentException("Commands cannot be null");
        }

        this.tickCommands.addAll(tickResult.getTickCommands());
        this.somethingHappenedInTick = somethingHappenedInTick || tickResult.somethingHappenedInTick;
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

    public void setSomethingHappenedInTick(boolean somethingHappenedInTick) {
        this.somethingHappenedInTick = somethingHappenedInTick;
    }

    public boolean somethingHappenedInTick() {
        return somethingHappenedInTick;
    }
}
