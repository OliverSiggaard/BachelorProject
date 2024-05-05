package com.digitalmicrofluidicbiochips.bachelorProject.reader.mappers.json.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.reader.mappers.generic.actions.IActionMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations.InputAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.json.model.actions.JsonInputAction;

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
    public void resolveReferences(JsonInputAction dtoModel, Map<String, InputAction> internalModelMap, Map<String, Droplet> dropletMap) {
        InputAction inputAction = internalModelMap.get(dtoModel.getId());

        inputAction.setNextAction(internalModelMap.get(dtoModel.getNextActionId()));
        
        inputAction.setDroplet(dropletMap.get(dtoModel.getDropletId()));
    }
}
