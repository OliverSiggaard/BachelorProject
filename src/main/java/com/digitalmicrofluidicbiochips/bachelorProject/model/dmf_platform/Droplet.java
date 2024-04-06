package com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform;

import com.digitalmicrofluidicbiochips.bachelorProject.executor.path_finding.DropletMove;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
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


    public List<Point> getCoordinatesToEnableBeforeMove() {
        List<Point> electrodePositions = new ArrayList<>();
        int diameterInElectrodes = (int) Math.ceil((double)diameter / 20);
        if(dropletMove == DropletMove.UP) {
            for (int i = 0; i < diameterInElectrodes ; i++) {
                electrodePositions.add(new Point(positionX + i, positionY - 1));
            }
        }
        if(dropletMove == DropletMove.DOWN) {
            for (int i = 0; i < diameterInElectrodes; i++) {
                electrodePositions.add(new Point(positionX + i, positionY + diameterInElectrodes));
            }
        }
        if(dropletMove == DropletMove.LEFT) {
            for (int i = 0; i < diameterInElectrodes; i++) {
                electrodePositions.add(new Point(positionX - 1, positionY + i));
            }
        }
        if(dropletMove == DropletMove.RIGHT) {
            for (int i = 0; i < diameterInElectrodes; i++) {
                electrodePositions.add(new Point(positionX + diameterInElectrodes, positionY + i));
            }
        }
        return electrodePositions;
    }

    public List<Point> getCoordinatesToDisableAfterMove() {
        List<Point> electrodePositions = new ArrayList<>();
        int diameterInElectrodes = (int) Math.ceil((double)diameter / 20);
        if(dropletMove == DropletMove.UP) {
            for (int i = 0; i < diameterInElectrodes; i++) {
                electrodePositions.add(new Point(positionX + i, positionY + diameterInElectrodes - 1));
            }
        }
        if(dropletMove == DropletMove.DOWN) {
            for (int i = 0; i < diameterInElectrodes; i++) {
                electrodePositions.add(new Point(positionX + i, positionY));
            }
        }
        if(dropletMove == DropletMove.LEFT) {
            for (int i = 0; i < diameterInElectrodes; i++) {
                electrodePositions.add(new Point(positionX + diameterInElectrodes - 1, positionY + i));
            }
        }
        if(dropletMove == DropletMove.RIGHT) {
            for (int i = 0; i < diameterInElectrodes; i++) {
                electrodePositions.add(new Point(positionX, positionY + i));
            }
        }
        return electrodePositions;
    }

}