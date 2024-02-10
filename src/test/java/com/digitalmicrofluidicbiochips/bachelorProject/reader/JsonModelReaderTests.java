package com.digitalmicrofluidicbiochips.bachelorProject.reader;

import com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.JsonProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.actions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static org.junit.Assert.*;

public class JsonModelReaderTests {
    private final String filePath = "src/test/resources/reader/simpleJsonModel.JSON"; // Adjust the file name if needed
    private JsonProgramConfiguration programConfiguration;
    private HashMap<String, JsonActionBase> actionMap;

    @BeforeEach
    public void setUp() {
        File programFile = new File(filePath);
        if (!programFile.exists()) {
            Assertions.fail("The program file does not exist: " + filePath);
        }

        try {
            programConfiguration = JsonModelLoader.loadProgramConfigurationFromJson(filePath);
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
            fail("The loaded program configuration does not contain any actions.");
        }
    }

    @Test
    public void actionMapIsNotEmpty() {
        if (actionMap.isEmpty()) {
            fail("The action map is empty. (It should contain the actions from the program configuration)");
        }
    }

    @Test
    public void startActionIsLoadedCorrectly() {
        String actionId = "unique_start_id";

        if (!actionMap.containsKey(actionId)) {
            fail("The action map does not contain the start action.");
        }

        JsonActionBase actionBase = actionMap.get(actionId);
        if (!(actionBase instanceof JsonStartAction)) {
            fail("The action: " + actionId + "is not of type StartAction.");
        }

        JsonStartAction startAction = (JsonStartAction) actionBase;
        Assertions.assertEquals("unique_start_id", startAction.getId());
        Assertions.assertEquals("next_unique_id_start", startAction.getNextActionId());
    }

    @Test
    public void inputActionIsLoadedCorrectly() {
        String actionId = "unique_input_id";

        if (!actionMap.containsKey(actionId)) {
            fail("The action map does not contain the input action.");
        }

        JsonActionBase actionBase = actionMap.get(actionId);
        if (!(actionBase instanceof JsonInputAction)) {
            fail("The action: " + actionId + "is not of type InputAction.");
        }

        JsonInputAction inputAction = (JsonInputAction) actionBase;
        Assertions.assertEquals("unique_input_id", inputAction.getId());
        Assertions.assertEquals("unique_droplet_id", inputAction.getDropletId());
        Assertions.assertEquals(10, inputAction.getPosX());
        Assertions.assertEquals(20, inputAction.getPosY());
        Assertions.assertEquals(30, inputAction.getVolume());
        Assertions.assertEquals("next_unique_id_input", inputAction.getNextActionId());
    }

    @Test
    public void outputActionIsLoadedCorrectly() {
        String actionId = "unique_output_id";

        if (!actionMap.containsKey(actionId)) {
            fail("The action map does not contain the output action.");
        }

        JsonActionBase actionBase = actionMap.get(actionId);
        if (!(actionBase instanceof JsonOutputAction)) {
            fail("The action: " + actionId + "is not of type OutputAction.");
        }

        JsonOutputAction outputAction = (JsonOutputAction) actionBase;
        Assertions.assertEquals("unique_output_id", outputAction.getId());
        Assertions.assertEquals("unique_droplet_id", outputAction.getDropletId());
        Assertions.assertEquals(30, outputAction.getPosX());
        Assertions.assertEquals(40, outputAction.getPosY());
        Assertions.assertEquals("next_unique_id_output", outputAction.getNextActionId());
    }

    @Test
    public void moveActionIsLoadedCorrectly() {
        String actionId = "unique_move_id";

        if (!actionMap.containsKey(actionId)) {
            fail("The action map does not contain the move action.");
        }

        JsonActionBase actionBase = actionMap.get(actionId);
        if (!(actionBase instanceof JsonMoveAction)) {
            fail("The action: " + actionId + "is not of type MoveAction.");
        }

        JsonMoveAction moveAction = (JsonMoveAction) actionBase;
        Assertions.assertEquals("unique_move_id", moveAction.getId());
        Assertions.assertEquals("unique_droplet_id", moveAction.getDropletId());
        Assertions.assertEquals(50, moveAction.getDestX());
        Assertions.assertEquals(60, moveAction.getDestY());
        Assertions.assertEquals("next_unique_id_move", moveAction.getNextActionId());
    }

    @Test
    public void mergeActionIsLoadedCorrectly() {
        String actionId = "unique_merge_id";

        if (!actionMap.containsKey(actionId)) {
            fail("The action map does not contain the merge action.");
        }

        JsonActionBase actionBase = actionMap.get(actionId);
        if (!(actionBase instanceof JsonMergeAction)) {
            fail("The action: " + actionId + "is not of type MergeAction.");
        }

        JsonMergeAction mergeAction = (JsonMergeAction) actionBase;
        Assertions.assertEquals("unique_merge_id", mergeAction.getId());
        Assertions.assertEquals("unique_droplet_id", mergeAction.getResultDropletId());
        Assertions.assertEquals("unique_droplet_id", mergeAction.getDropletId1());
        Assertions.assertEquals("unique_droplet_id", mergeAction.getDropletId2());
        Assertions.assertEquals(80, mergeAction.getDestX());
        Assertions.assertEquals(90, mergeAction.getDestY());
        Assertions.assertEquals("next_unique_id_merge", mergeAction.getNextActionId());
    }

    @Test
    public void splitActionIsLoadedCorrectly() {
        String actionId = "unique_split_id";

        if (!actionMap.containsKey(actionId)) {
            fail("The action map does not contain the split action.");
        }

        JsonActionBase actionBase = actionMap.get(actionId);
        if (!(actionBase instanceof JsonSplitAction)) {
            fail("The action: " + actionId + "is not of type SplitAction.");
        }

        JsonSplitAction splitAction = (JsonSplitAction) actionBase;
        Assertions.assertEquals("unique_split_id", splitAction.getId());
        Assertions.assertEquals("unique_droplet_id", splitAction.getOriginDroplet());
        Assertions.assertEquals("unique_droplet_id", splitAction.getResultDroplet1());
        Assertions.assertEquals("unique_droplet_id", splitAction.getResultDroplet2());
        Assertions.assertEquals(0.2, splitAction.getRatio());
        Assertions.assertEquals(100, splitAction.getDestX1());
        Assertions.assertEquals(110, splitAction.getDestY1());
        Assertions.assertEquals(120, splitAction.getDestX2());
        Assertions.assertEquals(130, splitAction.getDestY2());
        Assertions.assertEquals("next_unique_id_split", splitAction.getNextActionId());
    }

    @Test
    public void mixActionIsLoadedCorrectly() {
        String actionId = "unique_mix_id";

        if (!actionMap.containsKey(actionId)) {
            fail("The action map does not contain the mix action.");
        }

        JsonActionBase actionBase = actionMap.get(actionId);
        if (!(actionBase instanceof JsonMixAction)) {
            fail("The action: " + actionId + "is not of type MixAction.");
        }

        JsonMixAction mixAction = (JsonMixAction) actionBase;
        Assertions.assertEquals("unique_mix_id", mixAction.getId());
        Assertions.assertEquals("unique_droplet_id", mixAction.getDropletId());
        Assertions.assertEquals("next_unique_id_mix", mixAction.getNextActionId());
    }

    @Test
    public void storeActionIsLoadedCorrectly() {
        String actionId = "unique_store_id";

        if (!actionMap.containsKey(actionId)) {
            fail("The action map does not contain the store action.");
        }

        JsonActionBase actionBase = actionMap.get(actionId);
        if (!(actionBase instanceof JsonStoreAction)) {
            fail("The action: " + actionId + "is not of type StoreAction.");
        }

        JsonStoreAction storeAction = (JsonStoreAction) actionBase;
        Assertions.assertEquals("unique_store_id", storeAction.getId());
        Assertions.assertEquals("unique_droplet_id", storeAction.getDropletId());
        Assertions.assertEquals(140, storeAction.getPosX());
        Assertions.assertEquals(150, storeAction.getPosY());
        Assertions.assertEquals(10, storeAction.getTime());
        Assertions.assertEquals("next_unique_id_store", storeAction.getNextActionId());
    }

    @Test
    public void ifActionIsLoadedCorrectly() {
        String actionId = "unique_if_id";

        if (!actionMap.containsKey(actionId)) {
            fail("The action map does not contain the if action.");
        }

        JsonActionBase actionBase = actionMap.get(actionId);
        if (!(actionBase instanceof JsonIfAction)) {
            fail("The action: " + actionId + "is not of type IfAction.");
        }

        JsonIfAction ifAction = (JsonIfAction) actionBase;
        Assertions.assertEquals("unique_if_id", ifAction.getId());
        Assertions.assertEquals("sample_condition", ifAction.getCondition());
        Assertions.assertEquals("next_true_unique_id_if", ifAction.getTrueNext());
        Assertions.assertEquals("next_false_unique_id_if", ifAction.getFalseNext());
    }

    @Test
    public void repeatActionIsLoadedCorrectly() {
        String actionId = "unique_repeat_id";

        if (!actionMap.containsKey(actionId)) {
            fail("The action map does not contain the repeat action.");
        }

        JsonActionBase actionBase = actionMap.get(actionId);
        if (!(actionBase instanceof JsonRepeatAction)) {
            fail("The action: " + actionId + "is not of type RepeatAction.");
        }

        JsonRepeatAction repeatAction = (JsonRepeatAction) actionBase;
        Assertions.assertEquals("unique_repeat_id", repeatAction.getId());
        Assertions.assertEquals(5, repeatAction.getRepeatCount());
        Assertions.assertEquals("next_repeat_unique_id_repeat", repeatAction.getNextRepeatId());
        Assertions.assertEquals("next_exit_unique_id_repeat", repeatAction.getNextExitId());
    }

    @Test
    public void endActionIsLoadedCorrectly() {
        String actionId = "unique_end_id";

        if (!actionMap.containsKey(actionId)) {
            fail("The action map does not contain the end action.");
        }

        JsonActionBase actionBase = actionMap.get(actionId);
        if (!(actionBase instanceof JsonEndAction)) {
            fail("The action: " + actionId + "is not of type EndAction.");
        }

        JsonEndAction endAction = (JsonEndAction) actionBase;
        Assertions.assertEquals("unique_end_id", endAction.getId());
    }

}
