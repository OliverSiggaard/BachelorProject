package com.digitalmicrofluidicbiochips.bachelorProject.model;

import com.digitalmicrofluidicbiochips.bachelorProject.executor.path_finding.AStar;
import com.digitalmicrofluidicbiochips.bachelorProject.executor.path_finding.IPathFinder;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.*;
import lombok.Getter;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class ProgramConfiguration {

    private final PlatformInformation platformInformation;
    private final List<ActionBase> programActions;
    private final List<Droplet> droplets;
    private final IPathFinder pathFinder;
    private final ElectrodeGrid electrodeGrid;

    public ProgramConfiguration(PlatformInformation platformInformation, List<ActionBase> programActions) {
        this.platformInformation = platformInformation;
        this.programActions = programActions;
        this.droplets = getDropletsFromInputActions();
        this.pathFinder = new AStar();
        this.electrodeGrid = ElectrodeGridFactory.getElectrodeGrid(this);
    }

    public Collection<Droplet> getDropletsOnDmfPlatform() {
        return droplets.stream()
                .filter(droplet -> droplet.getStatus() != DropletStatus.NOT_CREATED)
                .collect(Collectors.toSet());
    }

    public List<Droplet> getDropletsFromInputActions() {
        return programActions.stream()
                .flatMap(action -> {
                    Set<Droplet> affectedDroplets = action.dropletsRequiredForExecution();
                    return affectedDroplets != null ? affectedDroplets.stream() : Stream.empty();
                })
                .distinct()
                .collect(Collectors.toList());
    }


}
