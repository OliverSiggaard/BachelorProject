package com.digitalmicrofluidicbiochips.bachelorProject.mappers.json.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.mappers.IActionMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.MoveAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.actions.JsonMoveAction;

import java.util.HashMap;

public class JsonMoveActionMapper implements IActionMapper<JsonMoveAction, MoveAction> {
    @Override
    public MoveAction mapToInternalModel(JsonMoveAction dtoModel) {
        return new MoveAction(
                dtoModel.getId(),
                dtoModel.getDropletId(),
                dtoModel.getDestX(),
                dtoModel.getDestY()
        );
    }

    @Override
    public JsonMoveAction mapToDtoModel(MoveAction internalModel) {
        return new JsonMoveAction(
                internalModel.id,
                internalModel.getDropletId(),
                internalModel.getDestX(),
                internalModel.getDestY(),
                internalModel.getNextAction().getId()
        );
    }

    @Override
    public void resolveReferences(JsonMoveAction dtoModel, HashMap<String, MoveAction> internalModelMap) {

    }
}
