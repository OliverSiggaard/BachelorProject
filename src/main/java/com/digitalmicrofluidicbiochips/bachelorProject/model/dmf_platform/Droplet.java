package com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform;

import lombok.Getter;
import lombok.Setter;

/**
 * A droplet is a small liquid volume that can be moved around on the DMF platform.
 * <br>
 * The droplet is, in this project, assumed to be circular,
 * meaning the volume can be used to calculate the size (radius).
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
    // From this, the radius, and thus the size can be calculated (given the assumption of a circular droplet)
    @Setter
    private double volume;

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
    }

    //Take inspiration from Wenje's code to calculate the radius of the droplet.
    //Calculate radius: https://github.com/gggdttt/BioGo/blob/main/Executor/Model/Droplet.cs



}
