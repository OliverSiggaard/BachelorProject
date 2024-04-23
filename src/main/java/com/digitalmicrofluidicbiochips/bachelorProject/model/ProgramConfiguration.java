package com.digitalmicrofluidicbiochips.bachelorProject.model;

import com.digitalmicrofluidicbiochips.bachelorProject.executor.path_finding.AStar;
import com.digitalmicrofluidicbiochips.bachelorProject.executor.path_finding.IPathFinder;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations.InputAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations.MergeAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations.SplitAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.*;
import lombok.Getter;

import java.util.Collection;
import java.util.HashSet;
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
        this.droplets = getAllDropletsFromActions();
        this.pathFinder = new AStar();
        this.electrodeGrid = ElectrodeGridFactory.getElectrodeGrid(this);
    }

    public Collection<Droplet> getDropletsOnDmfPlatform() {
        return droplets.stream()
                .filter(droplet ->
                        droplet.getStatus() != DropletStatus.NOT_CREATED &&
                        droplet.getStatus() != DropletStatus.CONSUMED)
                .collect(Collectors.toSet());
    }

    private List<Droplet> getAllDropletsFromActions() {
        Set<Droplet> droplets = new HashSet<>();

        droplets.addAll(programActions.stream()
                .filter(action -> action instanceof InputAction)
                .map(action -> {
                    InputAction inputAction = (InputAction) action;
                    return inputAction.getDroplet();
                })
                .toList()
        );

        droplets.addAll(programActions.stream()
                .filter(action -> action instanceof MergeAction)
                .map(action -> {
                    MergeAction mergeAction = (MergeAction) action;
                    return mergeAction.getResultDroplet();
                })
                .toList()
        );

        droplets.addAll(programActions.stream()
                .filter(action -> action instanceof SplitAction)
                .flatMap(action -> {
                    SplitAction splitAction = (SplitAction) action;
                    return Set.of(splitAction.getResultDroplet1(), splitAction.getResultDroplet2()).stream();
                })
                .toList()
        );

        return droplets.stream().toList();
    }

    public List<Droplet> getDropletsFromInputActions() {
        return programActions.stream()
                .filter(action -> action instanceof InputAction)
                .map(action -> {
                    InputAction inputAction = (InputAction) action;
                    return inputAction.getDroplet();
                })
                .distinct()
                .collect(Collectors.toList());
    }



}
