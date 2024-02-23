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
    private final int ID;

    // The name of the substance in the droplet
    private final String substance_name;

    // The X coordinate of the top-left corner of the droplet.
    @Setter
    private int positionX;

    // The Y coordinate of the top-left corner of the droplet.
    @Setter
    private int positionY;

    // The Volume of the droplet.
    private double volume;

    // Calculated from the volume of the droplet, assuming cylindrical size of the droplet.
    private double diameter;

    public Droplet(
            int ID,
            String substance_name,
            int positionX,
            int positionY,
            double volume
    ) {
        this.ID = ID;
        this.substance_name = substance_name;
        this.positionX = positionX;
        this.positionY = positionY;
        this.volume = volume;
        this.diameter = getDiameterFromVolume();
    }

    // We need a custom method for setting volume as this will also affect the droplets diameter
    public void setVolume(double volume) {
        this.volume = volume;
        this.diameter = getDiameterFromVolume();
    }

    // TODO: From Wenje's report we get that the height is about 500 micrometers (converted to meters for the calculation).
    // TODO: Wenje's code for calculating droplet: https://github.com/gggdttt/BioGo/blob/main/Executor/Model/Droplet.cs
    // Height to glass
    private final double heightToGlass = 0.0005;

    private double getDiameterFromVolume() {
        double volumeInCubicMeters = volume / 1000000; // Convert volume from milliliter to cubic meters for calculation
        return 2 * Math.sqrt((volumeInCubicMeters / (Math.PI * heightToGlass)));
    }
}