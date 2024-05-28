package com.digitalmicrofluidicbiochips.bachelorProject.compiler;

import com.digitalmicrofluidicbiochips.bachelorProject.errors.DmfInputReaderException;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations.InputAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.DropletStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;


import java.util.*;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ScheduleTest {

    private Schedule sut;

    private Droplet droplet1;
    private Droplet droplet2;

    private ActionBase action1;
    private ActionBase action2;
    private ActionBase action3;
    private ActionBase action4;

    @BeforeEach
    public void setUp() {
        droplet1 = mock(Droplet.class);
        droplet2 = mock(Droplet.class);

        when(droplet1.getID()).thenReturn("1");
        when(droplet2.getID()).thenReturn("2");

        InputAction produce1 = mock(InputAction.class);
        when(produce1.dropletsProducedByExecution()).thenReturn(Set.of(droplet1));
        InputAction produce2 = mock(InputAction.class);
        when(produce2.dropletsProducedByExecution()).thenReturn(Set.of(droplet2));

        action1 = mock(ActionBase.class);
        action2 = mock(ActionBase.class);
        action3 = mock(ActionBase.class);
        action4 = mock(ActionBase.class);

        when(action1.dropletsRequiredForExecution()).thenReturn(Set.of(droplet1));
        when(action2.dropletsRequiredForExecution()).thenReturn(Set.of(droplet1));
        when(action3.dropletsRequiredForExecution()).thenReturn(Set.of(droplet2));
        when(action4.dropletsRequiredForExecution()).thenReturn(Set.of(droplet1, droplet2));

        List<ActionBase> actions = new ArrayList<>(List.of(produce1, produce2, action1, action2, action3, action4));
        sut = new Schedule(actions);
        when(produce1.getStatus()).thenReturn(ActionStatus.COMPLETED);
        when(produce2.getStatus()).thenReturn(ActionStatus.COMPLETED);
    }

    @Test
    public void testGetActionsToBeTicked() {
        // Droplet setups
        when(droplet1.getStatus()).thenReturn(DropletStatus.AVAILABLE);
        when(droplet2.getStatus()).thenReturn(DropletStatus.AVAILABLE);

        // Action setups
        when(action1.getStatus()).thenReturn(ActionStatus.NOT_STARTED);
        when(action3.getStatus()).thenReturn(ActionStatus.NOT_STARTED);


        List<ActionBase> actionsToBeTicked = sut.getActionsToBeTicked();
        Assertions.assertEquals(2, actionsToBeTicked.size());
        Assertions.assertTrue(actionsToBeTicked.contains(action1));
        Assertions.assertTrue(actionsToBeTicked.contains(action3));
    }

    @Test
    public void GetActionsToBeTicked_UnavailableDropletAndNotStartedAction_ActionNotTicked() {
        // Droplet setups
        when(droplet1.getStatus()).thenReturn(DropletStatus.UNAVAILABLE);
        when(droplet2.getStatus()).thenReturn(DropletStatus.AVAILABLE);

        // Action setups
        when(action1.getStatus()).thenReturn(ActionStatus.NOT_STARTED);
        when(action3.getStatus()).thenReturn(ActionStatus.NOT_STARTED);

        List<ActionBase> actionsToBeTicked = sut.getActionsToBeTicked();

        Assertions.assertEquals(1, actionsToBeTicked.size());
        Assertions.assertTrue(actionsToBeTicked.contains(action3));
    }

    @Test
    public void GetActionsToBeTicked_UnavailableDropletAndStartedAction_ActionNotTicked() {
        // Droplet setups
        when(droplet1.getStatus()).thenReturn(DropletStatus.UNAVAILABLE);
        when(droplet2.getStatus()).thenReturn(DropletStatus.AVAILABLE);

        // Action setups
        when(action1.getStatus()).thenReturn(ActionStatus.IN_PROGRESS);
        when(action3.getStatus()).thenReturn(ActionStatus.NOT_STARTED);

        List<ActionBase> actionsToBeTicked = sut.getActionsToBeTicked();

        Assertions.assertEquals(2, actionsToBeTicked.size());
        Assertions.assertTrue(actionsToBeTicked.contains(action1));
        Assertions.assertTrue(actionsToBeTicked.contains(action3));
    }

    @Test
    public void GetActionsToBeTicked_ActionInScheduleIsCompleted_throw() {
        // Droplet setups
        when(droplet1.getStatus()).thenReturn(DropletStatus.UNAVAILABLE);
        when(droplet2.getStatus()).thenReturn(DropletStatus.AVAILABLE);

        // Action setups
        when(action1.getStatus()).thenReturn(ActionStatus.NOT_STARTED, ActionStatus.COMPLETED);
        when(action3.getStatus()).thenReturn(ActionStatus.NOT_STARTED);

        Assertions.assertThrows(IllegalStateException.class, () -> sut.getActionsToBeTicked());
    }

    @Test
    public void UpdateSchedule_NoActionsIsCompleted_NothingHappens() {
        // Droplet setups
        when(droplet1.getStatus()).thenReturn(DropletStatus.AVAILABLE);
        when(droplet2.getStatus()).thenReturn(DropletStatus.AVAILABLE);

        // Action setups
        when(action1.getStatus()).thenReturn(ActionStatus.NOT_STARTED);
        when(action3.getStatus()).thenReturn(ActionStatus.NOT_STARTED);


        List<ActionBase> actionsToBeTicked = sut.getActionsToBeTicked();
        Assertions.assertEquals(2, actionsToBeTicked.size());
        Assertions.assertTrue(actionsToBeTicked.contains(action1));
        Assertions.assertTrue(actionsToBeTicked.contains(action3));

        List<ActionBase> actionsToBeTicked2 = sut.getActionsToBeTicked();
        Assertions.assertEquals(2, actionsToBeTicked2.size());
        Assertions.assertTrue(actionsToBeTicked.contains(action1));
        Assertions.assertTrue(actionsToBeTicked.contains(action3));
    }

    @Test
    public void UpdateSchedule_ActionIsComplete_ActionIsRemoved() {
        // Droplet setups
        when(droplet1.getStatus()).thenReturn(DropletStatus.AVAILABLE);
        when(droplet2.getStatus()).thenReturn(DropletStatus.AVAILABLE);

        // Action setups
        when(action1.getStatus()).thenReturn(ActionStatus.NOT_STARTED);
        when(action3.getStatus()).thenReturn(ActionStatus.NOT_STARTED);

        List<ActionBase> actionsToBeTicked = sut.getActionsToBeTicked();
        Assertions.assertEquals(2, actionsToBeTicked.size());
        Assertions.assertTrue(actionsToBeTicked.contains(action1));
        Assertions.assertTrue(actionsToBeTicked.contains(action3));


        when(action1.getStatus()).thenReturn(ActionStatus.COMPLETED);
        List<ActionBase> actionsToBeTicked2 = sut.getActionsToBeTicked();
        Assertions.assertEquals(2, actionsToBeTicked2.size());
        Assertions.assertTrue(actionsToBeTicked2.contains(action2));
        Assertions.assertTrue(actionsToBeTicked2.contains(action3));
    }

    @Test void GetActionsToBeTicked_AllActionsForDropletIsCompleted_NoActionFromSaidDropletIsTicked() {
        // Droplet setups
        when(droplet1.getStatus()).thenReturn(DropletStatus.AVAILABLE);
        when(droplet2.getStatus()).thenReturn(DropletStatus.AVAILABLE);

        // Action setups
        when(action1.getStatus()).thenReturn(ActionStatus.COMPLETED);
        when(action2.getStatus()).thenReturn(ActionStatus.COMPLETED);
        when(action3.getStatus()).thenReturn(ActionStatus.NOT_STARTED);
        when(action4.getStatus()).thenReturn(ActionStatus.COMPLETED);

        // Updating (should remove action 1, 2 and 3)
        List<ActionBase> actionsToBeTicked = sut.getActionsToBeTicked();

        Assertions.assertEquals(1, actionsToBeTicked.size());
        Assertions.assertTrue(actionsToBeTicked.contains(action3));
    }

    @Test void GetActionsToBeTicked_2DropletsProduceSameAction_Error() {
        InputAction produce1 = mock(InputAction.class);
        when(produce1.dropletsProducedByExecution()).thenReturn(Set.of(droplet1));
        InputAction produce2 = mock(InputAction.class);
        when(produce2.dropletsProducedByExecution()).thenReturn(Set.of(droplet1));

        action1 = mock(ActionBase.class);
        action2 = mock(ActionBase.class);


        List<ActionBase> actions = new ArrayList<>(List.of(produce1, produce2));

        assertThrows(DmfInputReaderException.class, () -> new Schedule(actions));
    }
}
