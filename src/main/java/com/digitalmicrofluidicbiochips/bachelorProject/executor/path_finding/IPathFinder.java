package com.digitalmicrofluidicbiochips.bachelorProject.executor.path_finding;

import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Electrode;

public interface IPathFinder {
    DropletMove getMove(Droplet activeDroplet, Electrode[][] availableGrid, int goalX, int goalY);
}
