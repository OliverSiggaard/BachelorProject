package com.digitalmicrofluidicbiochips.bachelorProject.testUtils;

import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.IDmfCommand;

// Implementation of the IDmfCommand interface - only used for testing purposes

public class DmfCommand implements IDmfCommand {
    private final String command;

    public DmfCommand(String command) {
        this.command = command;
    }

    @Override
    public String getBioAssemblyInstruction() {
        return command;
    }

}
