package com.digitalmicrofluidicbiochips.bachelorProject.reader.model;

import lombok.Getter;

/**
 * Simple DTO class for the generic program action.
 *
 * This class is implemented, to avoid coupling of the IActionMapper interface with the specific import/export classes.
 */
@Getter
public class DtoActionBase {
    protected String id;

    /**
     * @Deprecated This constructor was initially used by the Json Actions.
     * We moved to simply relying on the class name for the action type.
     */
    @Deprecated
    public DtoActionBase(String id, String actionType) {
        this.id = id;
    }

    public DtoActionBase(String id) {
        this.id = id;
    }
}
