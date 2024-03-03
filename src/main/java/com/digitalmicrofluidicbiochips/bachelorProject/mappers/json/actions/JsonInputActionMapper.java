package com.digitalmicrofluidicbiochips.bachelorProject.mappers.json.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.mappers.generic.actions.IActionMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.InputAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.actions.JsonInputAction;

import java.util.HashMap;
import java.util.Map;

public class JsonInputActionMapper implements IActionMapper<JsonInputAction, InputAction> {

    @Override
    public InputAction mapToInternalModel(JsonInputAction dtoModel) {
        return new InputAction(
                dtoModel.getId(),
                dtoModel.getPosX(),
                dtoModel.getPosY(),
                dtoModel.getVolume()
        );
    }

    @Override
    public JsonInputAction mapToDtoModel(InputAction internalModel) {
        return new JsonInputAction(
                internalModel.getId(),
                internalModel.getDroplet().getID(),
                internalModel.getPosX(),
                internalModel.getPosY(),
                internalModel.getVolume(),
                internalModel.getNextAction().getId()
        );
    }

    @Override
    public void resolveReferences(JsonInputAction dtoModel, Map<String, InputAction> internalModelMap, Map<String, Droplet> dropletMap) {
        InputAction inputAction = internalModelMap.get(dtoModel.getId());

        inputAction.setNextAction(internalModelMap.get(dtoModel.getNextActionId()));

        inputAction.setDroplet(dropletMap.get(dtoModel.getDropletId()));
    }
}
