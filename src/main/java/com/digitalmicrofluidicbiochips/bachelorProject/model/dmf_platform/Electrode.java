package com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform;

import lombok.Getter;

/**
 * This class represents an electrode in the DMF platform.
 * <br>
 * Inspiration for the class was taken from the provided DMFasJSON.pdf file, found in the documentation.
 */

@Getter
public class Electrode {

    // A String identifier for the electrode
    private final String name;

    // A unique integer ID identifying the element
    private final int ID;

    // A unique electrode ID
    private final int electrodeID;

    // The ID of the electrode in the real platform_name (used by SETEL/CLREL)
    private final int driverID;

    // The X coordinate of the top-left corner of the electrode.
    private final int positionX;

    // The Y coordinate of the top-left corner of the electrode.
    private final int positionY;

    // The X size of the electrode for rectangle shape.
    private final int sizeX;

    // The Y size of the electrode for rectangle shape.
    private final int sizeY;

    // The current status of the electrode - 0=off , 1=on - (in the future a number from 0 to 100 shall be supported)
    private final int status;

    public Electrode(
            String name,
            int elementID,
            int electrodeID,
            int driverID,
            int positionX,
            int positionY,
            int sizeX,
            int sizeY,
            int status
    ) {
        this.name = name;
        this.ID = elementID;
        this.electrodeID = electrodeID;
        this.driverID = driverID;
        this.positionX = positionX;
        this.positionY = positionY;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.status = status;
    }

    public String getEnableBioAssemblyCommand() {
        return "SETEL " + ID + ";";
    }

    public String getDisableBioAssemblyCommand() {
        return "CLREL " + ID + ";";
    }
}
