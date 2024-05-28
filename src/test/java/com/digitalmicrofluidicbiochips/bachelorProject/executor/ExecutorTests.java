package com.digitalmicrofluidicbiochips.bachelorProject.executor;

import com.digitalmicrofluidicbiochips.bachelorProject.compiler.Schedule;
import com.digitalmicrofluidicbiochips.bachelorProject.errors.DmfExceptionMessage;
import com.digitalmicrofluidicbiochips.bachelorProject.executor.ExecutionResult;
import com.digitalmicrofluidicbiochips.bachelorProject.executor.Executor;
import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.ActionTickResult;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.ElectrodeGrid;
import com.digitalmicrofluidicbiochips.bachelorProject.testUtils.MockElectrodeGridSetupUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.Mockito.*;

public class ExecutorTests {

    private final String filePath = "src/test/resources/reader/simpleActionModel.JSON";
    private Executor sut;
    private ProgramConfiguration programConfiguration;
    private Schedule schedule;

    private Collection<Droplet> dropletsOnPlatform;

    private ElectrodeGrid electrodeGrid;

    @BeforeEach
    public void setUp() {
        dropletsOnPlatform = new ArrayList<>();
        schedule = mock(Schedule.class);
        electrodeGrid = MockElectrodeGridSetupUtil.createMockElectrodeGrid(32, 20);

        programConfiguration = mock(ProgramConfiguration.class);
        when(programConfiguration.getScheduleFromActions()).thenReturn(schedule);
        when(programConfiguration.getElectrodeGrid()).thenReturn(electrodeGrid);


        sut = new Executor(programConfiguration);
    }

    @Test
    public void testCompile_noActions_noErrors() {
        sut.compileProgramToDmf();
    }

    @Test
    public void testCompile_oneAction_noErrors() {
        ActionBase action = mock(ActionBase.class);
        when(action.getStatus()).thenReturn(ActionStatus.NOT_STARTED, ActionStatus.COMPLETED);
        ActionTickResult actionTickResult = new ActionTickResult();
        actionTickResult.setSomethingHappenedInTick(true);
        when(action.executeTick(programConfiguration)).thenReturn(actionTickResult);
        when(schedule.getActionsToBeTicked()).thenReturn(
                new ArrayList<>() {{
                    add(action);
                }},
                new ArrayList<>()
                );

        ExecutionResult executionResult = sut.compileProgramToDmf();
        verify(action, times(1)).beforeExecution(programConfiguration);
        verify(action, times(1)).executeTick(programConfiguration);
        verify(action, times(1)).afterExecution(programConfiguration);
    }

    @Test
    public void testCompile_nonRecoverableDeadlock() {
        ActionBase action = mock(ActionBase.class);
        when(action.getStatus()).thenReturn(ActionStatus.NOT_STARTED, ActionStatus.IN_PROGRESS);
        ActionTickResult actionTickResult = new ActionTickResult();
        when(action.executeTick(programConfiguration)).thenReturn(actionTickResult);
        when(action.executeTickAttemptToResolveDeadlock(programConfiguration)).thenReturn(actionTickResult);
        when(schedule.getActionsToBeTicked()).thenReturn(
                new ArrayList<>() {{
                    add(action);
                }}
        );

        ExecutionResult executionResult = sut.compileProgramToDmf();
        verify(action, times(1)).beforeExecution(programConfiguration);
        verify(action, times(1001)).executeTick(programConfiguration);
        verify(action, times(1001)).executeTickAttemptToResolveDeadlock(programConfiguration);

        Assertions.assertEquals(DmfExceptionMessage.PROGRAM_GOT_STUCK.getMessage(), executionResult.getErrorMessage());
    }

    @Test
    public void testCompile_recoverableDeadlock() {
        ActionBase action = mock(ActionBase.class);
        when(action.getStatus()).thenReturn(ActionStatus.NOT_STARTED, ActionStatus.IN_PROGRESS, ActionStatus.COMPLETED);
        ActionTickResult actionTickResult = new ActionTickResult();
        when(action.executeTick(programConfiguration)).thenReturn(actionTickResult);
        when(action.executeTickAttemptToResolveDeadlock(programConfiguration)).thenReturn(actionTickResult);
        when(schedule.getActionsToBeTicked()).thenReturn(
                new ArrayList<>() {{
                    add(action);
                }},
                new ArrayList<>() {{
                    add(action);
                }},
                new ArrayList<>() {{
                    add(action);
                }},
                new ArrayList<>()

        );

        ExecutionResult executionResult = sut.compileProgramToDmf();
        verify(action, times(1)).beforeExecution(programConfiguration);

        // ensure program terminates without error
        Assertions.assertNull(executionResult.getErrorMessage());
    }





}
