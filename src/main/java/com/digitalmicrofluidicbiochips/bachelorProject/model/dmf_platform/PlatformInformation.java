package com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform;

import lombok.Getter;

import java.util.List;

/**
 * This class is responsible for holding the state of the DMF platform.
 * This includes the size of the platform, its electrodes, actuators, sensors, input/output, etc.
 * Not all fields are implemented, only the ones that are relevant for the project. (added as needed)
 */

@Getter
public class PlatformInformation {
    
    private final int sizeX, sizeY; // size is in actual units, not number of electrodes.

    private final List<Electrode> electrodes;


    public PlatformInformation(int sizeX, int sizeY, List<Electrode> electrodes) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.electrodes = electrodes;
    }
}
