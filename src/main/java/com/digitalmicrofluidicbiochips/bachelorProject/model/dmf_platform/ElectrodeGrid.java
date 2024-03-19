package com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform;

import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import lombok.Getter;

import java.util.List;

/**
 * This is a representation of the grid of electrodes on the Biochip.
 * Electrodes are placed in an x * y grid.
 * <br>
 * Converts a list of electrodes into a grid.
 * Methods for calculating the available electrode grid for a droplet (activeDroplet) based on the size of this,
 * as well as the placement of other droplets (obstacleDroplets).
 */
public class ElectrodeGrid {

    private final ProgramConfiguration programConfiguration;
    @Getter
    private Electrode[][] grid;
    private int electrodeSizeX, electrodeSizeY;
    private int xMax, yMax;

    public ElectrodeGrid(ProgramConfiguration programConfiguration) {
        this.programConfiguration = programConfiguration;
        initializeGrid();
    }


    public void initializeGrid() {
        // Get list of electrodes
        List<Electrode> electrodes = programConfiguration.getPlatformInformation().getElectrodes();

        // Get size of electrodes (assuming that all electrodes are the same size as the first)
        electrodeSizeX = electrodes.getFirst().getSizeX();
        electrodeSizeY = electrodes.getFirst().getSizeY();

        // Get size of platform
        int platformSizeX = programConfiguration.getPlatformInformation().getSizeX();
        int platformSizeY = programConfiguration.getPlatformInformation().getSizeY();

        // Construct m * n grid of electrodes
        if (platformSizeX % electrodeSizeX == 0 && platformSizeY % electrodeSizeY == 0) {
            xMax = (platformSizeX / electrodeSizeX);
            yMax = (platformSizeY / electrodeSizeY);

            // Grid will be Electrode[x][y]
            grid = new Electrode[xMax][yMax];
            for (int y = 0; y < yMax; y++) {
                for (int x = 0; x < xMax; x++) {
                    int currentElectrodeIndex = y * xMax + x;
                    grid[x][y] = electrodes.get(currentElectrodeIndex);
                }
            }
        } else {
            // We will not support if platform size isn't directly divisible by electrode size.
            // This could happen if electrodes have different sizes or there is space between individual electrodes.
            System.err.println("Platform size not directly divisible by electrode size");
        }
    }


    // Returns a grid of the available electrodes for the activeDroplet based on the placement of the other
    // obstacleDroplets - a non-available electrode is set to null
    public Electrode[][] getAvailableElectrodeGrid(Droplet activeDroplet, List<Droplet> obstacleDroplets) {
        Electrode[][] availableGrid = grid.clone();

        int activeDropletSize = getRoundedDropletDiameterInFullElectrodes(activeDroplet);

        for (Droplet obstacleDroplet : obstacleDroplets) {
            removeElectrodesForObstacleDroplet(availableGrid, obstacleDroplet, activeDropletSize);
        }

        return availableGrid;
    }


    // Returns the electrodes
    private void removeElectrodesForObstacleDroplet(Electrode[][] availableGrid, Droplet obstacleDroplet, int activeDropletSize) {
        // Electrode at top left corner of droplet + padding and safe area determined by the active droplet's size - ensure no out of bounds
        int x1 = Math.max(obstacleDroplet.getPositionX() - activeDropletSize, 0);
        int y1 = Math.max(obstacleDroplet.getPositionY() - activeDropletSize, 0);

        // Maximum coordinates for x and y in the grid
        int maxXCoord = grid.length - 1;
        int maxYCoord = grid[0].length - 1;

        // Diameter of obstacle droplet
        int obstacleDropletDiameter = getRoundedDropletDiameterInFullElectrodes(obstacleDroplet);

        // Electrode at bottom right corner of droplet + safe area - ensure no out of bounds
        int x2 = Math.min(maxXCoord, (obstacleDroplet.getPositionX() + obstacleDropletDiameter));
        int y2 = Math.min(maxYCoord, (obstacleDroplet.getPositionY() + obstacleDropletDiameter));

        removeObstacleDropletElectrodes(availableGrid, x1, y1, x2, y2);
        removeInaccessibleBorderElectrodes(availableGrid, activeDropletSize);
    }


    // Removes electrodes in a rectangular area
    // (x1,y1) is the top left corner and (x2,y2) is the bottom right corner
    private void removeObstacleDropletElectrodes(Electrode[][] availableGrid, int x1, int y1, int x2, int y2) {
        for (int y = y1; y <= y2; y++) {
            for (int x = x1; x <= x2; x++) {
                availableGrid[x][y] = null;
            }
        }
    }


    // Removes electrodes from the right and bottom border that are inaccessible by the active droplet due to the size of it
    private void removeInaccessibleBorderElectrodes(Electrode[][] availableGrid, int activeDropletSize) {
        for (int i = 1; i < activeDropletSize; i++) {
            // Remove right border
            for (int y = 0; y < yMax; y++) {
                availableGrid[xMax - i][y] = null;
            }
            // Remove bottom border
            for (int x = 0; x < xMax; x++) {
                availableGrid[x][yMax - i] = null;
            }
        }
    }


    // Gets the width of the droplet in full electrodes from the diameter
    private int getRoundedDropletDiameterInFullElectrodes(Droplet droplet) {
        return (int) Math.ceil((double) droplet.getDiameter() / electrodeSizeX);
    }
}