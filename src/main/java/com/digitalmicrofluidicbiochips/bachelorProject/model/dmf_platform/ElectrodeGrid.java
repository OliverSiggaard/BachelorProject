package com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ElectrodeGrid implements Cloneable {
    private Electrode[][] grid;

    public ElectrodeGrid(Electrode[][] grid) {
        this.grid = grid;
    }

    public Electrode getElectrode(int x, int y) {
        return grid[x][y];
    }

    public void removeElectrode(int x, int y) {
        grid[x][y] = null;
    }

    @Override
    public ElectrodeGrid clone() {
        Electrode[][] clonedGrid = new Electrode[grid.length][];
        for (int i = 0; i < grid.length; i++) {
            clonedGrid[i] = grid[i].clone(); // Cloning each inner array
        }
        return new ElectrodeGrid(clonedGrid);
    }

    public int getXSize() {
        return grid.length;
    }

    public int getYSize() {
        return grid[0].length;
    }

    public Electrode[][] getGridAs2dArray() {
        return grid;
    }

    public int getElectrodeSizeOfElectrodeInGrid() {
        return getFirstNonNullElectrodeInGrid().getSizeX();
    }

    private Electrode getFirstNonNullElectrodeInGrid() {
        for (Electrode[] electrodes : grid) {
            for (Electrode electrode : electrodes) {
                if (electrode != null) {
                    return electrode;
                }
            }
        }
        throw new IllegalStateException("No electrode found in grid");
    }

}
