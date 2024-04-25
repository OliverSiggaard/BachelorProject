package com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GridAreaTest {

    @Test
    public void testConstructor() {
        GridArea gridArea = new GridArea(1, 2, 3, 4);

        Assertions.assertEquals(1, gridArea.getX1());
        Assertions.assertEquals(2, gridArea.getY1());
        Assertions.assertEquals(3, gridArea.getX2());
        Assertions.assertEquals(4, gridArea.getY2());
    }

    @Test
    public void testGetters() {
        GridArea gridArea = new GridArea(1, 2, 3, 4);

        Assertions.assertEquals(1, gridArea.getX1());
        Assertions.assertEquals(2, gridArea.getY1());
        Assertions.assertEquals(3, gridArea.getX2());
        Assertions.assertEquals(4, gridArea.getY2());
    }

    @Test
    public void testContains() {
        GridArea gridArea = new GridArea(1, 2, 3, 4);

        Assertions.assertTrue(gridArea.contains(1, 2));
        Assertions.assertTrue(gridArea.contains(3, 4));
        Assertions.assertTrue(gridArea.contains(2, 3));
        Assertions.assertTrue(gridArea.contains(2, 4));
        Assertions.assertTrue(gridArea.contains(3, 2));
        Assertions.assertTrue(gridArea.contains(1, 4));
        Assertions.assertTrue(gridArea.contains(2, 2));
        Assertions.assertTrue(gridArea.contains(3, 3));
        Assertions.assertTrue(gridArea.contains(1, 3));
        Assertions.assertTrue(gridArea.contains(2, 4));

        Assertions.assertFalse(gridArea.contains(0, 2));
        Assertions.assertFalse(gridArea.contains(1, 1));
        Assertions.assertFalse(gridArea.contains(4, 2));
        Assertions.assertFalse(gridArea.contains(1, 5));
        Assertions.assertFalse(gridArea.contains(4, 5));
        Assertions.assertFalse(gridArea.contains(0, 1));
        Assertions.assertFalse(gridArea.contains(4, 1));
        Assertions.assertFalse(gridArea.contains(0, 5));
        Assertions.assertFalse(gridArea.contains(4, 5));
    }

}
