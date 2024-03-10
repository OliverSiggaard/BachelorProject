package com.digitalmicrofluidicbiochips.bachelorProject.reader.mappers.json.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.reader.mappers.generic.actions.IActionMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.MoveAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.json.model.actions.JsonMoveAction;

import java.util.Map;

public class JsonMoveActionMapper implements IActionMapper<JsonMoveAction, MoveAction> {
    @Override
    public MoveAction mapToInternalModel(JsonMoveAction dtoModel) {
        return new MoveAction(
                dtoModel.getId(),
                dtoModel.getPosX(),
                dtoModel.getPosY()
        );
    }

    @Override
    public JsonMoveAction mapToDtoModel(MoveAction internalModel) {
        return new JsonMoveAction(
                internalModel.getId(),
                internalModel.getDroplet().getID(),
                internalModel.getPosX(),
                internalModel.getPosY(),
                internalModel.getNextAction().getId()
        );
    }

    @Override
    public void resolveReferences(JsonMoveAction dtoModel, Map<String, MoveAction> internalModelMap, Map<String, Droplet> dropletMap) {
        MoveAction moveAction = internalModelMap.get(dtoModel.getId());

        moveAction.setNextAction(internalModelMap.get(dtoModel.getNextActionId()));

        moveAction.setDroplet(dropletMap.get(dtoModel.getDropletId()));
    }
}
