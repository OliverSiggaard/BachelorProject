package com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform;

import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a representation of the grid of electrodes of the Biochip.
 * Electrodes are placed in an m * n grid.
 */
public class ElectrodeGrid {

    private final ProgramConfiguration programConfiguration;
    @Getter
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
            int xMax = (platformSizeX / electrodeSizeX);
            int yMax = (platformSizeY / electrodeSizeY);

            // Grid will be Electrode[x][y]
            grid = new Electrode[xMax][yMax];
            for (int y = 0; y < yMax; y++) {
                for (int x = 0; x < xMax; x++) {
                    int currentElectrodeIndex = y * xMax + x;
                    grid[x][y] = electrodes.get(currentElectrodeIndex);
                }
            }
        } else {
            /*
                TODO: Handle if platform size is not directly divisible by electrode size.
                 This can happen if electrodes have different sizes (which we will not handle),
                 but it can also happen if there is space between the individual electrodes.
             */
            System.out.println("Platform size not directly divisible by electrode size");
        }
    }

    // Returns all electrodes that the droplet is touching
    // TODO: Handle edge cases (if dropletPosition is 60 and electrodeSizeX is 20 then it will return 3 but in fact it is the border of 2 and 3 might not exist (on a very small board).
    //  What about literal edge cases where a droplet is up against the border of electrode grid, can it happen that the pos + diameter can cause out of bounds (literally).
    public List<Electrode> getElectrodesFromDroplet(Droplet droplet) {
        // Electrode at top left corner of droplet
        int x1 = droplet.getPositionX() / electrodeSizeX;
        int y1 = droplet.getPositionY() / electrodeSizeY;

        // Electrode at bottom right corner of droplet
        int x2 = (droplet.getPositionX() + droplet.getDiameter()) / electrodeSizeX;
        int y2 = (droplet.getPositionY() + droplet.getDiameter()) / electrodeSizeY;

        List<Electrode> dropletElectrodes = new ArrayList<>();

        for (int y = y1; y <= y2; y++) {
            for (int x = x1; x <= x2; x++) {
                dropletElectrodes.add(grid[x][y]);
            }
        }

        return dropletElectrodes;
    }

    // TODO: Should have a method for getting the droplet electrodes + the safe space
    //  The logic for the method above can possibly be used again (extract)
}