package com.digitalmicrofluidicbiochips.bachelorProject.reader.mappers.json.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.reader.mappers.generic.actions.IActionMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.MixAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.json.model.actions.JsonMixAction;

import java.util.Map;

public class JsonMixActionMapper implements IActionMapper<JsonMixAction, MixAction> {

    @Override
    public MixAction mapToInternalModel(JsonMixAction dtoModel) {
        return new MixAction(
                dtoModel.getId(),
                dtoModel.getPosX(),
                dtoModel.getPosY(),
                dtoModel.getSizeX(),
                dtoModel.getSizeY()
        );
    }

    @Override
    public JsonMixAction mapToDtoModel(MixAction internalModel) {
        return new JsonMixAction(
                internalModel.getId(),
                internalModel.getDroplet().getID(),
                internalModel.getNextAction().getId(),
                internalModel.getPosX(),
                internalModel.getPosY(),
                internalModel.getSizeX(),
                internalModel.getSizeY()
        );
    }

    @Override
    public void resolveReferences(JsonMixAction dtoModel, Map<String, MixAction> internalModelMap, Map<String, Droplet> dropletMap) {
        MixAction mixAction = internalModelMap.get(dtoModel.getId());

        mixAction.setNextAction(internalModelMap.get(dtoModel.getNextActionId()));

        mixAction.setDroplet(dropletMap.get(dtoModel.getDropletId()));
    }
}
