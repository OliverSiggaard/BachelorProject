package com.digitalmicrofluidicbiochips.bachelorProject.mappers.json.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.mappers.generic.actions.IActionMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.InputAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.actions.JsonInputAction;

import java.util.HashMap;

public class JsonInputActionMapper implements IActionMapper<JsonInputAction, InputAction> {

    @Override
    public InputAction mapToInternalModel(JsonInputAction dtoModel) {
        return new InputAction(
                dtoModel.getId(),
                dtoModel.getDropletId(),
                dtoModel.getPosX(),
                dtoModel.getPosY(),
                dtoModel.getVolume()
        );
    }

    @Override
    public JsonInputAction mapToDtoModel(InputAction internalModel) {
        return new JsonInputAction(
                internalModel.getId(),
                internalModel.getDropletId(),
                internalModel.getPosX(),
                internalModel.getPosY(),
                internalModel.getVolume(),
                internalModel.getNextAction().getId()
        );
    }

    @Override
    public void resolveReferences(JsonInputAction dtoModel, HashMap<String, InputAction> internalModelMap) {

    }
}
