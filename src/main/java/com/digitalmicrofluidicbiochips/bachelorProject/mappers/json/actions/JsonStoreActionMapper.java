package com.digitalmicrofluidicbiochips.bachelorProject.mappers.json.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.mappers.generic.actions.IActionMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.StoreAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.actions.JsonStoreAction;

import java.util.HashMap;
import java.util.Map;

public class JsonStoreActionMapper implements IActionMapper<JsonStoreAction, StoreAction> {
@Override
    public StoreAction mapToInternalModel(JsonStoreAction dtoModel) {
        return new StoreAction(
                dtoModel.getId(),
                dtoModel.getPosX(),
                dtoModel.getPosY(),
                dtoModel.getTime()
        );
    }

    @Override
    public JsonStoreAction mapToDtoModel(StoreAction internalModel) {
        return new JsonStoreAction(
                internalModel.getId(),
                internalModel.getDroplet().getID(),
                internalModel.getPosX(),
                internalModel.getPosY(),
                internalModel.getTime(),
                internalModel.getNextAction().getId()
        );
    }

    @Override
    public void resolveReferences(JsonStoreAction dtoModel, Map<String, StoreAction> internalModelMap, Map<String, Droplet> dropletMap) {
        StoreAction storeAction = internalModelMap.get(dtoModel.getId());

        storeAction.setNextAction(internalModelMap.get(dtoModel.getNextActionId()));

        storeAction.setDroplet(dropletMap.get(dtoModel.getDropletId()));
    }

}
