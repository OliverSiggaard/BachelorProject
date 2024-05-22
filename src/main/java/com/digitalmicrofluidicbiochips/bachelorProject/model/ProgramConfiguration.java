package com.digitalmicrofluidicbiochips.bachelorProject.model;

import com.digitalmicrofluidicbiochips.bachelorProject.compiler.Schedule;
import com.digitalmicrofluidicbiochips.bachelorProject.executor.path_finding.AStar;
import com.digitalmicrofluidicbiochips.bachelorProject.executor.path_finding.IPathFinder;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations.InputAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.*;
import lombok.Getter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
        this.droplets = getAllDropletsProducedByActions();
        this.pathFinder = new AStar();
        this.electrodeGrid = ElectrodeGridFactory.getElectrodeGrid(this);

        // Verify that all program actions are valid.
        for (ActionBase action : programActions) {
            action.verifyProperties(this);
        }
    }

    /**
     * Used to get all the droplets that are currently on the DMF platform.
     * Droplets that are not created yet or are already consumed are NOT included.
     * @return Collection of droplets that are currently on the DMF platform.
     */
    public Collection<Droplet> getDropletsOnDmfPlatform() {
        return droplets.stream()
                .filter(droplet ->
                        droplet.getStatus() != DropletStatus.NOT_CREATED &&
                        droplet.getStatus() != DropletStatus.CONSUMED)
                .collect(Collectors.toSet());
    }

    private List<Droplet> getAllDropletsProducedByActions() {
        return programActions.stream()
                .flatMap(action -> action.dropletsProducedByExecution().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    public Schedule getScheduleFromActions() {
        return new Schedule(programActions);
    }

}
