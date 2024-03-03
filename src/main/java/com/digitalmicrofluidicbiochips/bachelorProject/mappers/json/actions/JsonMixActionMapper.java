package com.digitalmicrofluidicbiochips.bachelorProject.mappers.json.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.mappers.generic.actions.IActionMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.MixAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.actions.JsonMixAction;

import java.util.HashMap;
import java.util.Map;

public class JsonMixActionMapper implements IActionMapper<JsonMixAction, MixAction> {

    @Override
    public MixAction mapToInternalModel(JsonMixAction dtoModel) {
        return new MixAction(
                dtoModel.getId()
        );
    }

    @Override
    public JsonMixAction mapToDtoModel(MixAction internalModel) {
        return new JsonMixAction(
                internalModel.getId(),
                internalModel.getDroplet().getID(),
                internalModel.getNextAction().getId()
        );
    }

    @Override
    public void resolveReferences(JsonMixAction dtoModel, Map<String, MixAction> internalModelMap, Map<String, Droplet> dropletMap) {
        MixAction mixAction = internalModelMap.get(dtoModel.getId());

        mixAction.setNextAction(internalModelMap.get(dtoModel.getNextActionId()));

        mixAction.setDroplet(dropletMap.get(dtoModel.getDropletId()));
    }
}
