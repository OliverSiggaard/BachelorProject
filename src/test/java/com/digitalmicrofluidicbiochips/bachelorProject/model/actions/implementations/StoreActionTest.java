package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations;

import com.digitalmicrofluidicbiochips.bachelorProject.errors.DmfException;
import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.DropletStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.ElectrodeGrid;
import com.digitalmicrofluidicbiochips.bachelorProject.testUtils.MockElectrodeGridSetupUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StoreActionTest {

    private StoreAction sut;
    private Droplet droplet;

    private ProgramConfiguration programConfigurationMock;

    @BeforeEach
    public void setUp() {
        sut =  new StoreAction("id", 1, 2, 3);

        droplet = new Droplet("droplet1", 1, 2, 1.0);
        sut.setDroplet(droplet);

        programConfigurationMock = mock(ProgramConfiguration.class);
        ElectrodeGrid electrodeGrid = MockElectrodeGridSetupUtil.createMockElectrodeGrid(32, 20);
        when(programConfigurationMock.getElectrodeGrid()).thenReturn(electrodeGrid);
    }

    @Test
    public void testInitialTime() {
        Assertions.assertEquals(3, sut.getTicksToWait());
        Assertions.assertEquals(3, sut.getTicksLeft());
    }

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
        Assertions.assertEquals(ActionStatus.NOT_STARTED, sut.getStatus());
        Assertions.assertEquals(DropletStatus.NOT_CREATED, droplet.getStatus());
        sut.beforeExecution(mock(ProgramConfiguration.class));
        Assertions.assertEquals(ActionStatus.IN_PROGRESS, sut.getStatus());
        Assertions.assertEquals(DropletStatus.UNAVAILABLE, droplet.getStatus());
    }

    @Test
    public void testExecution() {
        sut.beforeExecution(mock(ProgramConfiguration.class));

        Assertions.assertEquals(ActionStatus.IN_PROGRESS, sut.getStatus());
        Assertions.assertEquals(DropletStatus.UNAVAILABLE, droplet.getStatus());

        ProgramConfiguration programConfigurationMock = mock(ProgramConfiguration.class);
        for (int i = 0; i < 2; i++) {
            sut.executeTick(programConfigurationMock);
            Assertions.assertEquals(ActionStatus.IN_PROGRESS, sut.getStatus());
            Assertions.assertEquals(DropletStatus.UNAVAILABLE, droplet.getStatus());
        }

        sut.executeTick(programConfigurationMock);
        Assertions.assertEquals(ActionStatus.COMPLETED, sut.getStatus());
        Assertions.assertEquals(DropletStatus.UNAVAILABLE, droplet.getStatus());
    }

    @Test
    public void testExecutionWithZeroTime() {
        sut = new StoreAction("id", 1, 2, 0);
        sut.setDroplet(droplet);
        sut.beforeExecution(mock(ProgramConfiguration.class));

        Assertions.assertEquals(ActionStatus.IN_PROGRESS, sut.getStatus());
        Assertions.assertEquals(DropletStatus.UNAVAILABLE, droplet.getStatus());

        ProgramConfiguration programConfigurationMock = mock(ProgramConfiguration.class);
        sut.executeTick(programConfigurationMock);
        Assertions.assertEquals(ActionStatus.COMPLETED, sut.getStatus());
        Assertions.assertEquals(DropletStatus.UNAVAILABLE, droplet.getStatus());
    }

    @Test
    public void testExecutionDropletIsNotOnStorePosition() {
        droplet.setPositionX(0);
        droplet.setPositionY(0);
        sut.setDroplet(droplet);
        sut.beforeExecution(mock(ProgramConfiguration.class));

        Assertions.assertEquals(ActionStatus.IN_PROGRESS, sut.getStatus());
        Assertions.assertEquals(DropletStatus.UNAVAILABLE, droplet.getStatus());

        ProgramConfiguration programConfigurationMock = mock(ProgramConfiguration.class);
        Assertions.assertThrows(IllegalStateException.class, () -> sut.executeTick(programConfigurationMock));
    }

    @Test
    public void testAfterExecution() {
        sut = new StoreAction("id", 1, 2, 0);
        sut.setDroplet(droplet);
        sut.beforeExecution(mock(ProgramConfiguration.class));

        Assertions.assertEquals(ActionStatus.IN_PROGRESS, sut.getStatus());
        Assertions.assertEquals(DropletStatus.UNAVAILABLE, droplet.getStatus());

        ProgramConfiguration programConfigurationMock = mock(ProgramConfiguration.class);
        sut.executeTick(programConfigurationMock);
        Assertions.assertEquals(ActionStatus.COMPLETED, sut.getStatus());
        Assertions.assertEquals(DropletStatus.UNAVAILABLE, droplet.getStatus());

        sut.afterExecution(mock(ProgramConfiguration.class));
        Assertions.assertEquals(DropletStatus.AVAILABLE, droplet.getStatus());
        Assertions.assertEquals(ActionStatus.COMPLETED, sut.getStatus());
    }

    @Test
    public void testNextAction() {
        Assertions.assertNull(sut.getNextAction());
        StoreAction nextAction = new StoreAction("id", 1, 2, 3);
        sut.setNextAction(nextAction);
        Assertions.assertEquals(nextAction, sut.getNextAction());
    }

    @Test
    public void testVerifyProperties_dropletNotSet() {
        sut.setDroplet(null);
        Assertions.assertNull(sut.getDroplet());
        Assertions.assertThrows(DmfException.class, () -> sut.verifyProperties(programConfigurationMock));
    }

    @Test
    public void testVerifyProperties_posXOutOfBounds() {
        sut = new StoreAction("id", 33, 2, 3);
        sut.setDroplet(droplet);
        Assertions.assertThrows(DmfException.class, () -> sut.verifyProperties(programConfigurationMock));
    }

    @Test
    public void testVerifyProperties_posYOutOfBounds() {
        sut = new StoreAction("id", 1, 33, 3);
        sut.setDroplet(droplet);
        Assertions.assertThrows(DmfException.class, () -> sut.verifyProperties(programConfigurationMock));
    }

    @Test
    public void testVerifyProperties_time0() {
        sut = new StoreAction("id", 1, 2, 0);
        sut.setDroplet(droplet);
        Assertions.assertThrows(DmfException.class, () -> sut.verifyProperties(programConfigurationMock));
    }


}
