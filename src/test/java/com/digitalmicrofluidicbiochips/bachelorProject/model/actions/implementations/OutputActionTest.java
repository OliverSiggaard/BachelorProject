package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations;

import com.digitalmicrofluidicbiochips.bachelorProject.errors.DmfException;
import com.digitalmicrofluidicbiochips.bachelorProject.executor.path_finding.IPathFinder;
import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.DropletStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.ElectrodeGrid;
import com.digitalmicrofluidicbiochips.bachelorProject.testUtils.MockElectrodeGridSetupUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OutputActionTest {
    private OutputAction sut;
    private Droplet droplet;

    private ProgramConfiguration programConfigurationMock;

    @BeforeEach
    void setUp() {
        sut = new OutputAction("id", 1, 2);

        droplet = new Droplet("droplet1", 1, 2, 1.0);
        sut.setDroplet(droplet);

        programConfigurationMock = mock(ProgramConfiguration.class);
        ElectrodeGrid electrodeGrid = MockElectrodeGridSetupUtil.createMockElectrodeGrid(32, 20);
        when(programConfigurationMock.getElectrodeGrid()).thenReturn(electrodeGrid);
    }

    @Test
    void testSettersAndGetters() {
        Assertions.assertNull(sut.getNextAction());
        ActionBase nextAction = mock(ActionBase.class);
        sut.setNextAction(nextAction);
        Assertions.assertEquals(nextAction, sut.getNextAction());

        Assertions.assertEquals(droplet, sut.getDroplet());
        Droplet droplet = mock(Droplet.class);
        sut.setDroplet(droplet);
        Assertions.assertEquals(droplet, sut.getDroplet());
    }

    @Test
    void testAffectedDroplets() {
        Assertions.assertEquals(1, sut.dropletsRequiredForExecution().size());
        Assertions.assertTrue(sut.dropletsRequiredForExecution().contains(droplet));
    }

    @Test
    public void testDropletsProducedByExecution() {
        Assertions.assertEquals(0, sut.dropletsProducedByExecution().size());
    }

    @Test
    void testBeforeExecution() {

        Assertions.assertEquals(ActionStatus.NOT_STARTED, sut.getStatus());
        Assertions.assertEquals(DropletStatus.NOT_CREATED, droplet.getStatus());
        sut.beforeExecution(mock(ProgramConfiguration.class));
        Assertions.assertEquals(ActionStatus.IN_PROGRESS, sut.getStatus());
        Assertions.assertEquals(DropletStatus.UNAVAILABLE, droplet.getStatus());
    }

    @Test
    void testExecution() {

        sut.beforeExecution(mock(ProgramConfiguration.class));

        Assertions.assertEquals(ActionStatus.IN_PROGRESS, sut.getStatus());
        Assertions.assertEquals(DropletStatus.UNAVAILABLE, droplet.getStatus());

        ProgramConfiguration programConfiguration = mock(ProgramConfiguration.class);

        sut.executeTick(programConfiguration);

        Assertions.assertEquals(ActionStatus.COMPLETED, sut.getStatus());
        Assertions.assertEquals(DropletStatus.CONSUMED, droplet.getStatus());
    }

    @Test
    void testExecutionDropletIsNotOnOutputPosition() {
        droplet.setPositionX(5);
        droplet.setPositionY(5);

        sut.beforeExecution(mock(ProgramConfiguration.class));

        Assertions.assertEquals(ActionStatus.IN_PROGRESS, sut.getStatus());
        Assertions.assertEquals(DropletStatus.UNAVAILABLE, droplet.getStatus());

        ProgramConfiguration programConfiguration = mock(ProgramConfiguration.class);

        // Verify that an IllegalStateException is thrown
        assertThrows(IllegalStateException.class, () -> sut.executeTick(programConfiguration));
    }

    @Test
    void testAfterExecution() {
        sut.beforeExecution(mock(ProgramConfiguration.class));
        sut.executeTick(mock(ProgramConfiguration.class));
        sut.afterExecution(mock(ProgramConfiguration.class));
        Assertions.assertEquals(DropletStatus.CONSUMED, droplet.getStatus());
        Assertions.assertEquals(ActionStatus.COMPLETED, sut.getStatus());
    }

    @Test
    public void testVerifyProperties_dropletNotSet() {
        OutputAction action = new OutputAction("id", 1, 2);
        Assertions.assertNull(action.getDroplet());
        Assertions.assertThrows(DmfException.class, () -> action.verifyProperties(programConfigurationMock));
    }

    @Test
    public void testVerifyProperties_posXOutOfBounds() {
        OutputAction action = new OutputAction("id", 33, 2);
        action.setDroplet(droplet);
        Assertions.assertThrows(DmfException.class, () -> action.verifyProperties(programConfigurationMock));
    }

    @Test
    public void testVerifyProperties_posYOutOfBounds() {
        OutputAction action = new OutputAction("id", 10, 21);
        action.setDroplet(droplet);
        Assertions.assertThrows(DmfException.class, () -> action.verifyProperties(programConfigurationMock));
    }

}
