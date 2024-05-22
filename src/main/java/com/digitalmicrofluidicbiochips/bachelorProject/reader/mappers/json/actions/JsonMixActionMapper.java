package com.digitalmicrofluidicbiochips.bachelorProject.reader.mappers.json.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations.ActionQueue;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations.MoveAction;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.mappers.generic.actions.IActionMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.json.model.actions.JsonMixAction;

import java.util.Arrays;
import java.util.Map;

public class JsonMixActionMapper implements IActionMapper<JsonMixAction, ActionQueue> {

    @Override
    public ActionQueue mapToInternalModel(JsonMixAction dtoModel) {
        //Move to the first position
        MoveAction moveAction1 = new MoveAction(dtoModel.getId(), dtoModel.getPosX(), dtoModel.getPosY());

        //Do a lap in the anti-clockwise direction
        MoveAction moveAction2 = new MoveAction(dtoModel.getId(), dtoModel.getPosX() + dtoModel.getSizeX(), dtoModel.getPosY());
        MoveAction moveAction3 = new MoveAction(dtoModel.getId(), dtoModel.getPosX() + dtoModel.getSizeX(), dtoModel.getPosY() + dtoModel.getSizeY());
        MoveAction moveAction4 = new MoveAction(dtoModel.getId(), dtoModel.getPosX(), dtoModel.getPosY() + dtoModel.getSizeY());
        MoveAction moveAction5 = new MoveAction(dtoModel.getId(), dtoModel.getPosX(), dtoModel.getPosY());

        return new ActionQueue(
                dtoModel.getId(),
                Arrays.asList(moveAction1, moveAction2, moveAction3, moveAction4, moveAction5));
    }

    @Override
    public void resolveReferences(JsonMixAction dtoModel, Map<String, ActionQueue> internalModelMap, Map<String, Droplet> dropletMap) {
        ActionQueue actionQueue = internalModelMap.get(dtoModel.getId());

        actionQueue.setNextAction(internalModelMap.get(dtoModel.getNextActionId()));

        actionQueue.getActions().forEach(action -> {
            if(action instanceof MoveAction moveAction) {
                moveAction.setDroplet(dropletMap.get(dtoModel.getDropletId()));
            }
        });
    }
}
