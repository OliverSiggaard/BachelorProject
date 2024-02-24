package com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform;

import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a representation of the grid of electrodes on the Biochip.
 * Electrodes are placed in an x * y grid.
 * <br>
 * The main purpose here is to convert a list of electrodes into a grid,
 * as well as have methods that returns the electrodes related to a droplet
 * (bot with and without the safe area around the droplet)
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
    public List<Electrode> getElectrodesFromDroplet(Droplet droplet) {
        // Droplet coordinates are stored as [x1, y1, x2, y2]
        int[] electrodeCoords = convertAbsDropletPosToElectrodeCoords(droplet);
        return getElectrodes(electrodeCoords[0], electrodeCoords[1], electrodeCoords[2], electrodeCoords[3]);
    }


    // Returns all electrodes that the droplet is touching + the safe area of one electrode around the droplet itself
    public List<Electrode> getSafeAreaElectrodesFromDroplet(Droplet droplet) {
        // Droplet coordinates are stored as [x1, y1, x2, y2]
        int[] electrodeCoords = convertAbsDropletPosToElectrodeCoords(droplet);

        // Electrode at top left corner of safe area
        int x1Safe = Math.max(electrodeCoords[0] - 1, 0);
        int y1Safe = Math.max(electrodeCoords[1] - 1, 0);

        // Max x and y electrode coordinates
        int maxXCoord = grid.length - 1;
        int maxYCoord = grid[0].length - 1;

        // Electrode at bottom right corner of safe area
        int x2Safe = Math.min(electrodeCoords[2] + 1, maxXCoord);
        int y2Safe = Math.min(electrodeCoords[3] + 1, maxYCoord);

        return getElectrodes(x1Safe, y1Safe, x2Safe, y2Safe);
    }


    // Returns an array the electrode coordinates in the form [x1, y1, x2, y2]
    // Array approach is chosen since it minimizes the overhead when compared to creating an object
    private int[] convertAbsDropletPosToElectrodeCoords(Droplet droplet) {
        int dropX = droplet.getPositionX();
        int dropY = droplet.getPositionY();
        int dropDia = droplet.getDiameter();

        // Maximum coordinates for x and y in the grid
        int maxXCoord = grid.length - 1;
        int maxYCoord = grid[0].length - 1;

        // Electrode at top left corner of droplet - ensure no out of bounds
        int x1 = Math.max(0, dropX / electrodeSizeX);
        int y1 = Math.max(0, dropY / electrodeSizeY);

        // Electrode at bottom right corner of droplet - ensure no out of bounds
        int x2 = Math.min(maxXCoord, (dropX + dropDia) / electrodeSizeX);
        int y2 = Math.min(maxYCoord, (dropY + dropDia) / electrodeSizeY);
        // This also handles the special case where x/y pos of the droplet is equal to the grid size x/y.
        // Example could be 60/20 = 3, but we would only have 3 electrodes with index 0-2.

        return new int[]{x1, y1, x2, y2};
    }


    // Return list of electrodes in a rectangular area
    // (x1,y1) is the top left corner and (x2,y2) is the bottom right corner
    private List<Electrode> getElectrodes(int x1, int y1, int x2, int y2) {
        List<Electrode> dropletElectrodes = new ArrayList<>();

        for (int y = y1; y <= y2; y++) {
            for (int x = x1; x <= x2; x++) {
                dropletElectrodes.add(grid[x][y]);
            }
        }

        return dropletElectrodes;
    }
}