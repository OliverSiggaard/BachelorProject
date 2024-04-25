package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations;

import com.digitalmicrofluidicbiochips.bachelorProject.executor.path_finding.AStar;
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

public class MergeActionTest {

    private ProgramConfiguration programConfiguration;
    private ElectrodeGrid electrodeGrid;
    private MergeAction sut;
    private Droplet droplet1;
    private Droplet droplet2;
    private Droplet resultDroplet;

    @BeforeEach
    public void setUp() {
        programConfiguration = mock(ProgramConfiguration.class);
        electrodeGrid = MockElectrodeGridSetupUtil.createMockElectrodeGrid(32,20);
        when(programConfiguration.getElectrodeGrid()).thenReturn(electrodeGrid);
        AStar aStar = new AStar(false);
        when(programConfiguration.getPathFinder()).thenReturn(aStar);

        int posX = 10;
        int posY = 10;

        sut = new MergeAction("id", posX, posY);

        droplet1 = new Droplet("id", 5, 10, 10);
        droplet2 = new Droplet("id", 15, 10, 10);
        resultDroplet = new Droplet("id", posX, posY, 0);

        sut.setDroplet1(droplet1);
        sut.setDroplet2(droplet2);
        sut.setResultDroplet(resultDroplet);
    }

    @Test
    public void testBeforeExecutionResultDropletAlreadyCreated() {
        // Arrange
        resultDroplet.setStatus(DropletStatus.UNAVAILABLE);

        Assertions.assertThrows(IllegalStateException.class, () -> {
            // Act
            sut.beforeExecution(null);
        });
    }

    @Test
    public void testDropletsProducedByExecution() {
        Assertions.assertEquals(1, sut.dropletsProducedByExecution().size());
        Assertions.assertTrue(sut.dropletsProducedByExecution().contains(resultDroplet));
    }

    @Test
    public void testBeforeExecutionResult() {
        // Arrange
        resultDroplet.setStatus(DropletStatus.NOT_CREATED);

        Assertions.assertEquals(ActionStatus.NOT_STARTED, sut.getStatus());
        Assertions.assertEquals(ActionStatus.NOT_STARTED, sut.getMoveAction1().getStatus());
        Assertions.assertEquals(ActionStatus.NOT_STARTED, sut.getMoveAction2().getStatus());
        Assertions.assertEquals(null, sut.getInputAction());

        sut.beforeExecution(programConfiguration);

        Assertions.assertEquals(ActionStatus.IN_PROGRESS, sut.getStatus());
        Assertions.assertEquals(ActionStatus.IN_PROGRESS, sut.getMoveAction1().getStatus());
        Assertions.assertEquals(ActionStatus.IN_PROGRESS, sut.getMoveAction2().getStatus());
        Assertions.assertEquals(ActionStatus.NOT_STARTED, sut.getInputAction().getStatus());
        Assertions.assertEquals(droplet1.getVolume() + droplet2.getVolume()
                ,sut.getResultDroplet().getVolume());
        Assertions.assertEquals(sut.getPosX(), sut.getInputAction().getPosX());
        Assertions.assertEquals(sut.getPosY(), sut.getInputAction().getPosY());
        Assertions.assertEquals(droplet1.getVolume() + droplet2.getVolume(), sut.getInputAction().getDroplet().getVolume());
    }

    @Test
    public void testExecuteTick() {

        sut.beforeExecution(programConfiguration);

        Assertions.assertFalse(sut.isResultDropletInserted());

        // Ensure it completes at some point
        for(int i = 0; i < 1000; i++) { // it should complete way before 1000 ticks..
            if(sut.getStatus() == ActionStatus.COMPLETED) {
                break;
            }
            sut.executeTick(programConfiguration);
        }

        Assertions.assertTrue(sut.isResultDropletInserted());
        Assertions.assertEquals(ActionStatus.COMPLETED, sut.getStatus());
        Assertions.assertEquals(ActionStatus.COMPLETED, sut.getMoveAction1().getStatus());
        Assertions.assertEquals(ActionStatus.COMPLETED, sut.getMoveAction2().getStatus());
        Assertions.assertEquals(ActionStatus.COMPLETED, sut.getInputAction().getStatus());
        Assertions.assertEquals(droplet1.getVolume() + droplet2.getVolume()
                ,sut.getResultDroplet().getVolume());
        Assertions.assertEquals(sut.getPosX(), sut.getResultDroplet().getPositionX());
        Assertions.assertEquals(sut.getPosX(), sut.getResultDroplet().getPositionX());
        Assertions.assertEquals(sut.getPosY(), sut.getInputAction().getPosY());
        Assertions.assertEquals(droplet1.getVolume() + droplet2.getVolume(), sut.getInputAction().getDroplet().getVolume());

        Assertions.assertNotSame(sut.getResultDroplet().getStatus(), DropletStatus.AVAILABLE);
        Assertions.assertNotSame(sut.getDroplet1().getStatus(), DropletStatus.AVAILABLE);
        Assertions.assertNotSame(sut.getDroplet2().getStatus(), DropletStatus.AVAILABLE);
    }


    @Test
    public void testAfterExecution() {
        // Arrange
        sut.beforeExecution(programConfiguration);

        // Ensure it completes at some point
        for(int i = 0; i < 1000; i++) { // it should complete way before 1000 ticks..
            if(sut.getStatus() == ActionStatus.COMPLETED) {
                break;
            }
            sut.executeTick(programConfiguration);
        }

        // Act
        sut.afterExecution(programConfiguration);

        // Assert
        Assertions.assertEquals(DropletStatus.CONSUMED, droplet1.getStatus());
        Assertions.assertEquals(DropletStatus.CONSUMED, droplet2.getStatus());
        Assertions.assertEquals(DropletStatus.AVAILABLE, resultDroplet.getStatus());
    }

    @Test
    public void testDropletsRequiredForExecution() {
        // Arrange
        sut.setDroplet1(droplet1);
        sut.setDroplet2(droplet2);
        sut.setResultDroplet(resultDroplet);

        // Act
        var result = sut.dropletsRequiredForExecution();

        // Assert
        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.contains(droplet1));
        Assertions.assertTrue(result.contains(droplet2));
    }

}
