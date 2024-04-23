package com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform;

import com.digitalmicrofluidicbiochips.bachelorProject.executor.path_finding.DropletMove;
import com.digitalmicrofluidicbiochips.bachelorProject.utils.DmfPlatformUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * A droplet is a small liquid volume that can be moved around on the DMF platform.
 * <br>
 * Due to the top glass of the Biochip the droplets can be thought of as having a cylindrical shape.
 * Therefore, the diameter is calculated with the formula for calculating the diameter of a cylinder from volume and height.
 */
@Getter
public class Droplet {

    // A unique integer ID identifying the element
    private final String ID;

    // The X coordinate of the top-left corner of the droplet.
    @Setter
    private int positionX;

    // The Y coordinate of the top-left corner of the droplet.
    @Setter
    private int positionY;

    // The Volume of the droplet.
    private double volume;

    // Calculated from the volume of the droplet, assuming cylindrical size of the droplet.
    private int diameter;

    private final double heightToGlass = 0.0005;

    @Setter
    private DropletStatus status;

    @Setter
    DropletMove dropletMove;

    public Droplet(
            String ID,
            int positionX,
            int positionY,
            double volume
    ) {
        this.ID = ID;
        this.positionX = positionX;
        this.positionY = positionY;
        this.volume = volume;
        this.diameter = getDiameterFromVol();
        setStatus(DropletStatus.NOT_CREATED);
        setDropletMove(DropletMove.NONE);
    }

    // We need a custom method for setting volume as this will also affect the droplets diameter
    public void setVolume(double volume) {
        this.volume = volume;
        this.diameter = getDiameterFromVol();
    }

    public void moveDropletInDirection(DropletMove dropletMove) {
        switch (dropletMove) {
            case UP -> setPositionY(getPositionY() - 1);
            case DOWN -> setPositionY(getPositionY() + 1);
            case LEFT -> setPositionX(getPositionX() - 1);
            case RIGHT -> setPositionX(getPositionX() + 1);
        }
    }

    public static boolean dropletMoveChangesDropletPosition(DropletMove dropletMove) {
        return dropletMove == DropletMove.UP ||
                dropletMove == DropletMove.LEFT ||
                dropletMove == DropletMove.DOWN ||
                dropletMove == DropletMove.RIGHT;
    }


    private int getDiameterFromVol() {
        double volumeInCubicMeters = volume * Math.pow(10, -9); // Convert volume from microliters to cubic meters

        double diameterInMeters = 2 * Math.sqrt((volumeInCubicMeters / (Math.PI * heightToGlass))); // Calculate diameter
        double diameterInCorrectUnit = diameterInMeters * 10000; // Convert from m to 1/10th of a mm

        return (int) Math.ceil(diameterInCorrectUnit); // Return rounded (up) diameter
    }


    public List<Electrode> getElectrodesToEnableDuringDropletMove(ElectrodeGrid electrodeGrid) {
        int electrodeWidth = electrodeGrid.getElectrodeSizeOfElectrodeInGrid();
        int diameterInElectrodes = DmfPlatformUtils.electrodeSpanRequiredToMoveDroplet(this, electrodeWidth);
        List<Electrode> electrodes = new ArrayList<>();
        if(dropletMove == DropletMove.UP) {
            for (int i = 0; i < diameterInElectrodes ; i++) {
                electrodes.add(electrodeGrid.getElectrode(positionX + i, positionY - 1));
            }
        }
        if(dropletMove == DropletMove.DOWN) {
            for (int i = 0; i < diameterInElectrodes; i++) {
                electrodes.add(electrodeGrid.getElectrode(positionX + i, positionY + diameterInElectrodes));
            }
        }
        if(dropletMove == DropletMove.LEFT) {
            for (int i = 0; i < diameterInElectrodes; i++) {
                electrodes.add(electrodeGrid.getElectrode(positionX - 1, positionY + i));
            }
        }
        if(dropletMove == DropletMove.RIGHT) {
            for (int i = 0; i < diameterInElectrodes; i++) {
                electrodes.add(electrodeGrid.getElectrode(positionX + diameterInElectrodes, positionY + i));
            }
        }

        // Filter out electrodes that are null, and only return electrodes that are currently off.
        return electrodes.stream().filter(o -> o != null && o.getStatus() == 0).toList();
    }

    public List<Electrode> getElectrodesToDisableDuringDropletMove(ElectrodeGrid electrodeGrid) {
        int electrodeWidth = electrodeGrid.getElectrodeSizeOfElectrodeInGrid();
        int diameterInElectrodes = DmfPlatformUtils.electrodeSpanRequiredToMoveDroplet(this, electrodeWidth);

        List<Electrode> electrodes = new ArrayList<>();
        if(dropletMove == DropletMove.UP) {
            for (int i = 0; i < diameterInElectrodes; i++) {
                electrodes.add(electrodeGrid.getElectrode(positionX + i, positionY + diameterInElectrodes - 1));
            }
        }
        if(dropletMove == DropletMove.DOWN) {
            for (int i = 0; i < diameterInElectrodes; i++) {
                electrodes.add(electrodeGrid.getElectrode(positionX + i, positionY));
            }
        }
        if(dropletMove == DropletMove.LEFT) {
            for (int i = 0; i < diameterInElectrodes; i++) {
                electrodes.add(electrodeGrid.getElectrode(positionX + diameterInElectrodes - 1, positionY + i));
            }
        }
        if(dropletMove == DropletMove.RIGHT) {
            for (int i = 0; i < diameterInElectrodes; i++) {
                electrodes.add(electrodeGrid.getElectrode(positionX, positionY + i));
            }
        }

        // Filter out electrodes that are null, and only return electrodes that are currently active.
        return electrodes.stream().filter(o -> o != null && o.getStatus() >= 0).toList();
    }

    /**
     * Returns the area that the active droplet can't access due to the obstacle droplet.
     * @param electrodeGrid The electrode grid.
     * @param obstacleDroplet The obstacle droplet.
     * @return The area of the grid, that the active droplet can't access due to the obstacle droplet.
     */
    public GridArea getDropletSafeArea(ElectrodeGrid electrodeGrid, Droplet obstacleDroplet) {
        // Diameter of droplet rounded up to nearest electrode.
        int electrodeWidth = electrodeGrid.getElectrodeSizeOfElectrodeInGrid();
        int thisDropletDiameter = DmfPlatformUtils.dropletDiameterInElectrodesCeil(this, electrodeWidth);
        int obstacleDropletDiameter = DmfPlatformUtils.dropletDiameterInElectrodesCeil(obstacleDroplet, electrodeWidth);

        // Electrode at top left corner of droplet + padding and safe area determined by the active droplet's size - ensure no out of bounds
        int x1 = Math.max(obstacleDroplet.getPositionX() - thisDropletDiameter, 0);
        int y1 = Math.max(obstacleDroplet.getPositionY() - thisDropletDiameter, 0);

        // Maximum coordinates for x and y in the grid
        int maxX = electrodeGrid.getXSize() - 1;
        int maxY = electrodeGrid.getYSize() - 1;

        // Electrode at bottom right corner of droplet + safe area - ensure no out of bounds
        int x2 = Math.min(maxX, (obstacleDroplet.getPositionX() + obstacleDropletDiameter));
        int y2 = Math.min(maxY, (obstacleDroplet.getPositionY() + obstacleDropletDiameter));

        GridArea gridArea = new GridArea(x1, y1, x2, y2);
        return extendedGridAreaInDropletMoveDirection(obstacleDroplet, gridArea);
    }

    /**
     * Return the area (electrodes) beneath the droplet, that is currently occupied by the droplet,
     * and is used to move the droplet around. The width of this span is often less than the diameter of the droplet.
     * @param electrodeGrid
     * @return
     */
    public GridArea getDropletElectrodeArea(ElectrodeGrid electrodeGrid) {
        // Diameter of droplet rounded up to nearest electrode.
        int electrodeWidth = electrodeGrid.getElectrodeSizeOfElectrodeInGrid();
        int electrodeSpan = DmfPlatformUtils.electrodeSpanRequiredToMoveDroplet(this, electrodeWidth);

        // Electrode at top left corner of droplet + padding and safe area determined by the active droplet's size - ensure no out of bounds
        int x1 = Math.max(getPositionX(), 0);
        int y1 = Math.max(getPositionY(), 0);

        // Maximum coordinates for x and y in the grid
        int maxX = electrodeGrid.getXSize() - 1;
        int maxY = electrodeGrid.getYSize() - 1;

        // Electrode at bottom right corner of droplet + safe area - ensure no out of bounds
        int x2 = Math.min(maxX, getPositionX() + electrodeSpan - 1);
        int y2 = Math.min(maxY, getPositionY() + electrodeSpan - 1);

        GridArea gridArea = new GridArea(x1, y1, x2, y2);
        return extendedGridAreaInDropletMoveDirection(this, gridArea);
    }

    private static GridArea extendedGridAreaInDropletMoveDirection(Droplet movingDroplet, GridArea gridArea) {
        switch (movingDroplet.getDropletMove()) {
            case DOWN -> {
                return new GridArea(gridArea.getX1(), gridArea.getY1(), gridArea.getX2(), gridArea.getY2() + 1);
            }
            case UP -> {
                return new GridArea(gridArea.getX1(), gridArea.getY1() - 1, gridArea.getX2(), gridArea.getY2());
            }
            case RIGHT -> {
                return new GridArea(gridArea.getX1(), gridArea.getY1(), gridArea.getX2() + 1, gridArea.getY2());
            }
            case LEFT -> {
                return new GridArea(gridArea.getX1() - 1, gridArea.getY1(), gridArea.getX2(), gridArea.getY2() + 1);
            }
        }
        return gridArea;
    }




}