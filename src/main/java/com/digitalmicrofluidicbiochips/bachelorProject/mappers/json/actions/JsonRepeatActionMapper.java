package com.digitalmicrofluidicbiochips.bachelorProject.mappers.json.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.mappers.IActionMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.RepeatAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.actions.JsonRepeatAction;

import java.util.HashMap;

public class JsonRepeatActionMapper implements IActionMapper<JsonRepeatAction, RepeatAction> {

    @Override
    public RepeatAction mapToInternalModel(JsonRepeatAction dtoModel) {
        return new RepeatAction(
                dtoModel.getId(),
                dtoModel.getRepeatCount()
        );
    }

    @Override
    public JsonRepeatAction mapToDtoModel(RepeatAction internalModel) {
        return new JsonRepeatAction(
                internalModel.getId(),
                internalModel.getTimes(),
                internalModel.getNextRepeatAction().getId(),
                internalModel.getNextExitAction().getId()
        );
    }

    @Override
    public void resolveReferences(JsonRepeatAction dtoModel, HashMap<String, RepeatAction> internalModelMap) {

    }
}
