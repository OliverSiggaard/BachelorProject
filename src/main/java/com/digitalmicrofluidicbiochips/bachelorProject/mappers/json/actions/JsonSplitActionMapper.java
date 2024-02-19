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
                dtoModel.getPosX1(),
                dtoModel.getPosY1(),
                dtoModel.getPosX2(),
                dtoModel.getPosY2()
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
                internalModel.getPosX1(),
                internalModel.getPosY1(),
                internalModel.getPosX2(),
                internalModel.getPosY2(),
                internalModel.getNextAction().getId()
        );
    }

    @Override
    public void resolveReferences(JsonSplitAction dtoModel, HashMap<String, SplitAction> internalModelMap) {
        SplitAction splitAction = internalModelMap.get(dtoModel.getId());
        splitAction.setNextAction(internalModelMap.get(dtoModel.getNextActionId()));
    }
}
