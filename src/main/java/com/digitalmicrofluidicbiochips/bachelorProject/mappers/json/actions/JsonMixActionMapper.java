package com.digitalmicrofluidicbiochips.bachelorProject.mappers.json.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.mappers.generic.actions.IActionMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.MixAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.actions.JsonMixAction;

import java.util.HashMap;

public class JsonMixActionMapper implements IActionMapper<JsonMixAction, MixAction> {

    @Override
    public MixAction mapToInternalModel(JsonMixAction dtoModel) {
        return new MixAction(
                dtoModel.getId(),
                dtoModel.getDropletId()
        );
    }

    @Override
    public JsonMixAction mapToDtoModel(MixAction internalModel) {
        return new JsonMixAction(
                internalModel.getId(),
                internalModel.getDropletId(),
                internalModel.getNextAction().getId()
        );
    }

    @Override
    public void resolveReferences(JsonMixAction dtoModel, HashMap<String, MixAction> internalModelMap) {
        MixAction mixAction = internalModelMap.get(dtoModel.getId());
        mixAction.setNextAction(internalModelMap.get(dtoModel.getNextActionId()));
    }
}
