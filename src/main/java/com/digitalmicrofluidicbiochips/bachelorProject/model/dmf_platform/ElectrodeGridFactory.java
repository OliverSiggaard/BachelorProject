package com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform;

import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;

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

        int activeDropletDiameter = getRoundedDropletDiameterInFullElectrodes(activeDroplet, electrodeGrid);

        for (Droplet obstacleDroplet : obstacleDroplets) {
            removeElectrodesForObstacleDroplet(electrodeGridClone, obstacleDroplet, activeDropletDiameter);
        }

        removeInaccessibleBorderElectrodes(electrodeGridClone, activeDropletDiameter);

        return electrodeGridClone;
    }


    // Returns the electrodes
    private static void removeElectrodesForObstacleDroplet(ElectrodeGrid availableGrid, Droplet obstacleDroplet, int activeDropletDiameter) {

        // Diameter of droplets ceiled to nearest electrode.
        int obstacleDropletDiameter = getRoundedDropletDiameterInFullElectrodes(obstacleDroplet, availableGrid);

        // Electrode at top left corner of droplet + padding and safe area determined by the active droplet's size - ensure no out of bounds
        int x1 = Math.max(obstacleDroplet.getPositionX() - activeDropletDiameter, 0);
        int y1 = Math.max(obstacleDroplet.getPositionY() - activeDropletDiameter, 0);

        // Maximum coordinates for x and y in the grid
        int maxXCoord = availableGrid.getXSize() - 1;
        int maxYCoord = availableGrid.getYSize() - 1;

        // Electrode at bottom right corner of droplet + safe area - ensure no out of bounds
        int x2 = Math.min(maxXCoord, (obstacleDroplet.getPositionX() + obstacleDropletDiameter));
        int y2 = Math.min(maxYCoord, (obstacleDroplet.getPositionY() + obstacleDropletDiameter));

        // In case obstacle-droplet is mid-move, it has enabled electrodes, that should also be considered as unavailable.
        switch (obstacleDroplet.getDropletMove()) {
            case DOWN -> y2 = Math.min(maxYCoord, y2 + 1);
            case UP -> y1 = Math.max(0, y1 - 1);
            case RIGHT -> x2 = Math.min(maxXCoord, x2 + 1);
            case LEFT -> x1 = Math.max(0, x1 - 1);
        }

        removeObstacleDropletElectrodes(availableGrid, x1, y1, x2, y2);
    }


    // Removes electrodes in a rectangular area
    // (x1,y1) is the top left corner and (x2,y2) is the bottom right corner
    private static void removeObstacleDropletElectrodes(ElectrodeGrid availableGrid, int x1, int y1, int x2, int y2) {
        for (int y = y1; y <= y2; y++) {
            for (int x = x1; x <= x2; x++) {
                availableGrid.removeElectrode(x, y);
            }
        }

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


    // Gets the width of the droplet in full electrodes from the diameter
    private static int getRoundedDropletDiameterInFullElectrodes(Droplet droplet, ElectrodeGrid electrodeGrid) {
        int electrodeSizeX = electrodeGrid.getElectrodeSizeOfElectrodeInGrid();

        return (int) Math.ceil((double) droplet.getDiameter() / electrodeSizeX);
    }
}