package com.digitalmicrofluidicbiochips.bachelorProject.mappers.json.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.mappers.generic.actions.IActionMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.mappers.json.factory.JsonActionMapperFactory;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.*;
import com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.actions.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JsonActionMapperFactoryTests {

    private ActionBase getInternalAction(JsonActionBase jsonAction) {
        IActionMapper mapper = JsonActionMapperFactory.getMapper(jsonAction.getClass());
        return mapper.mapToInternalModel(jsonAction);
    }

    @Test
    public void MapJsonStartActionDtoModelToInternalModel() {

        // Arrange
        String id = "id";
        String nextActionId = "nextActionId";

        JsonStartAction jsonStartAction = new JsonStartAction(id, nextActionId);

        // Act
        ActionBase action = getInternalAction(jsonStartAction);

        // Assert
        if(!(action instanceof StartAction)) {
            Assertions.fail("jsonAction was not converted correctly to internal model");
        }

        StartAction startAction = (StartAction) action;
        Assertions.assertEquals(id, startAction.getId());
        Assertions.assertNull(startAction.getNextAction());
    }

    @Test
    public void MapsJsonInputActionDtoModelToInternalModel() {

        // Arrange
        String id = "id";
        String dropletId = "dropletId";
        int posX = 1;
        int posY = 2;
        int volume = 3;
        String nextActionId = "nextActionId";

        JsonInputAction jsonInputAction = new JsonInputAction(id, dropletId, posX, posY, volume, nextActionId);

        // Act
        ActionBase action = getInternalAction(jsonInputAction);

        // Assert
        if (!(action instanceof InputAction)) {
            Assertions.fail("jsonAction was not converted correctly to internal model");
        }

        InputAction inputAction = (InputAction) action;
        Assertions.assertEquals(id, inputAction.getId());
        Assertions.assertEquals(dropletId, inputAction.getDropletId());
        Assertions.assertEquals(posX, inputAction.getPosX());
        Assertions.assertEquals(posY, inputAction.getPosY());
        Assertions.assertEquals(volume, inputAction.getVolume());
        Assertions.assertNull(inputAction.getNextAction());
    }

    @Test
    public void MapsJsonOutputActionDtoModelToInternalModel() {
        // Arrange
        String id = "id";
        String dropletId = "dropletId";
        int posX = 1;
        int posY = 2;
        String nextActionId = "nextActionId";

        JsonOutputAction jsonOutputAction = new JsonOutputAction(id, dropletId, posX, posY, nextActionId);

        // Act
        ActionBase action = getInternalAction(jsonOutputAction);

        // Assert
        if (!(action instanceof OutputAction)) {
            Assertions.fail("jsonAction was not converted correctly to internal model");
        }

        OutputAction outputAction = (OutputAction) action;
        Assertions.assertEquals(id, outputAction.getId());
        Assertions.assertEquals(dropletId, outputAction.getDropletId());
        Assertions.assertEquals(posX, outputAction.getPosX());
        Assertions.assertEquals(posY, outputAction.getPosY());
        Assertions.assertNull(outputAction.getNextAction());
    }

    @Test
    public void MapsJsonMoveActionDtoModelToInternalModel() {
        // Arrange
        String id = "id";
        String dropletId = "dropletId";
        int posX = 1;
        int posY = 2;
        String nextActionId = "nextActionId";

        JsonMoveAction jsonMoveAction = new JsonMoveAction(id, dropletId, posX, posY, nextActionId);

        // Act
        ActionBase action = getInternalAction(jsonMoveAction);

        // Assert
        if (!(action instanceof MoveAction)) {
            Assertions.fail("jsonAction was not converted correctly to internal model");
        }

        MoveAction moveAction = (MoveAction) action;
        Assertions.assertEquals(id, moveAction.getId());
        Assertions.assertEquals(dropletId, moveAction.getDropletId());
        Assertions.assertEquals(posX, moveAction.getPosX());
        Assertions.assertEquals(posY, moveAction.getPosY());
        Assertions.assertNull(moveAction.getNextAction());
    }

    @Test
    public void MapsJsonMergeActionDtoModelToInternalModel() {
        // Arrange
        String id = "id";
        String resultDropletId = "ResultDropletId";
        String dropletId1 = "dropletId1";
        String dropletId2 = "dropletId2";
        int posX = 1;
        int posY = 2;
        String nextActionId = "nextActionId";

        JsonMergeAction jsonMergeAction = new JsonMergeAction(id, resultDropletId, dropletId1, dropletId2, posX, posY, nextActionId);

        // Act
        ActionBase action = getInternalAction(jsonMergeAction);

        // Assert
        if (!(action instanceof MergeAction)) {
            Assertions.fail("jsonAction was not converted correctly to internal model");
        }

        MergeAction mergeAction = (MergeAction) action;
        Assertions.assertEquals(id, mergeAction.getId());
        Assertions.assertEquals(resultDropletId, mergeAction.getResultDropletId());
        Assertions.assertEquals(dropletId1, mergeAction.getDropletId1());
        Assertions.assertEquals(dropletId2, mergeAction.getDropletId2());
        Assertions.assertEquals(posX, mergeAction.getPosX());
        Assertions.assertEquals(posY, mergeAction.getPosY());
        Assertions.assertNull(mergeAction.getNextAction());
    }

    @Test
    public void MapsJsonSplitActionDtoModelToInternalModel() {
        // Arrange
        String id = "id";
        String originDropletId = "originDropletId";
        String resultDropletId1 = "ResultDropletId1";
        String resultDropletId2 = "ResultDropletId2";
        double splitRatio = 0.5;
        int posX1 = 1;
        int posY1 = 2;
        int posX2 = 3;
        int posY2 = 4;
        String nextActionId = "nextActionId";

        JsonSplitAction jsonSplitAction = new JsonSplitAction(id, originDropletId, resultDropletId1, resultDropletId2, splitRatio, posX1, posY1, posX2, posY2, nextActionId);

        // Act
        ActionBase action = getInternalAction(jsonSplitAction);

        // Assert
        if (!(action instanceof SplitAction)) {
            Assertions.fail("jsonAction was not converted correctly to internal model");
        }

        SplitAction splitAction = (SplitAction) action;
        Assertions.assertEquals(id, splitAction.getId());
        Assertions.assertEquals(originDropletId, splitAction.getOriginDroplet());
        Assertions.assertEquals(resultDropletId1, splitAction.getResultDroplet1());
        Assertions.assertEquals(resultDropletId2, splitAction.getResultDroplet2());
        Assertions.assertEquals(splitRatio, splitAction.getRatio());
        Assertions.assertEquals(posX1, splitAction.getPosX1());
        Assertions.assertEquals(posY1, splitAction.getPosY1());
        Assertions.assertEquals(posX2, splitAction.getPosX2());
        Assertions.assertEquals(posY2, splitAction.getPosY2());
        Assertions.assertNull(splitAction.getNextAction());
    }

    @Test
    public void MapsJsonMixActionDtoModelToInternalModel() {
        // Arrange
        String id = "id";
        String dropletId = "dropletId";
        String nextActionId = "nextActionId";

        JsonMixAction jsonMixAction = new JsonMixAction(id, dropletId, nextActionId);

        // Act
        ActionBase action = getInternalAction(jsonMixAction);

        // Assert
        if (!(action instanceof MixAction)) {
            Assertions.fail("jsonAction was not converted correctly to internal model");
        }

        MixAction mixAction = (MixAction) action;
        Assertions.assertEquals(id, mixAction.getId());
        Assertions.assertEquals(dropletId, mixAction.getDropletId());
        Assertions.assertNull(mixAction.getNextAction());
    }

    @Test
    public void MapsJsonStoreActionDtoModelToInternalModel() {
        // Arrange
        String id = "id";
        String dropletId = "dropletId";
        int posX = 1;
        int posY = 2;
        int time = 3;
        String nextActionId = "nextActionId";

        JsonStoreAction jsonStoreAction = new JsonStoreAction(id, dropletId, posX, posY, time, nextActionId);

        // Act
        ActionBase action = getInternalAction(jsonStoreAction);

        // Assert
        if (!(action instanceof StoreAction)) {
            Assertions.fail("jsonAction was not converted correctly to internal model");
        }

        StoreAction storeAction = (StoreAction) action;
        Assertions.assertEquals(id, storeAction.getId());
        Assertions.assertEquals(dropletId, storeAction.getDropletId());
        Assertions.assertEquals(posX, storeAction.getPosX());
        Assertions.assertEquals(posY, storeAction.getPosY());
        Assertions.assertEquals(time, storeAction.getTime());
        Assertions.assertNull(storeAction.getNextAction());
    }

    @Test
    public void MapsJsonIfActionDtoModelToInternalModel() {
        // Arrange
        String id = "id";
        String condition = "condition";
        String nextActionId = "nextActionId";
        String elseActionId = "elseActionId";

        JsonIfAction jsonIfAction = new JsonIfAction(id, condition, nextActionId, elseActionId);

        // Act
        ActionBase action = getInternalAction(jsonIfAction);

        // Assert
        if (!(action instanceof IfAction)) {
            Assertions.fail("jsonAction was not converted correctly to internal model");
        }

        IfAction ifAction = (IfAction) action;
        Assertions.assertEquals(id, ifAction.getId());
        Assertions.assertEquals(condition, ifAction.getCondition());
        Assertions.assertNull(ifAction.getTrueNextAction());
        Assertions.assertNull(ifAction.getFalseNextAction());
    }

    @Test
    public void MapsJsonRepeatActionDtoModelToInternalModel() {
        // Arrange
        String id = "id";
        int times = 3;
        String nextRepeatId = "nextActionId";
        String nextExitId = "nextExitId";

        JsonRepeatAction jsonRepeatAction = new JsonRepeatAction(id, times, nextRepeatId, nextExitId);

        // Act
        ActionBase action = getInternalAction(jsonRepeatAction);

        // Assert
        if (!(action instanceof RepeatAction)) {
            Assertions.fail("jsonAction was not converted correctly to internal model");
        }

        RepeatAction repeatAction = (RepeatAction) action;
        Assertions.assertEquals(id, repeatAction.getId());
        Assertions.assertEquals(times, repeatAction.getTimes());
        Assertions.assertNull(repeatAction.getNextRepeatAction());
        Assertions.assertNull(repeatAction.getNextExitAction());
    }

    @Test
    public void MapsJsonEndActionDtoModelToInternalModel() {
        // Arrange
        String id = "id";

        JsonEndAction jsonEndAction = new JsonEndAction(id);

        // Act
        ActionBase action = getInternalAction(jsonEndAction);

        // Assert
        if (!(action instanceof EndAction)) {
            Assertions.fail("jsonAction was not converted correctly to internal model");
        }

        EndAction endAction = (EndAction) action;
        Assertions.assertEquals(id, endAction.getId());
    }

}
