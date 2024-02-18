package com.digitalmicrofluidicbiochips.bachelorProject.mappers.json.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.mappers.generic.actions.IActionMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.SplitAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.actions.JsonSplitAction;

import java.util.HashMap;

public class JsonSplitActionMapper implements IActionMapper<JsonSplitAction, SplitAction> {

    @Override
    public SplitAction mapToInternalModel(JsonSplitAction dtoModel) {
        return new SplitAction(
                dtoModel.getId(),
                dtoModel.getOriginDroplet(),
                dtoModel.getResultDroplet1(),
                dtoModel.getResultDroplet2(),
                dtoModel.getRatio(),
                dtoModel.getDestX1(),
                dtoModel.getDestY1(),
                dtoModel.getDestX2(),
                dtoModel.getDestY2()
        );
    }

    @Override
    public JsonSplitAction mapToDtoModel(SplitAction internalModel) {
        return new JsonSplitAction(
                internalModel.getId(),
                internalModel.getOriginDroplet(),
                internalModel.getResultDroplet1(),
                internalModel.getResultDroplet2(),
                internalModel.getRatio(),
                internalModel.getDestX1(),
                internalModel.getDestY1(),
                internalModel.getDestX2(),
                internalModel.getDestY2(),
                internalModel.getNextAction().getId()
        );
    }

    @Override
    public void resolveReferences(JsonSplitAction dtoModel, HashMap<String, SplitAction> internalModelMap) {

    }
}
