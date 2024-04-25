package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations;

import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.DropletStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

public class StoreActionTest {

    private StoreAction sut;
    private Droplet droplet;

    @BeforeEach
    public void setUp() {
        sut =  new StoreAction("id", 1, 2, 3);

        droplet = new Droplet("droplet1", 1, 2, 1.0);
        sut.setDroplet(droplet);
    }

    @Test
    public void testAffectedDroplets() {
        Assertions.assertEquals(1, sut.dropletsRequiredForExecution().size());
        Assertions.assertTrue(sut.dropletsRequiredForExecution().contains(droplet));
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


}
