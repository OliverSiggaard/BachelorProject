package com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform;

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
        return new ElectrodeGrid(grid.clone());
    }

    public int getXSize() {
        return grid.length;
    }

    public int getYSize() {
        return grid[0].length;
    }

}
