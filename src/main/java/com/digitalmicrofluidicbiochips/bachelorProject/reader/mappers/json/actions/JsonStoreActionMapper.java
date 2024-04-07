package com.digitalmicrofluidicbiochips.bachelorProject.reader.mappers.json.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations.ActionQueue;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations.MoveAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations.OutputAction;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.mappers.generic.actions.IActionMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations.StoreAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.json.model.actions.JsonStoreAction;

import java.util.Arrays;
import java.util.Map;

public class JsonStoreActionMapper implements IActionMapper<JsonStoreAction, ActionQueue> {
@Override
    public ActionQueue mapToInternalModel(JsonStoreAction dtoModel) {

    MoveAction moveAction = new MoveAction(
            dtoModel.getId(),
            dtoModel.getPosX(),
            dtoModel.getPosY()
    );
    StoreAction storeAction = new StoreAction(
            dtoModel.getId(),
            dtoModel.getPosX(),
            dtoModel.getPosY(),
            dtoModel.getTime()
    );

    return new ActionQueue(dtoModel.getId(), Arrays.asList(moveAction, storeAction));
}

    @Override
    public void resolveReferences(JsonStoreAction dtoModel, Map<String, ActionQueue> internalModelMap, Map<String, Droplet> dropletMap) {
        ActionQueue actionQueue = internalModelMap.get(dtoModel.getId());
        actionQueue.setNextAction(internalModelMap.get(dtoModel.getNextActionId()));

        assert actionQueue.getActions().size() == 2;
        MoveAction moveAction = (MoveAction) actionQueue.getActions().get(0);
        StoreAction storeAction = (StoreAction) actionQueue.getActions().get(1);

        moveAction.setDroplet(dropletMap.get(dtoModel.getDropletId()));
        storeAction.setDroplet(dropletMap.get(dtoModel.getDropletId()));

    }

}
