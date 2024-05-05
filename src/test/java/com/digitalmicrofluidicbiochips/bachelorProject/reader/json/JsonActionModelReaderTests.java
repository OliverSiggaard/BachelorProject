package com.digitalmicrofluidicbiochips.bachelorProject.reader.json;

import com.digitalmicrofluidicbiochips.bachelorProject.reader.json.model.JsonProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.json.model.actions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class JsonActionModelReaderTests {
    private final String filePath = "src/test/resources/reader/simpleActionModel.JSON"; // Adjust the file name if needed
    private JsonProgramConfiguration programConfiguration;
    private HashMap<String, JsonActionBase> actionMap;

    @BeforeEach
    public void setUp() {
        File programFile = new File(filePath);
        if (!programFile.exists()) {
            Assertions.fail("The program file does not exist: " + filePath);
        }

        try {
            programConfiguration = JsonModelLoader.loadProgramConfigurationFromJson(programFile);
        } catch (IOException e) {
            Assertions.fail("Could not read the program configuration from the JSON file. Error: " + e.getMessage());
        }

        actionMap = programConfiguration.getProgramActions()
                .stream()
                .collect(HashMap::new, (map, action) -> map.put(action.getId(), action), HashMap::putAll);

    }

    @Test
    public void actionListAreNotEmpty() {
        if (programConfiguration.getProgramActions().isEmpty()) {
            Assertions.fail("The loaded program configuration does not contain any actions.");
        }
    }

    @Test
    public void actionMapIsNotEmpty() {
        if (actionMap.isEmpty()) {
            Assertions.fail("The action map is empty. (It should contain the actions from the program configuration)");
        }
    }

    @Test
    public void inputActionIsLoadedCorrectly() {
        String actionId = "unique_input_id";

        if (!actionMap.containsKey(actionId)) {
            Assertions.fail("The action map does not contain the input action.");
        }

        JsonActionBase actionBase = actionMap.get(actionId);
        if (!(actionBase instanceof JsonInputAction)) {
            Assertions.fail("The action: " + actionId + "is not of type InputAction.");
        }

        JsonInputAction inputAction = (JsonInputAction) actionBase;
        Assertions.assertEquals("unique_input_id", inputAction.getId());
        Assertions.assertEquals("unique_droplet_id", inputAction.getDropletId());
        Assertions.assertEquals(1, inputAction.getPosX());
        Assertions.assertEquals(2, inputAction.getPosY());
        Assertions.assertEquals(30, inputAction.getVolume());
        Assertions.assertEquals("unique_output_id", inputAction.getNextActionId());
    }

    @Test
    public void outputActionIsLoadedCorrectly() {
        String actionId = "unique_output_id";

        if (!actionMap.containsKey(actionId)) {
            Assertions.fail("The action map does not contain the output action.");
        }

        JsonActionBase actionBase = actionMap.get(actionId);
        if (!(actionBase instanceof JsonOutputAction)) {
            Assertions.fail("The action: " + actionId + "is not of type OutputAction.");
        }

        JsonOutputAction outputAction = (JsonOutputAction) actionBase;
        Assertions.assertEquals("unique_output_id", outputAction.getId());
        Assertions.assertEquals("unique_droplet_id", outputAction.getDropletId());
        Assertions.assertEquals(3, outputAction.getPosX());
        Assertions.assertEquals(4, outputAction.getPosY());
        Assertions.assertEquals("unique_move_id", outputAction.getNextActionId());
    }

    @Test
    public void moveActionIsLoadedCorrectly() {
        String actionId = "unique_move_id";

        if (!actionMap.containsKey(actionId)) {
            Assertions.fail("The action map does not contain the move action.");
        }

        JsonActionBase actionBase = actionMap.get(actionId);
        if (!(actionBase instanceof JsonMoveAction)) {
            Assertions.fail("The action: " + actionId + "is not of type MoveAction.");
        }

        JsonMoveAction moveAction = (JsonMoveAction) actionBase;
        Assertions.assertEquals("unique_move_id", moveAction.getId());
        Assertions.assertEquals("unique_droplet_id", moveAction.getDropletId());
        Assertions.assertEquals(5, moveAction.getPosX());
        Assertions.assertEquals(6, moveAction.getPosY());
        Assertions.assertEquals("unique_merge_id", moveAction.getNextActionId());
    }

    @Test
    public void mergeActionIsLoadedCorrectly() {
        String actionId = "unique_merge_id";

        if (!actionMap.containsKey(actionId)) {
            Assertions.fail("The action map does not contain the merge action.");
        }

        JsonActionBase actionBase = actionMap.get(actionId);
        if (!(actionBase instanceof JsonMergeAction)) {
            Assertions.fail("The action: " + actionId + "is not of type MergeAction.");
        }

        JsonMergeAction mergeAction = (JsonMergeAction) actionBase;
        Assertions.assertEquals("unique_merge_id", mergeAction.getId());
        Assertions.assertEquals("unique_droplet_id2", mergeAction.getResultDropletId());
        Assertions.assertEquals("unique_droplet_id", mergeAction.getDropletId1());
        Assertions.assertEquals("unique_droplet_id", mergeAction.getDropletId2());
        Assertions.assertEquals(8, mergeAction.getPosX());
        Assertions.assertEquals(9, mergeAction.getPosY());
        Assertions.assertEquals("unique_split_id", mergeAction.getNextActionId());
    }

    @Test
    public void splitActionIsLoadedCorrectly() {
        String actionId = "unique_split_id";

        if (!actionMap.containsKey(actionId)) {
            Assertions.fail("The action map does not contain the split action.");
        }

        JsonActionBase actionBase = actionMap.get(actionId);
        if (!(actionBase instanceof JsonSplitAction)) {
            Assertions.fail("The action: " + actionId + "is not of type SplitAction.");
        }

        JsonSplitAction splitAction = (JsonSplitAction) actionBase;
        Assertions.assertEquals("unique_split_id", splitAction.getId());
        Assertions.assertEquals("unique_droplet_id", splitAction.getOriginDropletId());
        Assertions.assertEquals("unique_droplet_id3", splitAction.getResultDropletId1());
        Assertions.assertEquals("unique_droplet_id4", splitAction.getResultDropletId2());
        Assertions.assertEquals(0.2, splitAction.getRatio());
        Assertions.assertEquals(10, splitAction.getPosX1());
        Assertions.assertEquals(11, splitAction.getPosY1());
        Assertions.assertEquals(12, splitAction.getPosX2());
        Assertions.assertEquals(13, splitAction.getPosY2());
        Assertions.assertEquals("unique_mix_id", splitAction.getNextActionId());
    }

    @Test
    public void mixActionIsLoadedCorrectly() {
        String actionId = "unique_mix_id";

        if (!actionMap.containsKey(actionId)) {
            Assertions.fail("The action map does not contain the mix action.");
        }

        JsonActionBase actionBase = actionMap.get(actionId);
        if (!(actionBase instanceof JsonMixAction)) {
            Assertions.fail("The action: " + actionId + "is not of type MixAction.");
        }

        JsonMixAction mixAction = (JsonMixAction) actionBase;
        Assertions.assertEquals("unique_mix_id", mixAction.getId());
        Assertions.assertEquals("unique_droplet_id", mixAction.getDropletId());
        Assertions.assertEquals("unique_store_id", mixAction.getNextActionId());
        Assertions.assertEquals(1, mixAction.getPosX());
        Assertions.assertEquals(2, mixAction.getPosY());
        Assertions.assertEquals(3, mixAction.getSizeX());
        Assertions.assertEquals(4, mixAction.getSizeY());
    }

    @Test
    public void storeActionIsLoadedCorrectly() {
        String actionId = "unique_store_id";

        if (!actionMap.containsKey(actionId)) {
            Assertions.fail("The action map does not contain the store action.");
        }

        JsonActionBase actionBase = actionMap.get(actionId);
        if (!(actionBase instanceof JsonStoreAction)) {
            Assertions.fail("The action: " + actionId + "is not of type StoreAction.");
        }

        JsonStoreAction storeAction = (JsonStoreAction) actionBase;
        Assertions.assertEquals("unique_store_id", storeAction.getId());
        Assertions.assertEquals("unique_droplet_id", storeAction.getDropletId());
        Assertions.assertEquals(14, storeAction.getPosX());
        Assertions.assertEquals(15, storeAction.getPosY());
        Assertions.assertEquals(10, storeAction.getTime());
        Assertions.assertEquals("unique_mix_id", storeAction.getNextActionId());
    }

}
