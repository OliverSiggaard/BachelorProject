package com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform;

import com.digitalmicrofluidicbiochips.bachelorProject.executor.path_finding.DropletMove;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DropletTest {
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
}
