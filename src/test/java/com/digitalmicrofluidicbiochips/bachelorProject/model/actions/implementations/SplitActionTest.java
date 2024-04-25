package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations;

import com.digitalmicrofluidicbiochips.bachelorProject.executor.path_finding.AStar;
import com.digitalmicrofluidicbiochips.bachelorProject.executor.path_finding.DropletMove;
import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.DropletStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.ElectrodeGrid;
import com.digitalmicrofluidicbiochips.bachelorProject.testUtils.MockElectrodeGridSetupUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SplitActionTest {

    private ProgramConfiguration programConfiguration;
    private ElectrodeGrid electrodeGrid;
    private SplitAction sut;
    private Droplet originDroplet, resultDroplet1, resultDroplet2;

    private int posX, posY, posX1, posY1, posX2, posY2;
    @BeforeEach
    public void setUp() {

        posX = 14;
        posY = 8;
        posX1 = 10;
        posY1 = 15;
        posX2 = 18;
        posY2 = 15;

        originDroplet = new Droplet("originDroplet", posX, posY, 8);
        resultDroplet1 = new Droplet("resultDroplet1", posX1, posY1, 0);
        resultDroplet2 = new Droplet("resultDroplet2", posX2, posY2, 0);

        sut = new SplitAction("id", posX1, posY1, posX2, posY2);
        sut.setOriginDroplet(originDroplet);
        sut.setResultDroplet1(resultDroplet1);
        sut.setResultDroplet2(resultDroplet2);

        programConfiguration = mock(ProgramConfiguration.class);
        electrodeGrid = MockElectrodeGridSetupUtil.createMockElectrodeGrid(32,20);
        when(programConfiguration.getElectrodeGrid()).thenReturn(electrodeGrid);
        AStar aStar = new AStar(false);
        when(programConfiguration.getPathFinder()).thenReturn(aStar);

        Collection<Droplet> dropletsOnPlatform = List.of(resultDroplet1, resultDroplet2);
        when(programConfiguration.getDropletsOnDmfPlatform()).thenReturn(dropletsOnPlatform);
    }

    @Test
    public void testDropletsRequiredForExecution() {
        // Arrange
        Assertions.assertEquals(1, sut.dropletsRequiredForExecution().size());
        Assertions.assertTrue(sut.dropletsRequiredForExecution().contains(originDroplet));
    }

    @Test
    public void testBeforeExecutionResultDropletAlreadyCreated() {
        // Arrange
        resultDroplet1.setStatus(DropletStatus.UNAVAILABLE);
        Assertions.assertThrows(IllegalStateException.class, () -> {
            // Act
            sut.beforeExecution(programConfiguration);
        });

        setUp();
        resultDroplet2.setStatus(DropletStatus.UNAVAILABLE);
        Assertions.assertThrows(IllegalStateException.class, () -> {
            // Act
            sut.beforeExecution(programConfiguration);
        });
    }

    @Test
    public void testBeforeExecution_OriginDropletIsInAreaThatDoesNotAllowForSplit() {
        // Top left corner
        for (int x = 0; x <= 2; x++) {
            for (int y = 0; y <= 2; y++) {
                // Reset
                setUp();

                // Arrange
                originDroplet.setPositionX(x);
                originDroplet.setPositionY(y);


                Assertions.assertThrows(IllegalStateException.class, () -> {
                    sut.beforeExecution(programConfiguration);
                });
            }
        }

        // Top right corner
        for (int x = electrodeGrid.getXSize() - 3; x < electrodeGrid.getXSize(); x++) {
            for (int y = 0; y <= 2; y++) {
                // Reset
                setUp();

                // Arrange
                originDroplet.setPositionX(x);
                originDroplet.setPositionY(y);


                Assertions.assertThrows(IllegalStateException.class, () -> {
                    sut.beforeExecution(programConfiguration);
                });
            }
        }

        // Bottom left corner
        for (int x = 0; x <= 2; x++) {
            for (int y = electrodeGrid.getYSize() - 3; y < electrodeGrid.getYSize(); y++) {
                // Reset
                setUp();

                // Arrange
                originDroplet.setPositionX(x);
                originDroplet.setPositionY(y);


                Assertions.assertThrows(IllegalStateException.class, () -> {
                    sut.beforeExecution(programConfiguration);
                });
            }
        }

        // Bottom right corner
        for (int x = electrodeGrid.getXSize() - 3; x < electrodeGrid.getXSize(); x++) {
            for (int y = electrodeGrid.getYSize() - 3; y < electrodeGrid.getYSize(); y++) {
                // Reset
                setUp();

                // Arrange
                originDroplet.setPositionX(x);
                originDroplet.setPositionY(y);


                Assertions.assertThrows(IllegalStateException.class, () -> {
                    sut.beforeExecution(programConfiguration);
                });
            }
        }
    }


    @Test
    public void testBeforeExecutionResult() {
        // Arrange
        originDroplet.setStatus(DropletStatus.AVAILABLE);
        resultDroplet1.setStatus(DropletStatus.NOT_CREATED);
        resultDroplet2.setStatus(DropletStatus.NOT_CREATED);

        Assertions.assertEquals(DropletStatus.AVAILABLE, originDroplet.getStatus());
        Assertions.assertEquals(DropletStatus.NOT_CREATED, resultDroplet1.getStatus());
        Assertions.assertEquals(DropletStatus.NOT_CREATED, resultDroplet2.getStatus());
        Assertions.assertEquals(ActionStatus.NOT_STARTED, sut.getStatus());
        Assertions.assertNotEquals(originDroplet.getVolume() / 2, resultDroplet1.getVolume());
        Assertions.assertNotEquals(originDroplet.getVolume() / 2, resultDroplet1.getVolume());
        Assertions.assertEquals(0, sut.getTickQueue().size());
        Assertions.assertNull(sut.getMoveAction1());
        Assertions.assertNull(sut.getMoveAction2());

        sut.beforeExecution(programConfiguration);

        Assertions.assertEquals(DropletStatus.UNAVAILABLE, originDroplet.getStatus());
        Assertions.assertEquals(DropletStatus.NOT_CREATED, resultDroplet1.getStatus());
        Assertions.assertEquals(DropletStatus.NOT_CREATED, resultDroplet2.getStatus());
        Assertions.assertEquals(ActionStatus.IN_PROGRESS, sut.getStatus());
        Assertions.assertEquals(originDroplet.getVolume() / 2, resultDroplet1.getVolume());
        Assertions.assertEquals(originDroplet.getVolume() / 2, resultDroplet1.getVolume());
        Assertions.assertEquals(0, sut.getTickQueue().size());
        Assertions.assertEquals(posX1, sut.getMoveAction1().getPosX());
        Assertions.assertEquals(posY1, sut.getMoveAction1().getPosY());
        Assertions.assertEquals(resultDroplet1, sut.getMoveAction1().getDroplet());
        Assertions.assertEquals(posX2, sut.getMoveAction2().getPosX());
        Assertions.assertEquals(posY2, sut.getMoveAction2().getPosY());
        Assertions.assertEquals(resultDroplet2, sut.getMoveAction2().getDroplet());
    }

    @Test
    public void testExecuteTick() {
        // Arrange
        originDroplet.setStatus(DropletStatus.AVAILABLE);
        resultDroplet1.setStatus(DropletStatus.NOT_CREATED);
        resultDroplet2.setStatus(DropletStatus.NOT_CREATED);
        sut.beforeExecution(programConfiguration);

        // Ensure it completes at some point
        for(int i = 0; i < 1000; i++) { // it should complete way before 1000 ticks..
            if(sut.getStatus() == ActionStatus.COMPLETED) {
                break;
            }
            sut.executeTick(programConfiguration);
        }

        // Assert
        Assertions.assertEquals(DropletStatus.CONSUMED, originDroplet.getStatus());
        Assertions.assertEquals(posX1, resultDroplet1.getPositionX());
        Assertions.assertEquals(posY1, resultDroplet1.getPositionY());
        Assertions.assertEquals(posX2, resultDroplet2.getPositionX());
        Assertions.assertEquals(posY2, resultDroplet2.getPositionY());
    }

    @Test
    public void testExecuteTick_ObstacleDropletToTheLeftOfOriginDroplet() {
        Droplet obstacleDroplet = new Droplet("ObstacleDroplet", posX - 3, posY, 4);
        obstacleDroplet.setStatus(DropletStatus.UNAVAILABLE);

        Collection<Droplet> dropletsOnPlatform = List.of(originDroplet, obstacleDroplet, resultDroplet1, resultDroplet2);
        when(programConfiguration.getDropletsOnDmfPlatform()).thenReturn(dropletsOnPlatform);

        // Arrange
        originDroplet.setStatus(DropletStatus.AVAILABLE);
        resultDroplet1.setStatus(DropletStatus.NOT_CREATED);
        resultDroplet2.setStatus(DropletStatus.NOT_CREATED);
        sut.beforeExecution(programConfiguration);

        // Ensure it completes at some point
        for(int i = 0; i < 1000; i++) { // it should complete way before 1000 ticks..
            if(sut.getStatus() == ActionStatus.COMPLETED) {
                break;
            }
            sut.executeTick(programConfiguration);
        }

        Assertions.assertEquals(ActionStatus.COMPLETED, sut.getStatus());

        // Assert
        Assertions.assertEquals(DropletStatus.CONSUMED, originDroplet.getStatus());
        Assertions.assertEquals(posX1, resultDroplet1.getPositionX());
        Assertions.assertEquals(posY1, resultDroplet1.getPositionY());
        Assertions.assertEquals(posX2, resultDroplet2.getPositionX());
        Assertions.assertEquals(posY2, resultDroplet2.getPositionY());
    }

    @Test
    public void testExecuteTick_againstLeftBorder() {

        originDroplet.setPositionX(2);
        originDroplet.setPositionY(10);

        // Arrange
        originDroplet.setStatus(DropletStatus.AVAILABLE);
        resultDroplet1.setStatus(DropletStatus.NOT_CREATED);
        resultDroplet2.setStatus(DropletStatus.NOT_CREATED);
        sut.beforeExecution(programConfiguration);

        // Ensure it completes at some point
        for(int i = 0; i < 1000; i++) { // it should complete way before 1000 ticks..
            if(sut.getStatus() == ActionStatus.COMPLETED) {
                break;
            }
            sut.executeTick(programConfiguration);
        }

        Assertions.assertEquals(ActionStatus.COMPLETED, sut.getStatus());

        // Assert
        Assertions.assertEquals(DropletStatus.CONSUMED, originDroplet.getStatus());
        Assertions.assertEquals(posX1, resultDroplet1.getPositionX());
        Assertions.assertEquals(posY1, resultDroplet1.getPositionY());
        Assertions.assertEquals(posX2, resultDroplet2.getPositionX());
        Assertions.assertEquals(posY2, resultDroplet2.getPositionY());
    }

    @Test
    public void testExecuteTick_againstRightBorder() {

        originDroplet.setPositionX(electrodeGrid.getXSize() - 3);
        originDroplet.setPositionY(10);

        // Arrange
        originDroplet.setStatus(DropletStatus.AVAILABLE);
        resultDroplet1.setStatus(DropletStatus.NOT_CREATED);
        resultDroplet2.setStatus(DropletStatus.NOT_CREATED);
        sut.beforeExecution(programConfiguration);

        // Ensure it completes at some point
        for(int i = 0; i < 1000; i++) { // it should complete way before 1000 ticks..
            if(sut.getStatus() == ActionStatus.COMPLETED) {
                break;
            }
            sut.executeTick(programConfiguration);
        }

        Assertions.assertEquals(ActionStatus.COMPLETED, sut.getStatus());

        // Assert
        Assertions.assertEquals(DropletStatus.CONSUMED, originDroplet.getStatus());
        Assertions.assertEquals(posX1, resultDroplet1.getPositionX());
        Assertions.assertEquals(posY1, resultDroplet1.getPositionY());
        Assertions.assertEquals(posX2, resultDroplet2.getPositionX());
        Assertions.assertEquals(posY2, resultDroplet2.getPositionY());
    }

    @Test
    public void testExecuteTick_againstTopBorder() {

        originDroplet.setPositionX(15);
        originDroplet.setPositionY(2);

        // Arrange
        originDroplet.setStatus(DropletStatus.AVAILABLE);
        resultDroplet1.setStatus(DropletStatus.NOT_CREATED);
        resultDroplet2.setStatus(DropletStatus.NOT_CREATED);
        sut.beforeExecution(programConfiguration);

        // Ensure it completes at some point
        for(int i = 0; i < 1000; i++) { // it should complete way before 1000 ticks..
            if(sut.getStatus() == ActionStatus.COMPLETED) {
                break;
            }
            sut.executeTick(programConfiguration);
        }

        Assertions.assertEquals(ActionStatus.COMPLETED, sut.getStatus());

        // Assert
        Assertions.assertEquals(DropletStatus.CONSUMED, originDroplet.getStatus());
        Assertions.assertEquals(posX1, resultDroplet1.getPositionX());
        Assertions.assertEquals(posY1, resultDroplet1.getPositionY());
        Assertions.assertEquals(posX2, resultDroplet2.getPositionX());
        Assertions.assertEquals(posY2, resultDroplet2.getPositionY());
    }

    @Test
    public void testExecuteTick_againstBottomBorder() {

        originDroplet.setPositionX(15);
        originDroplet.setPositionY(electrodeGrid.getYSize() - 3);

        // Arrange
        originDroplet.setStatus(DropletStatus.AVAILABLE);
        resultDroplet1.setStatus(DropletStatus.NOT_CREATED);
        resultDroplet2.setStatus(DropletStatus.NOT_CREATED);
        sut.beforeExecution(programConfiguration);

        // Ensure it completes at some point
        for(int i = 0; i < 1000; i++) { // it should complete way before 1000 ticks..
            if(sut.getStatus() == ActionStatus.COMPLETED) {
                break;
            }
            sut.executeTick(programConfiguration);
            if(i == 900) {
                System.out.println();
            }
        }

        Assertions.assertEquals(ActionStatus.COMPLETED, sut.getStatus());

        // Assert
        Assertions.assertEquals(DropletStatus.CONSUMED, originDroplet.getStatus());
        Assertions.assertEquals(posX1, resultDroplet1.getPositionX());
        Assertions.assertEquals(posY1, resultDroplet1.getPositionY());
        Assertions.assertEquals(posX2, resultDroplet2.getPositionX());
        Assertions.assertEquals(posY2, resultDroplet2.getPositionY());
    }


    @Test
    public void testAfterExecutionResult() {
        // Arrange
        originDroplet.setStatus(DropletStatus.UNAVAILABLE);
        resultDroplet1.setStatus(DropletStatus.NOT_CREATED);
        resultDroplet2.setStatus(DropletStatus.NOT_CREATED);

        sut.beforeExecution(programConfiguration);

        Assertions.assertEquals(DropletStatus.UNAVAILABLE, originDroplet.getStatus());
        Assertions.assertEquals(DropletStatus.NOT_CREATED, resultDroplet1.getStatus());
        Assertions.assertEquals(DropletStatus.NOT_CREATED, resultDroplet2.getStatus());

        sut.afterExecution(programConfiguration);

        Assertions.assertEquals(DropletStatus.CONSUMED, originDroplet.getStatus());
        Assertions.assertEquals(DropletStatus.AVAILABLE, resultDroplet1.getStatus());
        Assertions.assertEquals(DropletStatus.AVAILABLE, resultDroplet2.getStatus());
    }



}
