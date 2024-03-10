package com.digitalmicrofluidicbiochips.bachelorProject.reader.mappers.generic.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.model.DtoActionBase;

import java.util.Map;

public interface IActionMapper<T extends DtoActionBase, U extends ActionBase> {
    U mapToInternalModel(T dtoModel);

    // Currently not used. Implemented for JSON, but the internal models is not mapped to a mapper in the Mapper Factory.
    // If anything, this method should probably be moved to a separate interface.
    T mapToDtoModel(U internalModel);

    /**
     * Resolves references for ID strings to objects. This is done for both "next" actions and for droplets.
     * @param dtoModel DtoModel for which we want to resolve the references for the respective internal model.
     * @param internalModelMap Map of all internal models, where the key is the ID of the action.
     * @param dropletMap Map of all droplets, where the key is the ID of the droplet.
     */
    void resolveReferences(T dtoModel, Map<String, U> internalModelMap, Map<String, Droplet> dropletMap);


}
