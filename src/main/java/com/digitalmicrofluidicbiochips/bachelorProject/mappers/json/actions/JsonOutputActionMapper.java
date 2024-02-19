package com.digitalmicrofluidicbiochips.bachelorProject.mappers.json.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.mappers.generic.actions.IActionMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.OutputAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.actions.JsonOutputAction;

import java.util.HashMap;

public class JsonOutputActionMapper implements IActionMapper<JsonOutputAction, OutputAction> {

    @Override
    public OutputAction mapToInternalModel(JsonOutputAction dtoModel) {
        return new OutputAction(
                dtoModel.getId(),
                dtoModel.getDropletId(),
                dtoModel.getPosX(),
                dtoModel.getPosY()
        );
    }

    @Override
    public JsonOutputAction mapToDtoModel(OutputAction internalModel) {
        return new JsonOutputAction(
                internalModel.getId(),
                internalModel.getDropletId(),
                internalModel.getPosX(),
                internalModel.getPosY(),
                internalModel.getNextAction().getId()
        );
    }

    @Override
    public void resolveReferences(JsonOutputAction dtoModel, HashMap<String, OutputAction> internalModelMap) {
        OutputAction outputAction = internalModelMap.get(dtoModel.getId());
        outputAction.setNextAction(internalModelMap.get(dtoModel.getNextActionId()));
    }
}
