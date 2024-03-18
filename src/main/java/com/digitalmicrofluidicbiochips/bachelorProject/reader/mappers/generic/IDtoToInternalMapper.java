package com.digitalmicrofluidicbiochips.bachelorProject.reader.mappers.generic;

import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;

/**
 * This interface is responsible for mapping all the external objects to their internal representations.
 *
 * This includes actions, and relevant information about the DMF platform.
 */
public interface IDtoToInternalMapper {

    public ProgramConfiguration getProgramConfiguration();
}
