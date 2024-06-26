package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations;

import com.digitalmicrofluidicbiochips.bachelorProject.errors.DmfException;
import com.digitalmicrofluidicbiochips.bachelorProject.executor.path_finding.DropletMove;
import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.ActionTickResult;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.SetElectrodeCommand;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.DropletStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Electrode;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.ElectrodeGrid;
import com.digitalmicrofluidicbiochips.bachelorProject.utils.DmfPlatformUtils;
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
    public void testDropletsProducedByExecution() {
        Assertions.assertEquals(1, sut.dropletsProducedByExecution().size());
        Assertions.assertTrue(sut.dropletsProducedByExecution().contains(droplet));
    }

    @Test
    public void testBeforeExecution() {
        Assertions.assertEquals(DropletStatus.NOT_CREATED, droplet.getStatus());
        Assertions.assertEquals(ActionStatus.NOT_STARTED, sut.getStatus());
        sut.beforeExecution(mock(ProgramConfiguration.class));
        Assertions.assertEquals(DropletStatus.NOT_CREATED, droplet.getStatus());
        Assertions.assertEquals(ActionStatus.IN_PROGRESS, sut.getStatus());
    }

    @Test
    public void testExecuteTickWithSingle1ElectrodeDroplet() {
        Assertions.assertEquals(DropletStatus.NOT_CREATED, droplet.getStatus());
        Assertions.assertEquals(ActionStatus.NOT_STARTED, sut.getStatus());

        sut.beforeExecution(mock(ProgramConfiguration.class));
        Assertions.assertEquals(DropletStatus.NOT_CREATED, droplet.getStatus());
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
                DmfPlatformUtils.getSetElectrodeCommand(electrodeUnderDroplet.getID()),
                command.getBioAssemblyInstruction());
    }

    @Test
    public void testExecuteTickWithDropletSpanning3x3() {
        sut = new InputAction("id", 1, 2, 15.0);
        droplet = new Droplet("droplet1", 0,0, 15.0);
        sut.setDroplet(droplet);

        Assertions.assertEquals(DropletStatus.NOT_CREATED, droplet.getStatus());
        Assertions.assertEquals(ActionStatus.NOT_STARTED, sut.getStatus());

        sut.beforeExecution(mock(ProgramConfiguration.class));
        Assertions.assertEquals(DropletStatus.NOT_CREATED, droplet.getStatus());
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
                        DmfPlatformUtils.getSetElectrodeCommand(electrodeUnderDroplet.getID()),
                        tickResult.getTickCommands().get(dx * 3 + dy).getBioAssemblyInstruction());
            }
        }
    }

    @Test
    public void testAfterExecution() {
        Assertions.assertEquals(DropletStatus.NOT_CREATED, droplet.getStatus());
        Assertions.assertEquals(ActionStatus.NOT_STARTED, sut.getStatus());

        sut.beforeExecution(mock(ProgramConfiguration.class));
        Assertions.assertEquals(DropletStatus.NOT_CREATED, droplet.getStatus());
        Assertions.assertEquals(ActionStatus.IN_PROGRESS, sut.getStatus());

        // Execute tick (should always run before afterExecution) - For input the action is completed in first tick.
        sut.executeTick(programConfigurationMock);

        sut.afterExecution(mock(ProgramConfiguration.class));
        Assertions.assertEquals(DropletStatus.AVAILABLE, droplet.getStatus());
        Assertions.assertEquals(ActionStatus.COMPLETED, sut.getStatus());
    }

    @Test
    public void testVerifyProperties_dropletNotSet() {
        InputAction inputAction = new InputAction("id", 1, 2, 3.0);
        Assertions.assertNull(inputAction.getDroplet());
        Assertions.assertThrows(DmfException.class, () -> inputAction.verifyProperties(programConfigurationMock));
    }

    @Test
    public void testVerifyProperties_posXOutOfBounds() {
        InputAction inputAction = new InputAction("id", 33, 2, 3.0);
        inputAction.setDroplet(droplet);
        Assertions.assertThrows(DmfException.class, () -> inputAction.verifyProperties(programConfigurationMock));
    }

    @Test
    public void testVerifyProperties_posYOutOfBounds() {
        InputAction inputAction = new InputAction("id", 10, 21, 3.0);
        inputAction.setDroplet(droplet);
        Assertions.assertThrows(DmfException.class, () -> inputAction.verifyProperties(programConfigurationMock));
    }

    @Test
    public void testVerifyProperties_volume0() {
        InputAction inputAction = new InputAction("id", 10, 10, 0);
        inputAction.setDroplet(droplet);
        Assertions.assertThrows(DmfException.class, () -> inputAction.verifyProperties(programConfigurationMock));
    }
}
