package com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform;

public class GridArea {
    private final int x1;
    private final int y1;
    private final int x2;
    private final int y2;

    public GridArea(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    /**
     * Checks if the given coordinates are within the grid area.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return true if the coordinates are within the grid area, false otherwise
     */
    public boolean contains(int x, int y) {
        return x >= x1 && x <= x2 && y >= y1 && y <= y2;
    }

    /**
     * @return the top left x coordinate of the grid area
     */
    public int getX1() {
        return x1;
    }

    /**
     * @return the top left y coordinate of the grid area
     */
    public int getY1() {
        return y1;
    }

    /**
     * @return the bottom right x coordinate of the grid area
     */
    public int getX2() {
        return x2;
    }

    /**
     * @return the bottom right y coordinate of the grid area
     */
    public int getY2() {
        return y2;
    }
}
