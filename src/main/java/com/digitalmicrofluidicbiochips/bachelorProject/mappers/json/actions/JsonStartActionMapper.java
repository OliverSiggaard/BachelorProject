package com.digitalmicrofluidicbiochips.bachelorProject.mappers.json.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.mappers.IActionMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.StartAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.actions.JsonStartAction;

import java.util.HashMap;

public class JsonStartActionMapper implements IActionMapper<JsonStartAction, StartAction> {

    @Override
    public StartAction mapToInternalModel(JsonStartAction dtoModel) {
        return new StartAction(dtoModel.getId());
    }

    @Override
    public JsonStartAction mapToDtoModel(StartAction internalModel) {
        return new JsonStartAction(
                internalModel.getId(),
                internalModel.getNextAction().getId()
        );
    }

    @Override
    public void resolveReferences(JsonStartAction dtoModel, HashMap<String, StartAction> internalModelMap) {

    }
}
