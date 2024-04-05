package com.digitalmicrofluidicbiochips.bachelorProject.reader.mappers.json.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.reader.mappers.generic.actions.IActionMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations.RepeatAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.json.model.actions.JsonRepeatAction;

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
    public void resolveReferences(JsonRepeatAction dtoModel, Map<String, RepeatAction> internalModelMap, Map<String, Droplet> dropletMap) {
        RepeatAction repeatAction = internalModelMap.get(dtoModel.getId());

        repeatAction.setNextRepeatAction(internalModelMap.get(dtoModel.getRepeatNextActionId()));
        repeatAction.setNextExitAction(internalModelMap.get(dtoModel.getExitNextActionId()));
    }
}
