package com.digitalmicrofluidicbiochips.bachelorProject.mappers.json.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.mappers.IActionMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.StoreAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.actions.JsonStoreAction;

import java.util.HashMap;

public class JsonStoreActionMapper implements IActionMapper<JsonStoreAction, StoreAction> {
@Override
    public StoreAction mapToInternalModel(JsonStoreAction dtoModel) {
        return new StoreAction(
                dtoModel.getId(),
                dtoModel.getDropletId(),
                dtoModel.getPosX(),
                dtoModel.getPosY(),
                dtoModel.getTime()
        );
    }

    @Override
    public JsonStoreAction mapToDtoModel(StoreAction internalModel) {
        return new JsonStoreAction(
                internalModel.getId(),
                internalModel.getDropletId(),
                internalModel.getPosX(),
                internalModel.getPosY(),
                internalModel.getTime(),
                internalModel.getNextAction().getId()
        );
    }

    @Override
    public void resolveReferences(JsonStoreAction dtoModel, HashMap<String, StoreAction> internalModelMap) {

    }

}
