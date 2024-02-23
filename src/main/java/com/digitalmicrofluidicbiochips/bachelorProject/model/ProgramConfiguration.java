package com.digitalmicrofluidicbiochips.bachelorProject.model;

import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.PlatformInformation;
import lombok.Getter;

import java.util.List;

@Getter
public class ProgramConfiguration {

    private final PlatformInformation platformInformation;
    private final List<ActionBase> programActions;

    public ProgramConfiguration(PlatformInformation platformInformation, List<ActionBase> programActions) {
        this.platformInformation = platformInformation;
        this.programActions = programActions;
    }
}
