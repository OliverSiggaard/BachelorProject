package com.digitalmicrofluidicbiochips.bachelorProject.mappers.json;

import com.digitalmicrofluidicbiochips.bachelorProject.mappers.IActionMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.mappers.IFileToInternalMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.DmfPlatformState;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Electrode;
import com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.JsonProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.actions.JsonActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.dmf_platform.JsonDmfPlatformInformation;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.JsonModelLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Main access point for the JSON file to internal model mapping.
 * Provided with a file path, it will map the JSON file to the internal model(s).
 * <br>
 * This includes actions, and relevant information about the DMF platform.
 */
public class JsonFileToInternalMapper implements IFileToInternalMapper {

    private final JsonProgramConfiguration programConfiguration;

    /**
     * Create a new instance of the JsonFileToInternalMapper.
     * @param filePath The path to the JSON file to be mapped to the internal model.
     */
    public JsonFileToInternalMapper(String filePath) {
        try {
            programConfiguration = JsonModelLoader.loadProgramConfigurationFromJson(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Could not read the JSON file: " + filePath, e);
        }
    }

    /**
     * Get a list of all the actions from the JSON file, mapped to their internal representation.
     */
    @Override
    public List<ActionBase> getActions() {

        HashMap<String, ActionBase> actionMap = new HashMap<>();

        //Creating a list of all the internal actions from the JSON actions. Initially, the next actions are not resolved.
        List<JsonActionBase> JsonActions = programConfiguration.getProgramActions();
        JsonActions.forEach(jsonAction -> {
            IActionMapper mapper = JsonActionMapperFactory.getMapper(jsonAction.getClass());
            ActionBase action = mapper.mapToInternalModel(jsonAction);
            actionMap.put(action.getId(), action);
        });

        //Resolving the next actions for all the internal actions. The JsonActions are required to resolve the next actions.
        JsonActions.forEach(jsonAction -> {
            IActionMapper mapper = JsonActionMapperFactory.getMapper(jsonAction.getClass());
            mapper.resolveReferences(jsonAction, actionMap);
        });

        return actionMap.values().stream().toList();
    }

    /**
     * Get the platform information from the JSON file.
     */
    @Override
    public DmfPlatformState getPlatformInformation() {

        JsonDmfPlatformInformation platformInformation = programConfiguration.getDmfPlatformState().getInformation();

        // TODO: Gather relevant information regarding electrodes.?
        List<Electrode> electrodes = new ArrayList<>();


        // TODO: Information Actuators?
        // TODO Information about sensors?
        // TODO Information about input/output?

        DmfPlatformState dmfPlatformState = new DmfPlatformState(
                platformInformation.getSizeX(),
                platformInformation.getSizeY(),
                electrodes
        );

        return dmfPlatformState;
    }
}
