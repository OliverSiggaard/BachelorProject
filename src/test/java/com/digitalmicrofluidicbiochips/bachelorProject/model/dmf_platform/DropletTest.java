package com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform;

import com.digitalmicrofluidicbiochips.bachelorProject.executor.path_finding.DropletMove;
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


}
