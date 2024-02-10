package com.digitalmicrofluidicbiochips.bachelorProject.model;

import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.DmfPlatformState;
import lombok.Getter;

import java.util.List;

@Getter
public class ProgramConfiguration {
    private DmfPlatformState initialDmfState;
    private List<ActionBase> programActions;
}
