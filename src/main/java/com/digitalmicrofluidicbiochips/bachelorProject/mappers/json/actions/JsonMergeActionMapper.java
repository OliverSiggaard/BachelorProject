package com.digitalmicrofluidicbiochips.bachelorProject.mappers.json.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.mappers.generic.actions.IActionMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.MergeAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.actions.JsonMergeAction;

import java.util.HashMap;
import java.util.Map;

public class JsonMergeActionMapper implements IActionMapper<JsonMergeAction, MergeAction> {
    @Override
    public MergeAction mapToInternalModel(JsonMergeAction dtoModel) {
        return new MergeAction(
                dtoModel.getId(),
                dtoModel.getPosX(),
                dtoModel.getPosY()
        );
    }

    @Override
    public JsonMergeAction mapToDtoModel(MergeAction internalModel) {
        return new JsonMergeAction(
                internalModel.getId(),
                internalModel.getResultDroplet().getID(),
                internalModel.getDroplet1().getID(),
                internalModel.getDroplet2().getID(),
                internalModel.getPosX(),
                internalModel.getPosY(),
                internalModel.getNextAction().getId()
        );
    }

    @Override
    public void resolveReferences(JsonMergeAction dtoModel, Map<String, MergeAction> internalModelMap, Map<String, Droplet> dropletMap) {
        MergeAction mergeAction = internalModelMap.get(dtoModel.getId());

        mergeAction.setNextAction(internalModelMap.get(dtoModel.getNextActionId()));

        mergeAction.setDroplet1(dropletMap.get(dtoModel.getDropletId1()));
        mergeAction.setDroplet2(dropletMap.get(dtoModel.getDropletId2()));
        mergeAction.setResultDroplet(dropletMap.get(dtoModel.getResultDropletId()));
    }
}
