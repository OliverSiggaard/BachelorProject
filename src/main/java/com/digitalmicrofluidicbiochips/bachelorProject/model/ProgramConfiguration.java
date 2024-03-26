package com.digitalmicrofluidicbiochips.bachelorProject.model;

import com.digitalmicrofluidicbiochips.bachelorProject.executor.path_finding.AStar;
import com.digitalmicrofluidicbiochips.bachelorProject.executor.path_finding.IPathFinder;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.PlatformInformation;
import lombok.Getter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class ProgramConfiguration {

    private final PlatformInformation platformInformation;
    private final List<ActionBase> programActions;
    private final List<Droplet> droplets;
    private final IPathFinder pathFinder;

    public ProgramConfiguration(PlatformInformation platformInformation, List<ActionBase> programActions) {
        this.platformInformation = platformInformation;
        this.programActions = programActions;
        this.droplets = programActions.stream()
                .flatMap(action -> action.affectedDroplets().stream())
                .distinct()
                .collect(Collectors.toList());
        this.pathFinder = new AStar();
    }
}
