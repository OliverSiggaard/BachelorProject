package com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DropletTest {
    @Test
    void testConstructorInitialization() {
        Droplet droplet = new Droplet(1, "Water", 20, 40, 0.2);

        assertEquals(1, droplet.getID());
        assertEquals("Water", droplet.getSubstance_name());
        assertEquals(20, droplet.getPositionX());
        assertEquals(40, droplet.getPositionY());
        assertEquals(0.2, droplet.getVolume());
        assertEquals(0.022568, droplet.getDiameter(), 0.0001); // Tests diameter calculation
    }

    @Test
    void testSetVolume() {
        Droplet droplet = new Droplet(1, "Water", 20, 40, 0.2);
        droplet.setVolume(0.3);

        assertEquals(0.3, droplet.getVolume());
        assertEquals(0.02764, droplet.getDiameter(), 0.0001); // Tests diameter calculation
    }
}
