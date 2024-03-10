package com.digitalmicrofluidicbiochips.bachelorProject.reader.mappers.json.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.reader.mappers.generic.actions.IActionMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.IfAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.json.model.actions.JsonIfAction;

import java.util.Map;

public class JsonIfActionMapper implements IActionMapper<JsonIfAction, IfAction> {

    @Override
    public IfAction mapToInternalModel(JsonIfAction dtoModel) {
        return new IfAction(
                dtoModel.getId(),
                dtoModel.getCondition()
        );
    }

    @Override
    public JsonIfAction mapToDtoModel(IfAction internalModel) {
        return new JsonIfAction(
                internalModel.getId(),
                internalModel.getCondition(),
                internalModel.getTrueNextAction().getId(),
                internalModel.getFalseNextAction().getId()
        );
    }

    @Override
    public void resolveReferences(JsonIfAction dtoModel, Map<String, IfAction> internalModelMap, Map<String, Droplet> dropletMap) {
        IfAction ifAction = internalModelMap.get(dtoModel.getId());

        ifAction.setTrueNextAction(internalModelMap.get(dtoModel.getTrueNextActionId()));
        ifAction.setFalseNextAction(internalModelMap.get(dtoModel.getFalseNextActionId()));

        // No droplets to resolve on IfAction
    }
}
