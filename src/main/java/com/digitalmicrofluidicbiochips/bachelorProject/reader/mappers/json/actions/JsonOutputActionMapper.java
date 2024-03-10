package com.digitalmicrofluidicbiochips.bachelorProject.reader.mappers.json.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.reader.mappers.generic.actions.IActionMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.OutputAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.json.model.actions.JsonOutputAction;

import java.util.Map;

public class JsonOutputActionMapper implements IActionMapper<JsonOutputAction, OutputAction> {

    @Override
    public OutputAction mapToInternalModel(JsonOutputAction dtoModel) {
        return new OutputAction(
                dtoModel.getId(),
                dtoModel.getPosX(),
                dtoModel.getPosY()
        );
    }

    @Override
    public JsonOutputAction mapToDtoModel(OutputAction internalModel) {
        return new JsonOutputAction(
                internalModel.getId(),
                internalModel.getDroplet().getID(),
                internalModel.getPosX(),
                internalModel.getPosY(),
                internalModel.getNextAction().getId()
        );
    }

    @Override
    public void resolveReferences(JsonOutputAction dtoModel, Map<String, OutputAction> internalModelMap, Map<String, Droplet> dropletMap) {
        OutputAction outputAction = internalModelMap.get(dtoModel.getId());

        outputAction.setNextAction(internalModelMap.get(dtoModel.getNextActionId()));

        outputAction.setDroplet(dropletMap.get(dtoModel.getDropletId()));
    }
}
