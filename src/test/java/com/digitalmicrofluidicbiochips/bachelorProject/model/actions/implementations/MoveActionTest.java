package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations;

import com.digitalmicrofluidicbiochips.bachelorProject.errors.DmfException;
import com.digitalmicrofluidicbiochips.bachelorProject.executor.path_finding.DropletMove;
import com.digitalmicrofluidicbiochips.bachelorProject.executor.path_finding.IPathFinder;
import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.ActionTickResult;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.SetElectrodeCommand;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.DropletStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Electrode;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.ElectrodeGrid;
import com.digitalmicrofluidicbiochips.bachelorProject.utils.DmfPlatformUtils;
import com.digitalmicrofluidicbiochips.bachelorProject.utils.DmfPlatformUtilsTest;
import com.digitalmicrofluidicbiochips.bachelorProject.testUtils.MockElectrodeGridSetupUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.mockito.Mockito.*;

public class MoveActionTest {

    private MoveAction sut;
    private Droplet droplet;
    private Droplet obstacleDroplet;
    private ProgramConfiguration programConfigurationMock;
    private IPathFinder pathFinderMock;

    @BeforeEach
    public void setUp() {
        sut = new MoveAction("id", 5, 5);
        droplet = new Droplet("droplet1", 0,0, 1.0);
        droplet.setStatus(DropletStatus.AVAILABLE);
        sut.setDroplet(droplet);

        obstacleDroplet = new Droplet("obstacleDroplet1", 10, 10, 1.0);
        obstacleDroplet.setStatus(DropletStatus.AVAILABLE);

        programConfigurationMock = mock(ProgramConfiguration.class);
        pathFinderMock = mock(IPathFinder.class);
        ElectrodeGrid electrodeGrid = MockElectrodeGridSetupUtil.createMockElectrodeGrid(32, 20);
        when(programConfigurationMock.getElectrodeGrid()).thenReturn(electrodeGrid);
        when(programConfigurationMock.getPathFinder()).thenReturn(pathFinderMock);
        when(programConfigurationMock.getDroplets()).thenReturn(List.of(new Droplet[]{droplet, obstacleDroplet}));
        when(programConfigurationMock.getDropletsOnDmfPlatform()).thenReturn(List.of(new Droplet[]{droplet, obstacleDroplet}));
    }

    // Constructor is tested in mappers. No need to test it again.

    @Test
    public void testAffectedDroplets() {
        Assertions.assertEquals(1, sut.dropletsRequiredForExecution().size());
        Assertions.assertTrue(sut.dropletsRequiredForExecution().contains(droplet));
    }

    @Test
    public void testDropletsProducedByExecution() {
        Assertions.assertEquals(0, sut.dropletsProducedByExecution().size());
    }

    @Test
    public void testBeforeExecution() {
        Assertions.assertEquals(DropletStatus.AVAILABLE, droplet.getStatus());
        Assertions.assertEquals(ActionStatus.NOT_STARTED, sut.getStatus());
        sut.beforeExecution(mock(ProgramConfiguration.class));
        Assertions.assertEquals(DropletStatus.UNAVAILABLE, droplet.getStatus());
        Assertions.assertEquals(ActionStatus.IN_PROGRESS, sut.getStatus());
    }

    /*
    public interface IPathFinder {
    DropletMove getMove(Droplet activeDroplet, ElectrodeGrid availableGrid, int goalX, int goalY);
}
     */

    @Test
    public void testExecuteTick_isCorrectAvailableGridParsed() {
        droplet.setPositionX(5);
        droplet.setPositionY(3);
        sut.beforeExecution(mock(ProgramConfiguration.class));

        ActionTickResult result = sut.executeTick(programConfigurationMock);
        ArgumentCaptor<ElectrodeGrid> electrodeGridCaptor = ArgumentCaptor.forClass(ElectrodeGrid.class);
        verify(pathFinderMock).getMove(eq(droplet), electrodeGridCaptor.capture(), eq(5), eq(5));
        ElectrodeGrid capturedElectrodeGrid = electrodeGridCaptor.getValue();

        ElectrodeGrid expectedElectrodeGrid = programConfigurationMock.getElectrodeGrid().clone();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                expectedElectrodeGrid.removeElectrode(obstacleDroplet.getPositionX() + dx, obstacleDroplet.getPositionY() + dy);
            }
        }

        Assertions.assertEquals(expectedElectrodeGrid.getXSize(), capturedElectrodeGrid.getXSize());
        Assertions.assertEquals(expectedElectrodeGrid.getYSize(), capturedElectrodeGrid.getYSize());
        for (int dx = 0; dx < expectedElectrodeGrid.getXSize(); dx++) {
            for (int dy = 0; dy < expectedElectrodeGrid.getYSize(); dy++) {
                Assertions.assertEquals(expectedElectrodeGrid.getElectrode(dx, dy), capturedElectrodeGrid.getElectrode(dx, dy));
            }
        }
    }

    @Test
    public void testExecuteTick_isCorrectAvailableGridParsed_obstacleDropletIsExempt() {
        droplet.setPositionX(5);
        droplet.setPositionY(3);
        sut.beforeExecution(mock(ProgramConfiguration.class));
        sut.addExemptObstacleDroplet(obstacleDroplet);

        ActionTickResult result = sut.executeTick(programConfigurationMock);
        ArgumentCaptor<ElectrodeGrid> electrodeGridCaptor = ArgumentCaptor.forClass(ElectrodeGrid.class);
        verify(pathFinderMock).getMove(eq(droplet), electrodeGridCaptor.capture(), eq(5), eq(5));
        ElectrodeGrid capturedElectrodeGrid = electrodeGridCaptor.getValue();

        ElectrodeGrid expectedElectrodeGrid = programConfigurationMock.getElectrodeGrid().clone();

        Assertions.assertEquals(expectedElectrodeGrid.getXSize(), capturedElectrodeGrid.getXSize());
        Assertions.assertEquals(expectedElectrodeGrid.getYSize(), capturedElectrodeGrid.getYSize());
        for (int dx = 0; dx < expectedElectrodeGrid.getXSize(); dx++) {
            for (int dy = 0; dy < expectedElectrodeGrid.getYSize(); dy++) {
                Assertions.assertEquals(expectedElectrodeGrid.getElectrode(dx, dy), capturedElectrodeGrid.getElectrode(dx, dy));
            }
        }
    }

    @Test
    public void testExecuteTickWithSingle1ElectrodeDropletDown() {
        droplet.setPositionX(5);
        droplet.setPositionY(3);
        sut.beforeExecution(mock(ProgramConfiguration.class));

        when(pathFinderMock.getMove(eq(droplet), any(ElectrodeGrid.class), eq(5), eq(5))).thenReturn(DropletMove.DOWN);

        ActionTickResult result = sut.executeTick(programConfigurationMock);
        Assertions.assertEquals(ActionStatus.IN_PROGRESS, sut.getStatus());
        Assertions.assertEquals(DropletMove.DOWN, droplet.getDropletMove());
        Assertions.assertEquals(5, droplet.getPositionX());
        Assertions.assertEquals(3, droplet.getPositionY());
        Assertions.assertEquals(1, result.getTickCommands().size());
        Assertions.assertTrue(result.getTickCommands().get(0) instanceof SetElectrodeCommand);
        Electrode expectedElectrode = programConfigurationMock.getElectrodeGrid().getElectrode(5, 4);
        Assertions.assertEquals(
                DmfPlatformUtils.getSetElectrodeCommand(expectedElectrode.getID()),
                (result.getTickCommands().get(0)).getDmfCommand());

        result = sut.executeTick(programConfigurationMock);
        Assertions.assertEquals(ActionStatus.IN_PROGRESS, sut.getStatus());
        Assertions.assertEquals(DropletMove.NONE, droplet.getDropletMove());
        Assertions.assertEquals(5, droplet.getPositionX());
        Assertions.assertEquals(4, droplet.getPositionY());
        Assertions.assertEquals(1, result.getTickCommands().size());
        expectedElectrode = programConfigurationMock.getElectrodeGrid().getElectrode(5, 3);
        Assertions.assertEquals(
                DmfPlatformUtils.getClearElectrodeCommand(expectedElectrode.getID()),
                (result.getTickCommands().get(0)).getDmfCommand());

        result = sut.executeTick(programConfigurationMock);
        Assertions.assertEquals(ActionStatus.IN_PROGRESS, sut.getStatus());
        Assertions.assertEquals(DropletMove.DOWN, droplet.getDropletMove());
        Assertions.assertEquals(5, droplet.getPositionX());
        Assertions.assertEquals(4, droplet.getPositionY());
        Assertions.assertEquals(1, result.getTickCommands().size());
        expectedElectrode = programConfigurationMock.getElectrodeGrid().getElectrode(5, 5);
        Assertions.assertEquals(
                DmfPlatformUtils.getSetElectrodeCommand(expectedElectrode.getID()),
                (result.getTickCommands().get(0)).getDmfCommand());

        result = sut.executeTick(programConfigurationMock);
        Assertions.assertEquals(ActionStatus.COMPLETED, sut.getStatus());
        Assertions.assertEquals(DropletMove.NONE, droplet.getDropletMove());
        Assertions.assertEquals(5, droplet.getPositionX());
        Assertions.assertEquals(5, droplet.getPositionY());
        Assertions.assertEquals(1, result.getTickCommands().size());
        expectedElectrode = programConfigurationMock.getElectrodeGrid().getElectrode(5, 4);
        Assertions.assertEquals(
                DmfPlatformUtils.getClearElectrodeCommand(expectedElectrode.getID()),
                (result.getTickCommands().get(0)).getDmfCommand());
    }

    @Test
    void testAttemptToResolveDeadlock() {
        Droplet droplet = new Droplet("1", 5, 10, 7);
        Droplet obstacleDroplet = new Droplet("1", 9, 10, 7);

        when(pathFinderMock.getMove(any(Droplet.class), any(ElectrodeGrid.class), eq(9), eq(10))).thenReturn(DropletMove.BLOCKED);
        when(pathFinderMock.getMove(any(Droplet.class), any(ElectrodeGrid.class), eq(6), eq(10))).thenReturn(DropletMove.RIGHT);
        when(pathFinderMock.getMove(any(Droplet.class), any(ElectrodeGrid.class), eq(4), eq(10))).thenReturn(DropletMove.LEFT);
        when(pathFinderMock.getMove(any(Droplet.class), any(ElectrodeGrid.class), eq(5), eq(11))).thenReturn(DropletMove.DOWN);
        when(pathFinderMock.getMove(any(Droplet.class), any(ElectrodeGrid.class), eq(5), eq(9))).thenReturn(DropletMove.UP);
        when(programConfigurationMock.getDroplets()).thenReturn(List.of(new Droplet[]{droplet, obstacleDroplet}));
        when(programConfigurationMock.getDropletsOnDmfPlatform()).thenReturn(List.of(new Droplet[]{droplet, obstacleDroplet}));

        MoveAction moveAction = new MoveAction("action", 9, 10);
        moveAction.setDroplet(droplet);

        ActionTickResult actionTickResult = moveAction.executeTick(programConfigurationMock);
        Assertions.assertFalse(actionTickResult.somethingHappenedInTick());

        actionTickResult = moveAction.executeTickAttemptToResolveDeadlock(programConfigurationMock);
        Assertions.assertTrue(actionTickResult.somethingHappenedInTick());
    }

    @Test
    public void testAfterExecution() {
        droplet.setPositionX(5);
        droplet.setPositionY(5);
        sut.beforeExecution(mock(ProgramConfiguration.class));

        when(pathFinderMock.getMove(eq(droplet), any(ElectrodeGrid.class), eq(5), eq(5))).thenReturn(DropletMove.DOWN);
        ActionTickResult result = sut.executeTick(programConfigurationMock);
        Assertions.assertEquals(0, result.getTickCommands().size());
        Assertions.assertEquals(ActionStatus.COMPLETED, sut.getStatus());

        sut.afterExecution(mock(ProgramConfiguration.class));
        Assertions.assertEquals(DropletStatus.AVAILABLE, droplet.getStatus());
        Assertions.assertEquals(ActionStatus.COMPLETED, sut.getStatus());
    }

    @Test
    public void testVerifyProperties_dropletNotSet() {
        MoveAction action = new MoveAction("id", 1, 2);
        Assertions.assertNull(action.getDroplet());
        Assertions.assertThrows(DmfException.class, () -> action.verifyProperties(programConfigurationMock));
    }

    @Test
    public void testVerifyProperties_posXOutOfBounds() {
        MoveAction action = new MoveAction("id", 33, 2);
        action.setDroplet(droplet);
        Assertions.assertThrows(DmfException.class, () -> action.verifyProperties(programConfigurationMock));
    }

    @Test
    public void testVerifyProperties_posYOutOfBounds() {
        MoveAction action = new MoveAction("id", 10, 21);
        action.setDroplet(droplet);
        Assertions.assertThrows(DmfException.class, () -> action.verifyProperties(programConfigurationMock));
    }
}
