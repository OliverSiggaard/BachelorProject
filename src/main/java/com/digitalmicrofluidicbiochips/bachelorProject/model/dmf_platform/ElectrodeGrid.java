package com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform;

import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.ActionTickResult;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.ClearElectrodeCommand;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.IDmfCommand;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.SetElectrodeCommand;

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

    public void removeElectrodes(GridArea gridArea) {
        for (int x = gridArea.getX1(); x <= gridArea.getX2(); x++) {
            for (int y = gridArea.getY1(); y <= gridArea.getY2(); y++) {
                grid[x][y] = null;
            }
        }
    }

    public List<IDmfCommand> getSetElectrodeCommands(GridArea gridArea) {
        List<IDmfCommand> setElectrodeCommands = new ArrayList<>();
        for (int x = gridArea.getX1(); x <= gridArea.getX2(); x++) {
            for (int y = gridArea.getY1(); y <= gridArea.getY2(); y++) {
                setElectrodeCommands.add(new SetElectrodeCommand(getElectrode(x, y)));
            }
        }
        return setElectrodeCommands;
    }

    public List<IDmfCommand> getClearElectrodeCommands(GridArea gridArea) {
        List<IDmfCommand> clearElectrodeCommands = new ArrayList<>();
        for (int x = gridArea.getX1(); x <= gridArea.getX2(); x++) {
            for (int y = gridArea.getY1(); y <= gridArea.getY2(); y++) {
                clearElectrodeCommands.add(new ClearElectrodeCommand(getElectrode(x, y)));
            }
        }
        return clearElectrodeCommands;
    }

    public boolean isAllElectrodesAvailableWithinArea(GridArea gridArea) {
        for (int x = gridArea.getX1(); x <= gridArea.getX2(); x++) {
            for (int y = gridArea.getY1(); y <= gridArea.getY2(); y++) {
                if(getElectrode(x, y) == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < getXSize() && y >= 0 && y < getYSize();
    }

    public void printGrid() {
        for (int y = 0; y < grid[0].length; y++) {
            for (int x = 0; x < grid.length; x++) {
                if (grid[x][y] == null) {
                    System.out.print("0 ");
                } else {
                    System.out.print("1 ");
                }
            }
            System.out.println();
        }
    }
}
