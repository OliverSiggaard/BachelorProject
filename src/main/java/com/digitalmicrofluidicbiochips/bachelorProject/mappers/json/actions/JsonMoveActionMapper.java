package com.digitalmicrofluidicbiochips.bachelorProject.mappers.json.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.mappers.generic.actions.IActionMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.MoveAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.actions.JsonMoveAction;

import java.util.HashMap;

public class JsonMoveActionMapper implements IActionMapper<JsonMoveAction, MoveAction> {
    @Override
    public MoveAction mapToInternalModel(JsonMoveAction dtoModel) {
        return new MoveAction(
                dtoModel.getId(),
                dtoModel.getDropletId(),
                dtoModel.getPosX(),
                dtoModel.getPosY()
        );
    }

    @Override
    public JsonMoveAction mapToDtoModel(MoveAction internalModel) {
        return new JsonMoveAction(
                internalModel.getId(),
                internalModel.getDropletId(),
                internalModel.getPosX(),
                internalModel.getPosY(),
                internalModel.getNextAction().getId()
        );
    }

    @Override
    public void resolveReferences(JsonMoveAction dtoModel, HashMap<String, MoveAction> internalModelMap) {
        MoveAction moveAction = internalModelMap.get(dtoModel.getId());
        moveAction.setNextAction(internalModelMap.get(dtoModel.getNextActionId()));
    }
}
