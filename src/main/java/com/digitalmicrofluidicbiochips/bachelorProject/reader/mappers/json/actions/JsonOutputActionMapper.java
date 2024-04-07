package com.digitalmicrofluidicbiochips.bachelorProject.reader.mappers.json.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations.ActionQueue;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations.MoveAction;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.mappers.generic.actions.IActionMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations.OutputAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.json.model.actions.JsonOutputAction;

import java.util.Arrays;
import java.util.Map;

public class JsonOutputActionMapper implements IActionMapper<JsonOutputAction, ActionQueue> {

    @Override
    public ActionQueue mapToInternalModel(JsonOutputAction dtoModel) {
        MoveAction moveAction = new MoveAction(
                dtoModel.getId(),
                dtoModel.getPosX(),
                dtoModel.getPosY()
        );
        OutputAction outputAction = new OutputAction(
                dtoModel.getId(),
                dtoModel.getPosX(),
                dtoModel.getPosY()
        );

        return new ActionQueue(dtoModel.getId(), Arrays.asList(moveAction, outputAction));
    }

    @Override
    public void resolveReferences(JsonOutputAction dtoModel, Map<String, ActionQueue> internalModelMap, Map<String, Droplet> dropletMap) {
        ActionQueue actionQueue = internalModelMap.get(dtoModel.getId());
        actionQueue.setNextAction(internalModelMap.get(dtoModel.getNextActionId()));

        assert actionQueue.getActions().size() == 2;
        MoveAction moveAction = (MoveAction) actionQueue.getActions().get(0);
        OutputAction outputAction = (OutputAction) actionQueue.getActions().get(1);

        moveAction.setDroplet(dropletMap.get(dtoModel.getDropletId()));
        outputAction.setDroplet(dropletMap.get(dtoModel.getDropletId()));
    }
}
