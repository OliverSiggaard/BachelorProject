package com.digitalmicrofluidicbiochips.bachelorProject.mappers.json.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.mappers.generic.actions.IActionMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.EndAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.actions.JsonEndAction;

import java.util.HashMap;
import java.util.Map;

public class JsonEndActionMapper implements IActionMapper<JsonEndAction, EndAction> {

    @Override
    public EndAction mapToInternalModel(JsonEndAction dtoModel) {
        return new EndAction(
                dtoModel.getId()
        );
    }

    @Override
    public JsonEndAction mapToDtoModel(EndAction internalModel) {
        return new JsonEndAction(
                internalModel.getId()
        );
    }

    @Override
    public void resolveReferences(JsonEndAction dtoModel, Map<String, EndAction> internalModelMap, Map<String, Droplet> dropletMap) {
        // No next actions on EndAction
        // No droplets to resolve on EndAction
    }
}
