package com.digitalmicrofluidicbiochips.bachelorProject.mappers.json.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.mappers.generic.actions.IActionMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.SplitAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.actions.JsonSplitAction;

import java.util.Map;

public class JsonSplitActionMapper implements IActionMapper<JsonSplitAction, SplitAction> {

    @Override
    public SplitAction mapToInternalModel(JsonSplitAction dtoModel) {
        return new SplitAction(
                dtoModel.getId(),
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
                internalModel.getOriginDroplet().getID(),
                internalModel.getResultDroplet1().getID(),
                internalModel.getResultDroplet2().getID(),
                internalModel.getRatio(),
                internalModel.getPosX1(),
                internalModel.getPosY1(),
                internalModel.getPosX2(),
                internalModel.getPosY2(),
                internalModel.getNextAction().getId()
        );
    }

    @Override
    public void resolveReferences(JsonSplitAction dtoModel, Map<String, SplitAction> internalModelMap, Map<String, Droplet> dropletMap) {
        SplitAction splitAction = internalModelMap.get(dtoModel.getId());

        splitAction.setNextAction(internalModelMap.get(dtoModel.getNextActionId()));

        splitAction.setOriginDroplet(dropletMap.get(dtoModel.getOriginDropletId()));
        splitAction.setResultDroplet1(dropletMap.get(dtoModel.getResultDropletId1()));
        splitAction.setResultDroplet2(dropletMap.get(dtoModel.getResultDropletId2()));
    }
}
