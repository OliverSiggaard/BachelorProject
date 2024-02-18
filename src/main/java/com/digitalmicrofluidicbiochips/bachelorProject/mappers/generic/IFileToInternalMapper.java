package com.digitalmicrofluidicbiochips.bachelorProject.mappers.generic;

import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.PlatformInformation;

import java.util.List;

/**
 * This interface is responsible for mapping all the external objects to their internal representations.
 *
 * This includes actions, and relevant information about the DMF platform.
 */
public interface IFileToInternalMapper {

    public ProgramConfiguration getProgramConfiguration();
}
