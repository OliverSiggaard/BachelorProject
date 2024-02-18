package com.digitalmicrofluidicbiochips.bachelorProject.mappers.generic.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.io.generic.DtoActionBase;

import java.util.HashMap;

public interface IActionMapper<T extends DtoActionBase, U extends ActionBase> {
    U mapToInternalModel(T dtoModel);

    // Currently not used. Implemented for JSON, but the internal models is not mapped to a mapper in the Mapper Factory.
    // If anything, this method should probably be moved to a separate interface.
    T mapToDtoModel(U internalModel);

    /**
     * Resolves references to "next" objects in the internal model. This cant be done on initialization, as the next
     * action-object might not have been created yet. (next actions is saved as an ID in the DTO model / file)
     * @param dtoModel DtoModel for which we want to resolve the references for the respective internal model.
     * @param internalModelMap Map of all internal models, where the key is the ID of the action.
     */
    void resolveReferences(T dtoModel, HashMap<String, U> internalModelMap);
}
