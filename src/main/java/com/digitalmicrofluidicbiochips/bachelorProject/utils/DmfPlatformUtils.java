package com.digitalmicrofluidicbiochips.bachelorProject.utils;

import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Electrode;

public class DmfPlatformUtils {
    /**
     * Get the BioAssembly command to set (turn on) the electrode with the given ID.
     * @return bioAssembly command
     */
    public static String getSetElectrodeCommand(int electrodeID) {
        return "SETELI " + electrodeID + ";";
    }

    /**
     * Get the BioAssembly command to clear (turn off) the electrode with the given ID.
     * @return bioAssembly command
     */
    public static String getClearElectrodeCommand(int electrodeID) {
        return "CLRELI " + electrodeID + ";";
    }

    /**
     * Returns the number of electrodes that are used, to move the droplet in a given direction.
     * This is often less than the diameter of the droplet, as the droplet can (and should) be moved by a smaller
     * span of electrodes, due to the electrodes being square, and the droplet being round.
     * @return number of electrodes required to move the droplet
     */
    public static int electrodeSpanRequiredToMoveDroplet(Droplet droplet, int electrodeWidth) {
        int diameterInElectrodes = droplet.getDiameter() / electrodeWidth; // Always round down.
        return diameterInElectrodes == 0 ? 1 : diameterInElectrodes; // If rounded down to 0, return 1 electrode.
    }



}
