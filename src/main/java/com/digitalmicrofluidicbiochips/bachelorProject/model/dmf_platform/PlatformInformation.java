package com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform;

import lombok.Getter;

import java.util.List;

/**
 * This class is responsible for holding the state of the DMF platform.
 * This includes the size of the platform, its electrodes, actuators, sensors, input/output, etc.
 *
 * Not all fields are implemented, only the ones that are relevant for the project. (added as needed)
 */

public class PlatformInformation {

    @Getter
    private final int sizeX, sizeY;

    @Getter
    private final List<Electrode> electrodes;

    // TODO: Information Actuators?
    // TODO: Information about sensors?
    // TODO: Information about input/output?
    // TODO: Information about height to top glass for calculating droplet diameter?

    public PlatformInformation(int sizeX, int sizeY, List<Electrode> electrodes) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.electrodes = electrodes;
    }
}
