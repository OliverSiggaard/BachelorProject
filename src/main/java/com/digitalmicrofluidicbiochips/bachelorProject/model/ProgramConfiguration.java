package com.digitalmicrofluidicbiochips.bachelorProject.model;

import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.PlatformInformation;
import lombok.Getter;

import java.util.List;

@Getter
public class ProgramConfiguration {

    private final PlatformInformation initialDmfState;
    private final List<ActionBase> programActions;

    public ProgramConfiguration(PlatformInformation initialDmfState, List<ActionBase> programActions) {
        this.initialDmfState = initialDmfState;
        this.programActions = programActions;
    }
}
