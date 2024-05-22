package com.digitalmicrofluidicbiochips.bachelorProject.utils;

import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.ActionTickResult;

import java.util.List;

public class TickResultsToStringConverter {
    public static String convertTickResultsToString(List<ActionTickResult> tickResults) {
        StringBuilder programStringBuilder = new StringBuilder();

        for (ActionTickResult tickResult : tickResults) {
            for (String command : tickResult.getBioAssemblyStringFromDmfCommands()) {
                programStringBuilder.append(command).append(System.lineSeparator());
            }
            programStringBuilder.append("TICK;").append(System.lineSeparator());
        }
        programStringBuilder.append("TSTOP;").append(System.lineSeparator());
        programStringBuilder.append("TICK;").append(System.lineSeparator());
        programStringBuilder.append("TICK;");

        return programStringBuilder.toString();
    }
}
