package com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform;

import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.utils.DmfPlatformUtils;

import java.awt.*;
import java.util.List;

/**
 * This is a representation of the grid of electrodes on the Biochip.
 * Electrodes are placed in an x * y grid.
 * <br>
 * Converts a list of electrodes into a grid.
 * Methods for calculating the available electrode grid for a droplet (activeDroplet) based on the size of this,
 * as well as the placement of other droplets (obstacleDroplets).
 */
public class ElectrodeGridFactory {

    public static ElectrodeGrid getElectrodeGrid(ProgramConfiguration programConfiguration) {
        // Get list of electrodes
        List<Electrode> electrodes = programConfiguration.getPlatformInformation().getElectrodes();

        // Get size of electrodes (assuming that all electrodes are the same size as the first)
        int electrodeSizeX = electrodes.get(0).getSizeX();
        int electrodeSizeY = electrodes.get(0).getSizeY();

        // Get size of platform
        int platformSizeX = programConfiguration.getPlatformInformation().getSizeX();
        int platformSizeY = programConfiguration.getPlatformInformation().getSizeY();

        if(platformSizeX % electrodeSizeX != 0 || platformSizeY % electrodeSizeY != 0) {
            // We will not support if platform size isn't directly divisible by electrode size.
            // This could happen if electrodes have different sizes or there is space between individual electrodes.
            throw new IllegalArgumentException("Platform size not directly divisible by electrode size");
        }

        int xMax = (platformSizeX / electrodeSizeX);
        int yMax = (platformSizeY / electrodeSizeY);

        // Grid will be Electrode[x][y]
        Electrode[][] grid = new Electrode[xMax][yMax];
        for (int y = 0; y < yMax; y++) {
            for (int x = 0; x < xMax; x++) {
                int currentElectrodeIndex = y * xMax + x;
                grid[x][y] = electrodes.get(currentElectrodeIndex);
            }
        }
        return new ElectrodeGrid(grid);
    }


    // Returns a grid of the available electrodes for the activeDroplet based on the placement of the other
    // obstacleDroplets - a non-available electrode is set to null
    public static ElectrodeGrid getAvailableElectrodeGrid(ElectrodeGrid electrodeGrid, Droplet activeDroplet, List<Droplet> obstacleDroplets) {
        ElectrodeGrid electrodeGridClone = electrodeGrid.clone();

        // Diameter of droplet rounded up to nearest electrode.
        int electrodeWidth = electrodeGrid.getElectrodeSizeOfElectrodeInGrid();
        int activeDropletDiameter = DmfPlatformUtils.dropletDiameterInElectrodesCeil(activeDroplet, electrodeWidth);

        for (Droplet obstacleDroplet : obstacleDroplets) {
            removeElectrodesForObstacleDroplet(electrodeGridClone, obstacleDroplet, activeDroplet);
        }

        removeInaccessibleBorderElectrodes(electrodeGridClone, activeDropletDiameter);

        return electrodeGridClone;
    }

    private static void removeElectrodesForObstacleDroplet(ElectrodeGrid electrodeGrid, Droplet obstacleDroplet, Droplet activeDroplet) {
        // Getting the area that the active droplet can't access due to the obstacle droplet
        GridArea safeArea = activeDroplet.getDropletSafeArea(electrodeGrid, obstacleDroplet);

        // Removing the electrodes in the safe area, from the electrode grid
        electrodeGrid.removeElectrodes(safeArea);
    }


    // Removes electrodes from the right and bottom border that are inaccessible by the active droplet due to the size of it
    private static void removeInaccessibleBorderElectrodes(ElectrodeGrid availableGrid, int activeDropletSize) {
        int xMax = availableGrid.getXSize();
        int yMax = availableGrid.getYSize();
        for (int i = 1; i < activeDropletSize; i++) {
            // Remove right border
            for (int y = 0; y < yMax; y++) {
                availableGrid.removeElectrode(xMax - i, y);
            }
            // Remove bottom border
            for (int x = 0; x < xMax; x++) {
                availableGrid.removeElectrode(x, yMax - i);
            }
        }
    }
}