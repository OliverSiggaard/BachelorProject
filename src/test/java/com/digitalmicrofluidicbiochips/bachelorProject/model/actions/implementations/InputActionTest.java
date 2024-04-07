package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations;

import com.digitalmicrofluidicbiochips.bachelorProject.executor.path_finding.DropletMove;
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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InputActionTest {

    private InputAction sut;
    private Droplet droplet;
    private ProgramConfiguration programConfigurationMock;

    @BeforeEach
    public void setUp() {
        sut = new InputAction("id", 1, 2, 3.0);
        droplet = new Droplet("droplet1", 0,0, 1.0);
        sut.setDroplet(droplet);

        programConfigurationMock = mock(ProgramConfiguration.class);
        ElectrodeGrid electrodeGrid = MockElectrodeGridSetupUtil.createMockElectrodeGrid(32, 20);
        when(programConfigurationMock.getElectrodeGrid()).thenReturn(electrodeGrid);
    }

    // Constructor is tested in mappers. No need to test it again.

    @Test
    public void testAffectedDroplets() {
        Assertions.assertEquals(0, sut.dropletsRequiredForExecution().size());
        // Droplet is not required for execution. Droplet is created by the action.
    }

    @Test
    public void testBeforeExecution() {
        Assertions.assertEquals(DropletStatus.NOT_CREATED, droplet.getStatus());
        Assertions.assertEquals(ActionStatus.NOT_STARTED, sut.getStatus());
        sut.beforeExecution();
        Assertions.assertEquals(DropletStatus.UNAVAILABLE, droplet.getStatus());
        Assertions.assertEquals(ActionStatus.IN_PROGRESS, sut.getStatus());
    }

    @Test
    public void testExecuteTickWithSingle1ElectrodeDroplet() {
        Assertions.assertEquals(DropletStatus.NOT_CREATED, droplet.getStatus());
        Assertions.assertEquals(ActionStatus.NOT_STARTED, sut.getStatus());

        sut.beforeExecution();
        Assertions.assertEquals(DropletStatus.UNAVAILABLE, droplet.getStatus());
        Assertions.assertEquals(ActionStatus.IN_PROGRESS, sut.getStatus());

        ActionTickResult tickResult = sut.executeTick(programConfigurationMock);

        Assertions.assertEquals(1, droplet.getPositionX());
        Assertions.assertEquals(2, droplet.getPositionY());
        Assertions.assertEquals(droplet.getDropletMove(), DropletMove.NONE);

        Assertions.assertEquals(1, tickResult.getTickCommands().size());
        Assertions.assertTrue(tickResult.getTickCommands().get(0) instanceof SetElectrodeCommand);

        ElectrodeGrid electrodeGrid = programConfigurationMock.getElectrodeGrid();
        SetElectrodeCommand command = (SetElectrodeCommand) tickResult.getTickCommands().get(0);
        Electrode electrodeUnderDroplet = electrodeGrid.getElectrode(droplet.getPositionX(), droplet.getPositionY());
        Assertions.assertEquals(
                DmfCommandUtils.getSetElectrodeCommand(electrodeUnderDroplet.getID()),
                command.getDmfCommand());
    }

    @Test
    public void testExecuteTickWithDropletSpanning3x3() {

        droplet = new Droplet("droplet1", 0,0, 7.0);
        sut.setDroplet(droplet);

        Assertions.assertEquals(DropletStatus.NOT_CREATED, droplet.getStatus());
        Assertions.assertEquals(ActionStatus.NOT_STARTED, sut.getStatus());

        sut.beforeExecution();
        Assertions.assertEquals(DropletStatus.UNAVAILABLE, droplet.getStatus());
        Assertions.assertEquals(ActionStatus.IN_PROGRESS, sut.getStatus());

        ActionTickResult tickResult = sut.executeTick(programConfigurationMock);

        Assertions.assertEquals(1, droplet.getPositionX());
        Assertions.assertEquals(2, droplet.getPositionY());
        Assertions.assertEquals(droplet.getDropletMove(), DropletMove.NONE);

        Assertions.assertEquals(9, tickResult.getTickCommands().size());
        Assertions.assertTrue(tickResult.getTickCommands().get(0) instanceof SetElectrodeCommand);

        ElectrodeGrid electrodeGrid = programConfigurationMock.getElectrodeGrid();

        for (int dx = 0; dx < 3; dx++) {
            for (int dy = 0; dy < 3; dy++) {
                Electrode electrodeUnderDroplet = electrodeGrid.getElectrode(droplet.getPositionX() + dx, droplet.getPositionY() + dy);
                Assertions.assertEquals(
                        DmfCommandUtils.getSetElectrodeCommand(electrodeUnderDroplet.getID()),
                        tickResult.getTickCommands().get(dx * 3 + dy).getDmfCommand());
            }
        }
    }

    @Test
    public void testAfterExecution() {
        Assertions.assertEquals(DropletStatus.NOT_CREATED, droplet.getStatus());
        Assertions.assertEquals(ActionStatus.NOT_STARTED, sut.getStatus());

        sut.beforeExecution();
        Assertions.assertEquals(DropletStatus.UNAVAILABLE, droplet.getStatus());
        Assertions.assertEquals(ActionStatus.IN_PROGRESS, sut.getStatus());

        // Execute tick (should always run before afterExecution) - For input the action is completed in first tick.
        sut.executeTick(programConfigurationMock);

        sut.afterExecution();
        Assertions.assertEquals(DropletStatus.AVAILABLE, droplet.getStatus());
        Assertions.assertEquals(ActionStatus.COMPLETED, sut.getStatus());
    }
}
