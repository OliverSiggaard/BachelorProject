package com.digitalmicrofluidicbiochips.bachelorProject.errors;

import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;

public enum DmfExceptionMessage {

    UNKNOWN_ERROR_MESSAGE("A non-recoverable error occurred while compiling the program."),
    ERROR_PARSING_PROGRAM("An error occurred while attempting to map the provided program to the internal model in the backend."),
    DROPLET_NOT_DEFINED_ON_ACTION("Unknown or no droplet(s) was assigned to an action in the program."),
    STORE_ACTION_INVALID_TIME("A non-positive time was assigned to a Store action in the program."),
    INPUT_ACTION_INVALID_VOLUME("A non-positive volume was assigned to an Input action in the program."),
    POSITION_OUT_OF_BOUND("An action was attempted at position (%posX%, %posY%), which is outside the bounds of the platform. The platform has dimensions (%posXMax%, %posYMax%). Remember that positions are zero-indexed."),
    DROPLET_PRODUCED_BY_MULTIPLE_ACTIONS("Droplet %dropletID% is produced by multiple actions"),
    DROPLET_USED_BEFORE_PRODUCED_BY_ACTION("Droplet %dropletID% was attempted to be used before being produced by an action");

    private final String message;
    DmfExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getMessage(Droplet droplet) {
        return message.replace("%dropletID%", droplet.getID());
    }

    public String getMessage(int posX, int posY, int maxX, int maxY) {
        return message.replace("%posX%", String.valueOf(posX))
                      .replace("%posY%", String.valueOf(posY))
                      .replace("%posXMax%", String.valueOf(maxX))
                      .replace("%posYMax%", String.valueOf(maxY));
    }
}
