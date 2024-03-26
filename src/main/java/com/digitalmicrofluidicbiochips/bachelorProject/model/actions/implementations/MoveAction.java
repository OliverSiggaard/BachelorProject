package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations;

import com.digitalmicrofluidicbiochips.bachelorProject.executor.path_finding.AStar;
import com.digitalmicrofluidicbiochips.bachelorProject.executor.path_finding.DropletMove;
import com.digitalmicrofluidicbiochips.bachelorProject.executor.path_finding.IPathFinder;
import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.DropletStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Electrode;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.ElectrodeGrid;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class MoveAction extends ActionBase {

    private final int posX;
    private final int posY;
    @Setter
    private ActionBase nextAction = null;
    @Setter
    private Droplet droplet = null;

    public MoveAction(
            String id,
            int posX,
            int posY
    ) {
        super(id);
        this.posX = posX;
        this.posY = posY;
    }

    @Override
    public Set<Droplet> affectedDroplets() {
        return new HashSet<>(Set.of(droplet));
    }

    @Override
    public void beforeExecution() {
        droplet.setStatus(DropletStatus.UNAVAILABLE);
        setStatus(ActionStatus.IN_PROGRESS);
    }

    @Override
    public void executeTick(ProgramConfiguration programConfiguration) {
        // if droplet is not moving -> get move
        //    if an actual direction is found, assign movement to droplet, and tick droplet.
        //       This should return a list of electrodes to be toggled.
        //    if no direction is found, do nothing, and attempt again next tick.
        // if droplet is moving -> tick droplet.
        //    This should return a list of electrodes to be toggled.

        if(droplet.getPositionX() == posX && droplet.getPositionY() == posY) {
            setStatus(ActionStatus.COMPLETED);
        }
    }

    @Override
    public void afterExecution() {
        droplet.setStatus(DropletStatus.AVAILABLE);
    }

    private DropletMove getDropletMove(ProgramConfiguration programConfiguration) {
        ElectrodeGrid electrodeGrid = new ElectrodeGrid(programConfiguration);
        List<Droplet> obstacleDroplets = programConfiguration.getDroplets().stream()
                .filter(droplet -> droplet != this.droplet &&
                        (droplet.getStatus() == DropletStatus.AVAILABLE ||
                                droplet.getStatus() == DropletStatus.UNAVAILABLE))
                .toList();

        Electrode[][] availableElectrodeGrid = electrodeGrid.getAvailableElectrodeGrid(droplet, obstacleDroplets);

        IPathFinder pathFinder = programConfiguration.getPathFinder();
        return pathFinder.getMove(droplet, availableElectrodeGrid, posX, posY);
    }


}
