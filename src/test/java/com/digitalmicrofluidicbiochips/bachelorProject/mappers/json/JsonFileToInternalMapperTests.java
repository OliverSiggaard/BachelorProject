package com.digitalmicrofluidicbiochips.bachelorProject.mappers.json;

import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.*;
import com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.JsonProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.actions.*;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.json.JsonModelLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonFileToInternalMapperTests {


    private final String filePath = "src/test/resources/reader/simpleActionModel.JSON"; // Adjust the file name if needed
    private ProgramConfiguration programConfiguration;
    private JsonProgramConfiguration jsonProgramConfiguration;

    @BeforeEach
    public void setUp() {
        File programFile = new File(filePath);
        if (!programFile.exists()) {
            Assertions.fail("The program file does not exist: " + filePath);
        }

        try {
            jsonProgramConfiguration = JsonModelLoader.loadProgramConfigurationFromJson(filePath);
        } catch (IOException e) {
            Assertions.fail("Could not read the program configuration from the JSON file. Error: " + e.getMessage());
        }

        JsonFileToInternalMapper jsonFileToInternalMapper = new JsonFileToInternalMapper(filePath);
        programConfiguration = jsonFileToInternalMapper.getProgramConfiguration();
    }

    @Test
    public void correctNumberOfActionsMapped() {
        Assertions.assertEquals(
                jsonProgramConfiguration.getProgramActions().size(),
                programConfiguration.getProgramActions().size()
        );
    }

   /*
    * Since the actionMapperFactory is tested separately, we can assume that the actions are mapped correctly.
    * We do however need to test if the "next" actions are resolved correctly.
    *
    * Hereunder sloppily tested, with the data from the simpleActionModel.JSON file - We therefore assume,
    * that only 1 instance of each action type is present in the JSON file, and that the next actions are properly set.
    */

    @Test
    public void startActionNextActionsAreResolvedCorrectly() {
        Map<String, ActionBase> actions = programConfiguration.getProgramActions().stream()
                .collect(Collectors.toMap(ActionBase::getId, action -> action));

        JsonStartAction jsonStartAction = (JsonStartAction) jsonProgramConfiguration.getProgramActions().stream()
                .filter(action -> action instanceof JsonStartAction)
                .findFirst()
                .orElseThrow();

        StartAction startAction = (StartAction) actions.get(jsonStartAction.getId());
        Assertions.assertEquals(jsonStartAction.getNextActionId(), startAction.getNextAction().getId());
    }

    @Test
    public void inputActionNextActionsAreResolvedCorrectly() {
        Map<String, ActionBase> actions = programConfiguration.getProgramActions().stream()
                        .collect(Collectors.toMap(ActionBase::getId, action -> action));

        JsonInputAction jsonInputAction = (JsonInputAction) jsonProgramConfiguration.getProgramActions().stream()
                .filter(action -> action instanceof JsonInputAction)
                .findFirst()
                .orElseThrow();

        InputAction inputAction = (InputAction) actions.get(jsonInputAction.getId());
        Assertions.assertEquals(jsonInputAction.getNextActionId(), inputAction.getNextAction().getId());
    }

    @Test
    public void OutputActionNextActionsAreResolvedCorrectly() {
        Map<String,ActionBase> actions = programConfiguration.getProgramActions().stream()
                .collect(Collectors.toMap(ActionBase::getId, action -> action));

        JsonOutputAction jsonOutputAction = (JsonOutputAction) jsonProgramConfiguration.getProgramActions().stream()
                .filter(action -> action instanceof JsonOutputAction)
                .findFirst()
                .orElseThrow();

        OutputAction outputAction = (OutputAction) actions.get(jsonOutputAction.getId());
        Assertions.assertEquals(jsonOutputAction.getNextActionId(), outputAction.getNextAction().getId());
    }

    @Test
    public void MoveActionNextActionsAreResolvedCorrectly() {
        Map<String,ActionBase> actions = programConfiguration.getProgramActions().stream()
                .collect(Collectors.toMap(ActionBase::getId, action -> action));

        JsonMoveAction jsonMoveAction = (JsonMoveAction) jsonProgramConfiguration.getProgramActions().stream()
                .filter(action -> action instanceof JsonMoveAction)
                .findFirst()
                .orElseThrow();

        MoveAction moveAction = (MoveAction) actions.get(jsonMoveAction.getId());
        Assertions.assertEquals(jsonMoveAction.getNextActionId(), moveAction.getNextAction().getId());
    }

    @Test
    public void MergeActionNextActionsAreResolvedCorrectly() {
        Map<String,ActionBase> actions = programConfiguration.getProgramActions().stream()
                .collect(Collectors.toMap(ActionBase::getId, action -> action));

        JsonMergeAction jsonMergeAction = (JsonMergeAction) jsonProgramConfiguration.getProgramActions().stream()
                .filter(action -> action instanceof JsonMergeAction)
                .findFirst()
                .orElseThrow();

        MergeAction mergeAction = (MergeAction) actions.get(jsonMergeAction.getId());
        Assertions.assertEquals(jsonMergeAction.getNextActionId(), mergeAction.getNextAction().getId());
    }

    @Test
    public void SplitActionNextActionsAreResolvedCorrectly() {
        Map<String,ActionBase> actions = programConfiguration.getProgramActions().stream()
                .collect(Collectors.toMap(ActionBase::getId, action -> action));

        JsonSplitAction jsonSplitAction = (JsonSplitAction) jsonProgramConfiguration.getProgramActions().stream()
                .filter(action -> action instanceof JsonSplitAction)
                .findFirst()
                .orElseThrow();

        SplitAction splitAction = (SplitAction) actions.get(jsonSplitAction.getId());
        Assertions.assertEquals(jsonSplitAction.getNextActionId(), splitAction.getNextAction().getId());
    }

    @Test
    public void MixActionNextActionsAreResolvedCorrectly() {
        Map<String,ActionBase> actions = programConfiguration.getProgramActions().stream()
                .collect(Collectors.toMap(ActionBase::getId, action -> action));

        JsonMixAction jsonMixAction = (JsonMixAction) jsonProgramConfiguration.getProgramActions().stream()
                .filter(action -> action instanceof JsonMixAction)
                .findFirst()
                .orElseThrow();

        MixAction mixAction = (MixAction) actions.get(jsonMixAction.getId());
        Assertions.assertEquals(jsonMixAction.getNextActionId(), mixAction.getNextAction().getId());
    }

    @Test
    public void StoreActionNextActionsAreResolvedCorrectly() {
        Map<String,ActionBase> actions = programConfiguration.getProgramActions().stream()
                .collect(Collectors.toMap(ActionBase::getId, action -> action));

        JsonStoreAction jsonStoreAction = (JsonStoreAction) jsonProgramConfiguration.getProgramActions().stream()
                .filter(action -> action instanceof JsonStoreAction)
                .findFirst()
                .orElseThrow();

        StoreAction storeAction = (StoreAction) actions.get(jsonStoreAction.getId());
        Assertions.assertEquals(jsonStoreAction.getNextActionId(), storeAction.getNextAction().getId());
    }

    @Test
    public void IfActionNextActionsAreResolvedCorrectly() {
        Map<String,ActionBase> actions = programConfiguration.getProgramActions().stream()
                .collect(Collectors.toMap(ActionBase::getId, action -> action));

        JsonIfAction jsonIfAction = (JsonIfAction) jsonProgramConfiguration.getProgramActions().stream()
                .filter(action -> action instanceof JsonIfAction)
                .findFirst()
                .orElseThrow();

        IfAction ifAction = (IfAction) actions.get(jsonIfAction.getId());
        Assertions.assertEquals(jsonIfAction.getTrueNextActionId(), ifAction.getTrueNextAction().getId());
        Assertions.assertEquals(jsonIfAction.getFalseNextActionId(), ifAction.getFalseNextAction().getId());
    }

    @Test
    public void RepeatActionNextActionsAreResolvedCorrectly() {
        Map<String,ActionBase> actions = programConfiguration.getProgramActions().stream()
                .collect(Collectors.toMap(ActionBase::getId, action -> action));

        JsonRepeatAction jsonRepeatAction = (JsonRepeatAction) jsonProgramConfiguration.getProgramActions().stream()
                .filter(action -> action instanceof JsonRepeatAction)
                .findFirst()
                .orElseThrow();

        RepeatAction repeatAction = (RepeatAction) actions.get(jsonRepeatAction.getId());
        Assertions.assertEquals(jsonRepeatAction.getRepeatNextActionId(), repeatAction.getNextRepeatAction().getId());
        Assertions.assertEquals(jsonRepeatAction.getExitNextActionId(), repeatAction.getNextExitAction().getId());
    }

}
