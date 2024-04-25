package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations;

import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.ActionTickResult;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;

public class ActionQueueTest {

    private ActionQueue sut;

    private ActionBase action1 = mock(ActionBase.class);
    private ActionBase action2 = mock(ActionBase.class);
    private ActionBase action3 = mock(ActionBase.class);

    private Droplet droplet1 = mock(Droplet.class);
    private Droplet droplet2 = mock(Droplet.class);
    private Droplet droplet3 = mock(Droplet.class);
    private Droplet droplet4 = mock(Droplet.class);

    @BeforeEach
    public void setUp() {
        when(action1.dropletsRequiredForExecution()).thenReturn(Set.of(droplet1, droplet2));
        when(action2.dropletsRequiredForExecution()).thenReturn(Set.of(droplet3, droplet2));
        when(action3.dropletsRequiredForExecution()).thenReturn(Set.of(droplet4));

        List<ActionBase> actions = new ArrayList<>(List.of(action1, action2, action3));
        sut = new ActionQueue("id", actions);
    }

    @Test
    public void testDropletsRequiredForExecution() {
        Assertions.assertEquals(4, sut.dropletsRequiredForExecution().size());
        Assertions.assertTrue(sut.dropletsRequiredForExecution().contains(droplet1));
        Assertions.assertTrue(sut.dropletsRequiredForExecution().contains(droplet2));
        Assertions.assertTrue(sut.dropletsRequiredForExecution().contains(droplet3));
        Assertions.assertTrue(sut.dropletsRequiredForExecution().contains(droplet4));
    }

    @Test
    public void testBeforeExecution() {
        Assertions.assertEquals(ActionStatus.NOT_STARTED, sut.getStatus());
        sut.beforeExecution(mock(ProgramConfiguration.class));
        Assertions.assertEquals(ActionStatus.IN_PROGRESS, sut.getStatus());

        verifyNoMoreInteractions(droplet1);
        verifyNoMoreInteractions(droplet2);
        verifyNoMoreInteractions(droplet3);
        verifyNoMoreInteractions(droplet4);
    }

    @Test
    public void testExecuteTick() {

        ProgramConfiguration programConfiguration = mock(ProgramConfiguration.class);
        ActionTickResult actionTickResult1 = mock(ActionTickResult.class);
        ActionTickResult actionTickResult2 = mock(ActionTickResult.class);
        ActionTickResult actionTickResult3 = mock(ActionTickResult.class);
        when(action1.executeTick(programConfiguration)).thenReturn(actionTickResult1);
        when(action2.executeTick(programConfiguration)).thenReturn(actionTickResult2);
        when(action3.executeTick(programConfiguration)).thenReturn(actionTickResult3);

        sut.beforeExecution(programConfiguration);

        when(action1.getStatus())
                .thenReturn(ActionStatus.NOT_STARTED)
                .thenReturn(ActionStatus.COMPLETED);
        ActionTickResult result = sut.executeTick(programConfiguration);
        verify(action1).beforeExecution(programConfiguration);
        Assertions.assertEquals(actionTickResult1, result);

        when(action2.getStatus())
                .thenReturn(ActionStatus.NOT_STARTED)
                .thenReturn(ActionStatus.IN_PROGRESS);
        result = sut.executeTick(programConfiguration);
        verify(action2).beforeExecution(programConfiguration);
        Assertions.assertEquals(actionTickResult2, result);

        when(action2.getStatus())
                .thenReturn(ActionStatus.IN_PROGRESS)
                .thenReturn(ActionStatus.COMPLETED);
        result = sut.executeTick(programConfiguration);
        Assertions.assertEquals(actionTickResult2, result);

        when(action3.getStatus())
                .thenReturn(ActionStatus.NOT_STARTED)
                .thenReturn(ActionStatus.COMPLETED);
        result = sut.executeTick(programConfiguration);
        verify(action3).beforeExecution(programConfiguration);
        Assertions.assertEquals(actionTickResult3, result);
        Assertions.assertEquals(ActionStatus.COMPLETED, sut.getStatus());
    }

    @Test
    public void testExecuteTickWithFailingAction() {

        ProgramConfiguration programConfiguration = mock(ProgramConfiguration.class);
        ActionTickResult actionTickResult1 = mock(ActionTickResult.class);
        ActionTickResult actionTickResult2 = mock(ActionTickResult.class);
        ActionTickResult actionTickResult3 = mock(ActionTickResult.class);
        when(action1.executeTick(programConfiguration)).thenReturn(actionTickResult1);
        when(action2.executeTick(programConfiguration)).thenReturn(actionTickResult2);
        when(action3.executeTick(programConfiguration)).thenReturn(actionTickResult3);

        sut.beforeExecution(programConfiguration);

        when(action1.getStatus())
                .thenReturn(ActionStatus.NOT_STARTED)
                .thenReturn(ActionStatus.FAILED);
        ActionTickResult result = sut.executeTick(programConfiguration);
        verify(action1).beforeExecution(programConfiguration);
        Assertions.assertEquals(0, result.getTickCommands().size());
        Assertions.assertEquals(ActionStatus.FAILED, sut.getStatus());
    }

    @Test
    public void testAfterExecution() {
        sut.afterExecution(mock(ProgramConfiguration.class));
        verifyNoMoreInteractions(droplet1);
        verifyNoMoreInteractions(droplet2);
        verifyNoMoreInteractions(droplet3);
        verifyNoMoreInteractions(droplet4);
    }
}
