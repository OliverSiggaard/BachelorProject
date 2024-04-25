package com.digitalmicrofluidicbiochips.bachelorProject.reader.mappers.json;

import com.digitalmicrofluidicbiochips.bachelorProject.reader.json.model.actions.JsonMergeAction;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.json.model.actions.JsonSplitAction;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.mappers.generic.actions.IActionMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.mappers.generic.IDtoToInternalMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.mappers.json.dmf_platform.JsonDmfInformationMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.mappers.json.factory.JsonActionMapperFactory;
import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.PlatformInformation;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.json.model.JsonProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.json.model.actions.JsonActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.json.model.actions.JsonInputAction;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.json.JsonModelLoader;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Main access point for the JSON file to internal model mapping.
 * Provided with a file path, it will map the JSON file to the internal model(s).
 * <br>
 * This includes actions, and relevant information about the DMF platform.
 */
public class JsonToInternalMapper implements IDtoToInternalMapper {

    private final JsonProgramConfiguration programConfiguration;

    /**
     * Create a new instance of the JsonFileToInternalMapper.
     *
     * Mainly used for testing purposes.
     *
     * @param jsonFile the JSON file to be mapped to the internal model.
     */
    public JsonToInternalMapper(File jsonFile) {
        try {
            programConfiguration = JsonModelLoader.loadProgramConfigurationFromJson(jsonFile);
        } catch (IOException e) {
            throw new RuntimeException("Could not read the JSON file: " + jsonFile.getPath(), e);
        }
    }

    /**
     * Create a new instance of the JsonFileToInternalMapper.
     *
     * Used when receiving a JSON string from the frontend.
     *
     * @param jsonString the JSON string to be mapped to the internal model.
     */
    public JsonToInternalMapper(String jsonString) {
        try {
            programConfiguration = JsonModelLoader.loadProgramConfigurationFromJsonString(jsonString);
        } catch (IOException e) {
            throw new RuntimeException("Could not read the JSON object: " + jsonString, e);
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

        //Creating a list of all the droplets from the json input actions.
        List<Droplet> dropletList = deriveAllDropletsFromJsonActions(JsonActions);
        Map<String, Droplet> dropletMap = dropletList.stream()
                .collect(Collectors.toMap(Droplet::getID, droplet -> droplet));

        //Resolving the next actions for all the internal actions. The JsonActions are required to resolve the next actions.
        JsonActions.forEach(jsonAction -> {
            IActionMapper mapper = JsonActionMapperFactory.getMapper(jsonAction.getClass());
            mapper.resolveReferences(jsonAction, actionMap, dropletMap);
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
        String dmfConfigFilePath = "src/test/resources/reader/dmf_configuration.JSON";
        File dmfConfigFile = new File(dmfConfigFilePath);

        JsonProgramConfiguration dmfProgramConfiguration;
        try {
            dmfProgramConfiguration = JsonModelLoader.loadProgramConfigurationFromJson(dmfConfigFile);
        } catch (IOException e) {
            throw new RuntimeException("Could not read the JSON file: " + dmfConfigFile, e);
        }

        JsonDmfInformationMapper mapper = new JsonDmfInformationMapper();

        return mapper.mapToInternal(dmfProgramConfiguration.getDmfPlatformState());
    }



    private List<Droplet> deriveAllDropletsFromJsonActions(List<JsonActionBase> actions) {
        List<Droplet> droplets = new ArrayList<>();

        // Add all droplets created by the JsonInputActions to the list of droplets.
        droplets.addAll(actions.stream()
                .filter(a -> a instanceof JsonInputAction)
                .map( a -> {
                    JsonInputAction jsonInputAction = (JsonInputAction) a;
                    return new Droplet(jsonInputAction.getDropletId(), jsonInputAction.getPosX(), jsonInputAction.getPosY(), jsonInputAction.getVolume());
                })
                .toList());

        // Add all droplets created by the JsonMergeActions to the list of droplets.
        droplets.addAll(actions.stream()
                .filter(a -> a instanceof JsonMergeAction)
                .map( a -> {
                    JsonMergeAction jsonMergeAction = (JsonMergeAction) a;
                    return new Droplet(jsonMergeAction.getResultDropletId(), jsonMergeAction.getPosX(), jsonMergeAction.getPosY(), 0);
                })
                .toList());

        // Add all droplets created by the JsonSplitActions to the list of droplets.
        droplets.addAll(actions.stream()
                .filter(a -> a instanceof JsonSplitAction)
                .flatMap(a -> {
                    JsonSplitAction jsonSplitAction = (JsonSplitAction) a;
                    Droplet droplet1 = new Droplet(jsonSplitAction.getResultDropletId1(), jsonSplitAction.getPosX1(), jsonSplitAction.getPosY1(), 0);
                    Droplet droplet2 = new Droplet(jsonSplitAction.getResultDropletId2(), jsonSplitAction.getPosX2(), jsonSplitAction.getPosY2(), 0);
                    return Stream.of(droplet1, droplet2);
                })
                .toList());

        return droplets;



    }


}
