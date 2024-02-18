package com.digitalmicrofluidicbiochips.bachelorProject.mappers.json.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.mappers.generic.actions.IActionMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.MergeAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.actions.JsonMergeAction;

import java.util.HashMap;

public class JsonMergeActionMapper implements IActionMapper<JsonMergeAction, MergeAction> {
    @Override
    public MergeAction mapToInternalModel(JsonMergeAction dtoModel) {
        return new MergeAction(
                dtoModel.getId(),
                dtoModel.getResultDropletId(),
                dtoModel.getDropletId1(),
                dtoModel.getDropletId2(),
                dtoModel.getDestX(),
                dtoModel.getDestY()
        );
    }

    @Override
    public JsonMergeAction mapToDtoModel(MergeAction internalModel) {
        return new JsonMergeAction(
                internalModel.getId(),
                internalModel.getResultDropletId(),
                internalModel.getDropletId1(),
                internalModel.getDropletId2(),
                internalModel.getDestX(),
                internalModel.getDestY(),
                internalModel.getNextAction().getId()
        );
    }

    @Override
    public void resolveReferences(JsonMergeAction dtoModel, HashMap<String, MergeAction> internalModelMap) {

    }
}
