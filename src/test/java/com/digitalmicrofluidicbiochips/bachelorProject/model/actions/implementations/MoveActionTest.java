package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations;

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
import com.digitalmicrofluidicbiochips.bachelorProject.utils.DmfCommandUtils;
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
    public void testBeforeExecution() {
        Assertions.assertEquals(DropletStatus.AVAILABLE, droplet.getStatus());
        Assertions.assertEquals(ActionStatus.NOT_STARTED, sut.getStatus());
        sut.beforeExecution();
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
        sut.beforeExecution();

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
    public void testExecuteTickWithSingle1ElectrodeDropletDown() {
        droplet.setPositionX(5);
        droplet.setPositionY(3);
        sut.beforeExecution();

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
                DmfCommandUtils.getSetElectrodeCommand(expectedElectrode.getID()),
                (result.getTickCommands().get(0)).getDmfCommand());

        result = sut.executeTick(programConfigurationMock);
        Assertions.assertEquals(ActionStatus.IN_PROGRESS, sut.getStatus());
        Assertions.assertEquals(DropletMove.NONE, droplet.getDropletMove());
        Assertions.assertEquals(5, droplet.getPositionX());
        Assertions.assertEquals(4, droplet.getPositionY());
        Assertions.assertEquals(1, result.getTickCommands().size());
        expectedElectrode = programConfigurationMock.getElectrodeGrid().getElectrode(5, 3);
        Assertions.assertEquals(
                DmfCommandUtils.getClearElectrodeCommand(expectedElectrode.getID()),
                (result.getTickCommands().get(0)).getDmfCommand());

        result = sut.executeTick(programConfigurationMock);
        Assertions.assertEquals(ActionStatus.IN_PROGRESS, sut.getStatus());
        Assertions.assertEquals(DropletMove.DOWN, droplet.getDropletMove());
        Assertions.assertEquals(5, droplet.getPositionX());
        Assertions.assertEquals(4, droplet.getPositionY());
        Assertions.assertEquals(1, result.getTickCommands().size());
        expectedElectrode = programConfigurationMock.getElectrodeGrid().getElectrode(5, 5);
        Assertions.assertEquals(
                DmfCommandUtils.getSetElectrodeCommand(expectedElectrode.getID()),
                (result.getTickCommands().get(0)).getDmfCommand());

        result = sut.executeTick(programConfigurationMock);
        Assertions.assertEquals(ActionStatus.COMPLETED, sut.getStatus());
        Assertions.assertEquals(DropletMove.NONE, droplet.getDropletMove());
        Assertions.assertEquals(5, droplet.getPositionX());
        Assertions.assertEquals(5, droplet.getPositionY());
        Assertions.assertEquals(1, result.getTickCommands().size());
        expectedElectrode = programConfigurationMock.getElectrodeGrid().getElectrode(5, 4);
        Assertions.assertEquals(
                DmfCommandUtils.getClearElectrodeCommand(expectedElectrode.getID()),
                (result.getTickCommands().get(0)).getDmfCommand());
    }

    @Test
    public void testAfterExecution() {
        droplet.setPositionX(5);
        droplet.setPositionY(5);
        sut.beforeExecution();

        when(pathFinderMock.getMove(eq(droplet), any(ElectrodeGrid.class), eq(5), eq(5))).thenReturn(DropletMove.DOWN);
        ActionTickResult result = sut.executeTick(programConfigurationMock);
        Assertions.assertEquals(0, result.getTickCommands().size());
        Assertions.assertEquals(ActionStatus.COMPLETED, sut.getStatus());

        sut.afterExecution();
        Assertions.assertEquals(DropletStatus.AVAILABLE, droplet.getStatus());
        Assertions.assertEquals(ActionStatus.COMPLETED, sut.getStatus());
    }
}
