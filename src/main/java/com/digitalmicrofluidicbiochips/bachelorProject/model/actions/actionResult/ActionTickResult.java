package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ActionTickResult {
    @Getter
    private final List<IDmfCommand> tickCommands;
    private boolean somethingHappenedInTick;

    public ActionTickResult() {
        this.tickCommands = new ArrayList<>();
        this.somethingHappenedInTick = false;
    }

    public ActionTickResult(IDmfCommand command) {
        if(command == null) {
            throw new IllegalArgumentException("Cannot instantiate a tick result with a null command.");
        }

        this.tickCommands = new ArrayList<>();
        tickCommands.add(command);
        somethingHappenedInTick = true;
    }

    public ActionTickResult(List<IDmfCommand> commands) {
        if(commands == null) {
            throw new IllegalArgumentException("Cannot instantiate a tick result with a null list of commands.");
        }
        if(commands.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Cannot instantiate a tick result with a null command.");
        }

        this.tickCommands = commands;
        somethingHappenedInTick = true;
    }


    public void addCommand(IDmfCommand command) {
        if(command == null) {
            throw new IllegalArgumentException("Attempting to add a null command to a tick result.");
        }
        tickCommands.add(command);
        somethingHappenedInTick = true;
    }

    public void addCommands(List<IDmfCommand> commands) {
        if(commands == null) {
            throw new IllegalArgumentException("Attempting to add a null list of commands to a tick result.");
        }
        if(commands.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Attempting to add a null command to a tick result.");
        }

        if(commands.isEmpty()) {
            return;
        }

        tickCommands.addAll(commands);
        somethingHappenedInTick = true;
    }

    public void addTickResult(ActionTickResult tickResult) {
        if(tickResult == null) {
            throw new IllegalArgumentException("Attempting to add a null tick result to a tick result.");
        }

        this.tickCommands.addAll(tickResult.getTickCommands());
        this.somethingHappenedInTick = somethingHappenedInTick || tickResult.somethingHappenedInTick;
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
