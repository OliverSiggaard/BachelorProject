package com.digitalmicrofluidicbiochips.bachelorProject.reader.mappers.json.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.errors.DmfExceptionMessage;
import com.digitalmicrofluidicbiochips.bachelorProject.errors.DmfInputReaderException;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations.ActionQueue;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations.MoveAction;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.mappers.generic.actions.IActionMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.json.model.actions.JsonMixAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class JsonMixActionMapper implements IActionMapper<JsonMixAction, ActionQueue> {

    @Override
    public ActionQueue mapToInternalModel(JsonMixAction dtoModel) {

        int xSize = dtoModel.getSizeX() - 1;
        int ySize = dtoModel.getSizeY() - 1;

        if(xSize < 0 || ySize < 0 || (xSize == 0 && ySize == 0)) {
            throw new DmfInputReaderException(DmfExceptionMessage.MIX_INVALID_SIZE.getMessage());
        }

        ArrayList<ActionBase> moveActions = new ArrayList<>();

        //Move to the first position
        moveActions.add(new MoveAction(dtoModel.getId(), dtoModel.getPosX(), dtoModel.getPosY()));

        //Do a lap in the anti-clockwise direction
        if(xSize > 0) {
            moveActions.add(new MoveAction(dtoModel.getId(), dtoModel.getPosX() + xSize, dtoModel.getPosY()));
        }
        if(ySize > 0) {
            moveActions.add(new MoveAction(dtoModel.getId(), dtoModel.getPosX() + xSize, dtoModel.getPosY() + ySize));
        }
        if(xSize > 0) {
            moveActions.add(new MoveAction(dtoModel.getId(), dtoModel.getPosX(), dtoModel.getPosY() + ySize));
        }
        if(ySize > 0) {
            moveActions.add(new MoveAction(dtoModel.getId(), dtoModel.getPosX(), dtoModel.getPosY()));
        }

        return new ActionQueue(
                dtoModel.getId(),
                moveActions);
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
