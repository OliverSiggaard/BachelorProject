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
    DROPLET_USED_BEFORE_PRODUCED_BY_ACTION("Droplet %dropletID% was attempted to be used before being produced by an action"),
    ACTION_FIELD_EMPTY("One or more actions in the program have empty fields. All fields must be non-empty."),
    ACTION_FIELD_INTEGER_NOT_INTEGER("One or more actions in the program have non-integer elements in a integer field. All integer fields must be integers."),
    ACTION_FIELD_DOUBLE_NOT_DOUBLE("One or more actions in the program have non-decimal elements in a decimal field. All decimal fields must be decimals."),
    INPUT_ACTION_DROPLET_DIAMETER_SMALLER_THAN_ELECTRODE_SIZE("The droplet %dropletID% is too small to be moved around on the board. Please increase the volume."),
    SPLIT_ACTION_RESULT_DROPLETS_DIAMETER_SMALLER_THAN_ELECTRODE_SIZE("The droplets resulting from splitting %dropletID% is too small to be moved around on the board. Please increase the volume."),
    PROGRAM_GOT_STUCK("The program got stuck. We attempted to resolve the deadlock without success."),
    DROPLET_CONSUMED_BEFORE_USED("Droplet %dropletID% was attempted to be used after being consumed by an action"),
    MIX_INVALID_SIZE("The size of the mix action must be greater than 0, and x and y can not both be 1 (i.e. not mixing)."),;

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
