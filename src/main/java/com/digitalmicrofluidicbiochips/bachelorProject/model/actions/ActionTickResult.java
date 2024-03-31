package com.digitalmicrofluidicbiochips.bachelorProject.model.actions;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ActionTickResult {

    private final List<String> tickCommands;

    public ActionTickResult() {
        this.tickCommands = new ArrayList<>();
    }

    public ActionTickResult(String command) {
        this.tickCommands = new ArrayList<>();
    }

    public ActionTickResult(List<String> commands) {
        this.tickCommands = commands;
    }


    public void addCommand(String command) {
        tickCommands.add(command);
    }

    public void addTickResult(ActionTickResult tickResult) {
        tickCommands.addAll(tickResult.getTickCommands());
    }


}
