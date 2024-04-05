package com.digitalmicrofluidicbiochips.bachelorProject.executor.path_finding;

import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Electrode;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.ElectrodeGrid;

public interface IPathFinder {
    DropletMove getMove(Droplet activeDroplet, ElectrodeGrid availableGrid, int goalX, int goalY);
}
