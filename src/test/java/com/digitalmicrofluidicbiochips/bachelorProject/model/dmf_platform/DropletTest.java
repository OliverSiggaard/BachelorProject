package com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform;

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
}
