package com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform;

import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;

import java.util.List;

/**
 * This is a representation of the grid of electrodes of the Biochip.
 * Electrodes are placed in an m * n grid.
 */

public class ElectrodeGrid {

    private final ProgramConfiguration programConfiguration;
    private Electrode[][] grid;
    private int electrodeSizeX, electrodeSizeY;

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
            int m = (platformSizeX / electrodeSizeX) - 1;
            int n = (platformSizeY / electrodeSizeY) - 1;

            // Construct Biochip representation from electrodes
            grid = new Electrode[m][n];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    int currentElectrodeIndex = i * m + j;
                    grid[i][j] = electrodes.get(currentElectrodeIndex);
                }
            }
        } else {
            /*
                TODO: Handle if platform size is not directly divisible by electrode size.
                 This can happen if electrodes have different sizes (which we will not handle),
                 but it also includes if there is space between the individual electrodes.
             */
            System.out.println("Platform size not directly divisible by electrode size");
        }
    }

    // TODO: We need to change this method to return a list/array of all electrodes that the droplet is currently on,
    //  we can also make a similar method that gets the electrodes that the droplet is on + the "safety area"
    /*
    public Electrode getElectrodeFromCoords(int xCoord, int yCoord, int diameter) {
        // TODO: Handle edge cases e.g. 60/20 if there platform size x is 60. (out of bounds).
        return grid[xCoord / electrodeSizeX][yCoord / electrodeSizeY];
    }
     */
}