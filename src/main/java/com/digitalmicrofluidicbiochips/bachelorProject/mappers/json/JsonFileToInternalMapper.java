package com.digitalmicrofluidicbiochips.bachelorProject.mappers.json;

import com.digitalmicrofluidicbiochips.bachelorProject.mappers.generic.actions.IActionMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.mappers.generic.IFileToInternalMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.mappers.json.dmf_platform.JsonDmfInformationMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.mappers.json.factory.JsonActionMapperFactory;
import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.PlatformInformation;
import com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.JsonProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.actions.JsonActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.json.JsonModelLoader;

import java.io.IOException;
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

    @Override
    public ProgramConfiguration getProgramConfiguration() {
        return new ProgramConfiguration(getPlatformInformation(), getActions());
    }

    /**
     * Get a list of all the actions from the JSON file, mapped to their internal representation.
     */
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
    public PlatformInformation getPlatformInformation() {

        //TODO: This is "hardcoded" for now. The platform information should be read from the provided JSON file.
        //For now, the platform is hardcoded to be read from the file "src/test/resources/reader/dmf_configuration.JSON".
        //This allows for us not having to deal with sending the files from the frontend to the backend (for now).
        String dmfConfigFile = "src/test/resources/reader/dmf_configuration.JSON";
        JsonProgramConfiguration dmfProgramConfiguration;
        try {
            dmfProgramConfiguration = JsonModelLoader.loadProgramConfigurationFromJson(dmfConfigFile);
        } catch (IOException e) {
            throw new RuntimeException("Could not read the JSON file: " + dmfConfigFile, e);
        }

        JsonDmfInformationMapper mapper = new JsonDmfInformationMapper();

        return mapper.mapToInternal(dmfProgramConfiguration.getDmfPlatformState());
    }


}
