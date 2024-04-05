package com.digitalmicrofluidicbiochips.bachelorProject.reader.mappers.json.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.reader.mappers.generic.actions.IActionMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations.SplitAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.json.model.actions.JsonSplitAction;

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
    public void resolveReferences(JsonSplitAction dtoModel, Map<String, SplitAction> internalModelMap, Map<String, Droplet> dropletMap) {
        SplitAction splitAction = internalModelMap.get(dtoModel.getId());

        splitAction.setNextAction(internalModelMap.get(dtoModel.getNextActionId()));

        splitAction.setOriginDroplet(dropletMap.get(dtoModel.getOriginDropletId()));
        splitAction.setResultDroplet1(dropletMap.get(dtoModel.getResultDropletId1()));
        splitAction.setResultDroplet2(dropletMap.get(dtoModel.getResultDropletId2()));
    }
}
