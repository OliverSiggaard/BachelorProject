package com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform;

import com.digitalmicrofluidicbiochips.bachelorProject.executor.path_finding.DropletMove;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations.MoveAction;
import com.digitalmicrofluidicbiochips.bachelorProject.testUtils.MockElectrodeGridSetupUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DropletTest {

    private ElectrodeGrid electrodeGrid;

    @BeforeEach
    void setUp() {
        electrodeGrid = MockElectrodeGridSetupUtil.createMockElectrodeGrid(32, 20);
    }

    @Test
    void testConstructorInitialization() {
        Droplet droplet = new Droplet("1", 20, 40, 2);

        assertEquals("1", droplet.getID());
        assertEquals(20, droplet.getPositionX());
        assertEquals(40, droplet.getPositionY());
        assertEquals(2, droplet.getVolume());
        assertEquals(23, droplet.getDiameter()); // Calculated diameter
    }

    @Test
    void testSetVolume() {
        Droplet droplet = new Droplet("1", 20, 40, 2);

        droplet.setVolume(3);

        assertEquals(3, droplet.getVolume());
    }

    @Test
    void testDiameterCalculation() {
        Droplet droplet = new Droplet("1", 20, 40, 1);

        assertEquals(16, droplet.getDiameter());
    }

    @Test
    void testDiameterCalculation_changedVolume() {
        Droplet droplet = new Droplet("1", 20, 40, 1);
        assertEquals(1, droplet.getVolume());
        assertEquals(16, droplet.getDiameter());

        droplet.setVolume(3);

        assertEquals(3, droplet.getVolume());
        assertEquals(28, droplet.getDiameter());
    }

    @Test
    void testDropletStatusStatus() {
        Droplet droplet = new Droplet("1", 20, 40, 1);
        Assertions.assertEquals(DropletStatus.NOT_CREATED, droplet.getStatus());

        droplet.setStatus(DropletStatus.AVAILABLE);
        Assertions.assertEquals(DropletStatus.AVAILABLE, droplet.getStatus());
    }

    @Test
    void testDropletMoveStatus() {
        Droplet droplet = new Droplet("1", 20, 40, 1);
        Assertions.assertEquals(DropletMove.NONE, droplet.getDropletMove());

        droplet.setDropletMove(DropletMove.RIGHT);
        Assertions.assertEquals(DropletMove.RIGHT, droplet.getDropletMove());
    }

    @Test
    void testDropletPosition() {
        Droplet droplet = new Droplet("1", 20, 40, 1);
        Assertions.assertEquals(20, droplet.getPositionX());
        Assertions.assertEquals(40, droplet.getPositionY());

        droplet.setPositionX(30);
        droplet.setPositionY(50);
        Assertions.assertEquals(30, droplet.getPositionX());
        Assertions.assertEquals(50, droplet.getPositionY());
    }

    @Test
    void testMoveDropletInDirectionUp() {
        Droplet droplet = new Droplet("1", 10, 10, 1);
        Assertions.assertEquals(10, droplet.getPositionX());
        Assertions.assertEquals(10, droplet.getPositionY());

        droplet.moveDropletInDirection(DropletMove.UP);
        Assertions.assertEquals(10, droplet.getPositionX());
        Assertions.assertEquals(9, droplet.getPositionY());

        droplet.setPositionX(10);
        droplet.setPositionY(10);
        Assertions.assertEquals(10, droplet.getPositionX());
        Assertions.assertEquals(10, droplet.getPositionY());

        droplet.moveDropletInDirection(DropletMove.DOWN);
        Assertions.assertEquals(10, droplet.getPositionX());
        Assertions.assertEquals(11, droplet.getPositionY());

        droplet.setPositionX(10);
        droplet.setPositionY(10);
        Assertions.assertEquals(10, droplet.getPositionX());
        Assertions.assertEquals(10, droplet.getPositionY());

        droplet.moveDropletInDirection(DropletMove.LEFT);
        Assertions.assertEquals(9, droplet.getPositionX());
        Assertions.assertEquals(10, droplet.getPositionY());

        droplet.setPositionX(10);
        droplet.setPositionY(10);
        Assertions.assertEquals(10, droplet.getPositionX());
        Assertions.assertEquals(10, droplet.getPositionY());

        droplet.moveDropletInDirection(DropletMove.RIGHT);
        Assertions.assertEquals(11, droplet.getPositionX());
        Assertions.assertEquals(10, droplet.getPositionY());
    }

    @Test
    void testDropletMoveChangesDropletPosition() {
        Assertions.assertTrue(Droplet.dropletMoveChangesDropletPosition(DropletMove.UP));
        Assertions.assertTrue(Droplet.dropletMoveChangesDropletPosition(DropletMove.DOWN));
        Assertions.assertTrue(Droplet.dropletMoveChangesDropletPosition(DropletMove.LEFT));
        Assertions.assertTrue(Droplet.dropletMoveChangesDropletPosition(DropletMove.RIGHT));
        Assertions.assertFalse(Droplet.dropletMoveChangesDropletPosition(DropletMove.NONE));
    }


    @Test
    void testGetCoordinatesToEnableBeforeMove1x1Droplet() {
        Droplet droplet = new Droplet("1", 10, 10, 1);// volume 1 = 1 electrode diameter

        droplet.setDropletMove(DropletMove.UP);
        Assertions.assertEquals(1, droplet.getElectrodesToEnableDuringDropletMove(electrodeGrid).size());
        Assertions.assertTrue(droplet.getElectrodesToEnableDuringDropletMove(electrodeGrid).contains(electrodeGrid.getElectrode(10,9)));

        droplet.setDropletMove(DropletMove.DOWN);
        Assertions.assertEquals(1, droplet.getElectrodesToEnableDuringDropletMove(electrodeGrid).size());
        Assertions.assertTrue(droplet.getElectrodesToEnableDuringDropletMove(electrodeGrid).contains(electrodeGrid.getElectrode(10,11)));

        droplet.setDropletMove(DropletMove.LEFT);
        Assertions.assertEquals(1, droplet.getElectrodesToEnableDuringDropletMove(electrodeGrid).size());
        Assertions.assertTrue(droplet.getElectrodesToEnableDuringDropletMove(electrodeGrid).contains(electrodeGrid.getElectrode(9,10)));

        droplet.setDropletMove(DropletMove.RIGHT);
        Assertions.assertEquals(1, droplet.getElectrodesToEnableDuringDropletMove(electrodeGrid).size());
        Assertions.assertTrue(droplet.getElectrodesToEnableDuringDropletMove(electrodeGrid).contains(electrodeGrid.getElectrode(11,10)));
    }

    @Test
    void testGetCoordinatesToEnableBeforeMove3x3Droplet() {
        Droplet droplet = new Droplet("1", 10, 10, 15); // volume 15 = 2 electrodes diameter

        droplet.setDropletMove(DropletMove.UP);
        Assertions.assertEquals(3, droplet.getElectrodesToEnableDuringDropletMove(electrodeGrid).size());
        Assertions.assertTrue(droplet.getElectrodesToEnableDuringDropletMove(electrodeGrid).contains(electrodeGrid.getElectrode(10,9)));
        Assertions.assertTrue(droplet.getElectrodesToEnableDuringDropletMove(electrodeGrid).contains(electrodeGrid.getElectrode(11,9)));
        Assertions.assertTrue(droplet.getElectrodesToEnableDuringDropletMove(electrodeGrid).contains(electrodeGrid.getElectrode(12,9)));

        droplet.setDropletMove(DropletMove.DOWN);
        Assertions.assertEquals(3, droplet.getElectrodesToEnableDuringDropletMove(electrodeGrid).size());
        Assertions.assertTrue(droplet.getElectrodesToEnableDuringDropletMove(electrodeGrid).contains(electrodeGrid.getElectrode(10,13)));
        Assertions.assertTrue(droplet.getElectrodesToEnableDuringDropletMove(electrodeGrid).contains(electrodeGrid.getElectrode(11,13)));
        Assertions.assertTrue(droplet.getElectrodesToEnableDuringDropletMove(electrodeGrid).contains(electrodeGrid.getElectrode(12,13)));

        droplet.setDropletMove(DropletMove.LEFT);
        Assertions.assertEquals(3, droplet.getElectrodesToEnableDuringDropletMove(electrodeGrid).size());
        Assertions.assertTrue(droplet.getElectrodesToEnableDuringDropletMove(electrodeGrid).contains(electrodeGrid.getElectrode(9,10)));
        Assertions.assertTrue(droplet.getElectrodesToEnableDuringDropletMove(electrodeGrid).contains(electrodeGrid.getElectrode(9,11)));
        Assertions.assertTrue(droplet.getElectrodesToEnableDuringDropletMove(electrodeGrid).contains(electrodeGrid.getElectrode(9,12)));

        droplet.setDropletMove(DropletMove.RIGHT);
        Assertions.assertEquals(3, droplet.getElectrodesToEnableDuringDropletMove(electrodeGrid).size());
        Assertions.assertTrue(droplet.getElectrodesToEnableDuringDropletMove(electrodeGrid).contains(electrodeGrid.getElectrode(13,10)));
        Assertions.assertTrue(droplet.getElectrodesToEnableDuringDropletMove(electrodeGrid).contains(electrodeGrid.getElectrode(13,11)));
        Assertions.assertTrue(droplet.getElectrodesToEnableDuringDropletMove(electrodeGrid).contains(electrodeGrid.getElectrode(13,12)));
    }

    @Test
    void testGetCoordinatesToDisableBeforeMove1x1Droplet() {
        Droplet droplet = new Droplet("1", 10, 10, 1);// volume 1 = 1 electrode diameter

        droplet.setDropletMove(DropletMove.UP);
        Assertions.assertEquals(1, droplet.getElectrodesToDisableDuringDropletMove(electrodeGrid).size());
        Assertions.assertTrue(droplet.getElectrodesToDisableDuringDropletMove(electrodeGrid).contains(electrodeGrid.getElectrode(10,10)));

        droplet.setDropletMove(DropletMove.DOWN);
        Assertions.assertEquals(1, droplet.getElectrodesToDisableDuringDropletMove(electrodeGrid).size());
        Assertions.assertTrue(droplet.getElectrodesToDisableDuringDropletMove(electrodeGrid).contains(electrodeGrid.getElectrode(10,10)));

        droplet.setDropletMove(DropletMove.LEFT);
        Assertions.assertEquals(1, droplet.getElectrodesToDisableDuringDropletMove(electrodeGrid).size());
        Assertions.assertTrue(droplet.getElectrodesToDisableDuringDropletMove(electrodeGrid).contains(electrodeGrid.getElectrode(10,10)));

        droplet.setDropletMove(DropletMove.RIGHT);
        Assertions.assertEquals(1, droplet.getElectrodesToDisableDuringDropletMove(electrodeGrid).size());
        Assertions.assertTrue(droplet.getElectrodesToDisableDuringDropletMove(electrodeGrid).contains(electrodeGrid.getElectrode(10,10)));
    }

    @Test
    void testGetCoordinatesToDisableBeforeMove3x3Droplet() {
        Droplet droplet = new Droplet("1", 10, 10, 15); // volume 15 = 3 electrodes diameter

        droplet.setDropletMove(DropletMove.UP);
        Assertions.assertEquals(3, droplet.getElectrodesToDisableDuringDropletMove(electrodeGrid).size());
        Assertions.assertTrue(droplet.getElectrodesToDisableDuringDropletMove(electrodeGrid).contains(electrodeGrid.getElectrode(10,12)));
        Assertions.assertTrue(droplet.getElectrodesToDisableDuringDropletMove(electrodeGrid).contains(electrodeGrid.getElectrode(11,12)));
        Assertions.assertTrue(droplet.getElectrodesToDisableDuringDropletMove(electrodeGrid).contains(electrodeGrid.getElectrode(12,12)));

        droplet.setDropletMove(DropletMove.DOWN);
        Assertions.assertEquals(3, droplet.getElectrodesToDisableDuringDropletMove(electrodeGrid).size());
        Assertions.assertTrue(droplet.getElectrodesToDisableDuringDropletMove(electrodeGrid).contains(electrodeGrid.getElectrode(10,10)));
        Assertions.assertTrue(droplet.getElectrodesToDisableDuringDropletMove(electrodeGrid).contains(electrodeGrid.getElectrode(11,10)));
        Assertions.assertTrue(droplet.getElectrodesToDisableDuringDropletMove(electrodeGrid).contains(electrodeGrid.getElectrode(12,10)));

        droplet.setDropletMove(DropletMove.LEFT);
        Assertions.assertEquals(3, droplet.getElectrodesToDisableDuringDropletMove(electrodeGrid).size());
        Assertions.assertTrue(droplet.getElectrodesToDisableDuringDropletMove(electrodeGrid).contains(electrodeGrid.getElectrode(12,10)));
        Assertions.assertTrue(droplet.getElectrodesToDisableDuringDropletMove(electrodeGrid).contains(electrodeGrid.getElectrode(12,11)));
        Assertions.assertTrue(droplet.getElectrodesToDisableDuringDropletMove(electrodeGrid).contains(electrodeGrid.getElectrode(12,12)));

        droplet.setDropletMove(DropletMove.RIGHT);
        Assertions.assertEquals(3, droplet.getElectrodesToDisableDuringDropletMove(electrodeGrid).size());
        Assertions.assertTrue(droplet.getElectrodesToDisableDuringDropletMove(electrodeGrid).contains(electrodeGrid.getElectrode(10,10)));
        Assertions.assertTrue(droplet.getElectrodesToDisableDuringDropletMove(electrodeGrid).contains(electrodeGrid.getElectrode(10,11)));
        Assertions.assertTrue(droplet.getElectrodesToDisableDuringDropletMove(electrodeGrid).contains(electrodeGrid.getElectrode(10,12)));
    }

    @Test
    void testGetDropletElectrodeArea() {
        Droplet droplet = new Droplet("1", 10, 11, 15); // volume 15 = 3 electrodes diameter

        GridArea gridArea = droplet.getDropletElectrodeArea(electrodeGrid);
        Assertions.assertEquals(10, gridArea.getX1());
        Assertions.assertEquals(11, gridArea.getY1());
        Assertions.assertEquals(12, gridArea.getX2());
        Assertions.assertEquals(13, gridArea.getY2());
    }

    @Test
    void testGetDropletSafeArea() {
        ElectrodeGrid electrodeGrid = MockElectrodeGridSetupUtil.createMockElectrodeGrid(32, 20);

        Droplet movingDroplet = new Droplet("1", 3, 4, 15); // volume 15 = 3 electrode diameter
        Droplet obstacleDroplet = new Droplet("1", 10, 12, 15); // volume 15 = 3 electrode diameter

        GridArea gridArea = movingDroplet.getDropletSafeArea(electrodeGrid, obstacleDroplet);

        Assertions.assertEquals(6, gridArea.getX1());
        Assertions.assertEquals(8, gridArea.getY1());
        Assertions.assertEquals(14, gridArea.getX2());
        Assertions.assertEquals(16, gridArea.getY2());
    }

    @Test
    void testDropletClone() {
        Droplet droplet = new Droplet("1", 10, 11, 15); // volume 15 = 3 electrodes diameter

        Droplet clonedDroplet = droplet.clone();
        Assertions.assertEquals(droplet.getID(), clonedDroplet.getID());
        Assertions.assertEquals(droplet.getPositionX(), clonedDroplet.getPositionX());
        Assertions.assertEquals(droplet.getPositionY(), clonedDroplet.getPositionY());
        Assertions.assertEquals(droplet.getVolume(), clonedDroplet.getVolume());
        Assertions.assertEquals(droplet.getDiameter(), clonedDroplet.getDiameter());
        Assertions.assertEquals(droplet.getStatus(), clonedDroplet.getStatus());
        Assertions.assertEquals(droplet.getDropletMove(), clonedDroplet.getDropletMove());
    }

    @Test
    void testDropletEquals() {
        Droplet droplet1 = new Droplet("1", 10, 11, 15); // volume 15 = 3 electrodes diameter
        Droplet droplet2 = new Droplet("1", 10, 11, 15); // volume 15 = 3 electrodes diameter
        Droplet droplet3 = new Droplet("2", 10, 11, 15); // volume 15 = 3 electrodes diameter
        Droplet droplet4 = new Droplet("1", 11, 11, 15); // volume 15 = 3 electrodes diameter
        Droplet droplet5 = new Droplet("1", 10, 12, 15); // volume 15 = 3 electrodes diameter
        Droplet droplet6 = new Droplet("1", 10, 11, 16); // volume 16 = 3 electrodes diameter

        Assertions.assertEquals(droplet1, droplet2);
        Assertions.assertNotEquals(droplet1, droplet3);
        Assertions.assertNotEquals(droplet1, droplet4);
        Assertions.assertNotEquals(droplet1, droplet5);
        Assertions.assertNotEquals(droplet1, droplet6);
    }

}
