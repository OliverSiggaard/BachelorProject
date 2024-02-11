package com.digitalmicrofluidicbiochips.bachelorProject.mappers;

import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.DmfPlatformState;

import java.util.List;

/**
 * This interface is responsible for mapping all the external objects to their internal representations.
 *
 * This includes actions, and relevant information about the DMF platform.
 */
public interface IFileToInternalMapper {

    /**
     * This method is responsible for mapping the actions from the external representation to the internal representation.
     */
    public List<ActionBase> getActions();

    public DmfPlatformState getPlatformInformation();
}
