package com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform;

import lombok.Getter;
import lombok.Setter;

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

    @Setter
    private DropletStatus status;

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
        this.status = DropletStatus.NOT_CREATED;
    }

    // We need a custom method for setting volume as this will also affect the droplets diameter
    public void setVolume(double volume) {
        this.volume = volume;
        this.diameter = getDiameterFromVol();
    }

    // TODO: From Wenje's report we get that the height is about 500 micrometers (converted to meters for the calculation).
    // TODO: Wenje's code for calculating droplet: https://github.com/gggdttt/BioGo/blob/main/Executor/Model/Droplet.cs
    // Height to glass (in meters)
    private final double heightToGlass = 0.0005;

    private int getDiameterFromVol() {
        double volumeInCubicMeters = volume * Math.pow(10, -9); // Convert volume from microliters to cubic meters

        double diameterInMeters = 2 * Math.sqrt((volumeInCubicMeters / (Math.PI * heightToGlass))); // Calculate diameter
        double diameterInCorrectUnit = diameterInMeters * 10000; // Convert from m to 1/10th of a mm

        return (int) Math.ceil(diameterInCorrectUnit); // Return rounded (up) diameter
    }
}