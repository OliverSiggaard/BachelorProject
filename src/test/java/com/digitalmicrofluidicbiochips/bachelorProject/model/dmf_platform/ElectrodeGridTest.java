package com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class ElectrodeGridTest {

    private ElectrodeGrid sut;
    Electrode[][] electrode2dArray;
    @BeforeEach
    void setUp() {
        electrode2dArray = new Electrode[32][20];
        for (int dx = 0; dx < 32; dx++) {
            for (int dy = 0; dy < 20; dy++) {
                electrode2dArray[dx][dy] = new Electrode("Electrode" + dx + dy, dx + dy * dx, dx + dy * dx, 1, dx * 20, dy * 20, 20, 20, 0);
            }
        }
        sut = new ElectrodeGrid(electrode2dArray);

    }

    @Test
    void testGetElectrode() {
        Assertions.assertEquals(electrode2dArray[3][3], sut.getElectrode(3,3));
        Assertions.assertEquals(electrode2dArray[0][0], sut.getElectrode(0,0));
        Assertions.assertEquals(electrode2dArray[31][19], sut.getElectrode(31,19));
        Assertions.assertEquals(electrode2dArray[31][0], sut.getElectrode(31,0));
        Assertions.assertEquals(electrode2dArray[0][19], sut.getElectrode(0,19));
        Assertions.assertEquals(electrode2dArray[15][10], sut.getElectrode(15,10));
        Assertions.assertEquals(electrode2dArray[10][15], sut.getElectrode(10,15));
    }

    @Test
    void testSize() {
        Assertions.assertEquals(electrode2dArray.length, sut.getXSize());
        Assertions.assertEquals(electrode2dArray[0].length, sut.getYSize());
    }

    @Test
    void testRemoveElectrode() {
        sut.removeElectrode(3,3);
        Assertions.assertNull(sut.getElectrode(3,3));

        sut.removeElectrode(0,0);
        Assertions.assertNull(sut.getElectrode(0,0));

        sut.removeElectrode(31,19);
        Assertions.assertNull(sut.getElectrode(31,19));

        sut.removeElectrode(31,0);
        Assertions.assertNull(sut.getElectrode(31,0));

        sut.removeElectrode(0,19);
        Assertions.assertNull(sut.getElectrode(0,19));

        sut.removeElectrode(15,10);
        Assertions.assertNull(sut.getElectrode(15,10));

        sut.removeElectrode(10,15);
        Assertions.assertNull(sut.getElectrode(10,15));
    }

    @Test
    void testGetElectrodesAs2dArray() {
        Assertions.assertArrayEquals(electrode2dArray, sut.getGridAs2dArray());
    }

    @Test
    void testClone() {
        ElectrodeGrid cloned = sut.clone();
        Assertions.assertNotSame(sut, cloned);

        Assertions.assertNotSame(sut.getGridAs2dArray(), cloned.getGridAs2dArray());
        for (int dx = 0; dx < 32; dx++) {
            Assertions.assertNotSame(sut.getGridAs2dArray()[dx], cloned.getGridAs2dArray()[dx]);
            for (int dy = 0; dy < 20; dy++) {
                Assertions.assertSame(sut.getElectrode(dx, dy), cloned.getElectrode(dx, dy));
            }
        }
    }

    @Test
    void testGetElectrodesInGridAt() {
        List<Point> points = new ArrayList<>();
        points.add(new Point(0,0));
        points.add(new Point(1,3));
        points.add(new Point(12,7));
        points.add(new Point(31,19));
        points.add(new Point(15,10));
        points.add(new Point(10,15));

        List<Electrode> electrodes = sut.getElectrodesInGridAt(points);
        Assertions.assertEquals(6, electrodes.size());
        Assertions.assertTrue(electrodes.contains(electrode2dArray[0][0]));
        Assertions.assertTrue(electrodes.contains(electrode2dArray[1][3]));
        Assertions.assertTrue(electrodes.contains(electrode2dArray[12][7]));
        Assertions.assertTrue(electrodes.contains(electrode2dArray[31][19]));
        Assertions.assertTrue(electrodes.contains(electrode2dArray[15][10]));
        Assertions.assertTrue(electrodes.contains(electrode2dArray[10][15]));
    }

}
