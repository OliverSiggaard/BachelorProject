package com.digitalmicrofluidicbiochips.bachelorProject.mappers.json.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.mappers.generic.actions.IActionMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.IfAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.actions.JsonIfAction;

import java.util.HashMap;

public class JsonIfActionMapper implements IActionMapper<JsonIfAction, IfAction> {

    @Override
    public IfAction mapToInternalModel(JsonIfAction dtoModel) {
        return new IfAction(
                dtoModel.getId(),
                dtoModel.getCondition()
        );
    }

    @Override
    public JsonIfAction mapToDtoModel(IfAction internalModel) {
        return new JsonIfAction(
                internalModel.getId(),
                internalModel.getCondition(),
                internalModel.getTrueNextAction().getId(),
                internalModel.getFalseNextAction().getId()
        );
    }

    @Override
    public void resolveReferences(JsonIfAction dtoModel, HashMap<String, IfAction> internalModelMap) {

    }
}
