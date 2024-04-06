package com.digitalmicrofluidicbiochips.bachelorProject.compiler;

import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.DropletStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;


import java.util.*;

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

        action1 = mock(ActionBase.class);
        action2 = mock(ActionBase.class);
        action3 = mock(ActionBase.class);
        action4 = mock(ActionBase.class);

        when(action1.dropletsRequiredForExecution()).thenReturn(Set.of(droplet1));
        when(action2.dropletsRequiredForExecution()).thenReturn(Set.of(droplet1));
        when(action3.dropletsRequiredForExecution()).thenReturn(Set.of(droplet2));
        when(action4.dropletsRequiredForExecution()).thenReturn(Set.of(droplet1, droplet2));

        Map<Droplet, Queue<ActionBase>> dropletActions = new HashMap<>();
        dropletActions.put(droplet1, new LinkedList<>(List.of(action1, action2, action4)));
        dropletActions.put(droplet2, new LinkedList<>(List.of(action3, action4)));
        sut = new Schedule(dropletActions);
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
        when(action1.getStatus()).thenReturn(ActionStatus.COMPLETED);
        when(action3.getStatus()).thenReturn(ActionStatus.NOT_STARTED);

        Assertions.assertThrows(IllegalStateException.class, () -> {
            sut.getActionsToBeTicked();
        });
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

        sut.updateSchedule();

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
        when(action1.getStatus()).thenReturn(ActionStatus.NOT_STARTED, ActionStatus.COMPLETED);
        when(action3.getStatus()).thenReturn(ActionStatus.NOT_STARTED);

        List<ActionBase> actionsToBeTicked = sut.getActionsToBeTicked();
        Assertions.assertEquals(2, actionsToBeTicked.size());
        Assertions.assertTrue(actionsToBeTicked.contains(action1));
        Assertions.assertTrue(actionsToBeTicked.contains(action3));

        sut.updateSchedule();

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
        sut.updateSchedule();
        List<ActionBase> actionsToBeTicked = sut.getActionsToBeTicked();

        Assertions.assertEquals(1, actionsToBeTicked.size());
        Assertions.assertTrue(actionsToBeTicked.contains(action3));
    }

}
