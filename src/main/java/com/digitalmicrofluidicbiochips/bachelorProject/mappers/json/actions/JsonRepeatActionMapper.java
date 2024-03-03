package com.digitalmicrofluidicbiochips.bachelorProject.mappers.json.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.mappers.generic.actions.IActionMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.RepeatAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.actions.JsonRepeatAction;

import java.util.HashMap;
import java.util.Map;

public class JsonRepeatActionMapper implements IActionMapper<JsonRepeatAction, RepeatAction> {

    @Override
    public RepeatAction mapToInternalModel(JsonRepeatAction dtoModel) {
        return new RepeatAction(
                dtoModel.getId(),
                dtoModel.getRepeatCount()
        );
    }

    @Override
    public JsonRepeatAction mapToDtoModel(RepeatAction internalModel) {
        return new JsonRepeatAction(
                internalModel.getId(),
                internalModel.getTimes(),
                internalModel.getNextRepeatAction().getId(),
                internalModel.getNextExitAction().getId()
        );
    }

    @Override
    public void resolveReferences(JsonRepeatAction dtoModel, Map<String, RepeatAction> internalModelMap, Map<String, Droplet> dropletMap) {
        RepeatAction repeatAction = internalModelMap.get(dtoModel.getId());

        repeatAction.setNextRepeatAction(internalModelMap.get(dtoModel.getRepeatNextActionId()));
        repeatAction.setNextExitAction(internalModelMap.get(dtoModel.getExitNextActionId()));
    }
}
