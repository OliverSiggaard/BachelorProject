package com.digitalmicrofluidicbiochips.bachelorProject.mappers.json.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.mappers.IActionMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.EndAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.actions.JsonEndAction;

import java.util.HashMap;

public class JsonEndActionMapper implements IActionMapper<JsonEndAction, EndAction> {

    @Override
    public EndAction mapToInternalModel(JsonEndAction dtoModel) {
        return new EndAction(
                dtoModel.getId()
                );
    }

    @Override
    public JsonEndAction mapToDtoModel(EndAction internalModel) {
        return new JsonEndAction(
                internalModel.id
        );
    }

    @Override
    public void resolveReferences(JsonEndAction dtoModel, HashMap<String, EndAction> internalModelMap) {

    }
}
