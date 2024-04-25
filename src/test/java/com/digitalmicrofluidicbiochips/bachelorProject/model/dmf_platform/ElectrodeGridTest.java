package com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform;


import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.IDmfCommand;
import com.digitalmicrofluidicbiochips.bachelorProject.utils.DmfPlatformUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ElectrodeGridTest {

    private ElectrodeGrid sut;
    Electrode[][] electrode2dArray;
    @BeforeEach
    void setUp() {
        sut = createElectrodeGrid(32, 20, 20);
    }

    private ElectrodeGrid createElectrodeGrid(int sizeX, int sizeY, int electrodeSize) {
        Electrode[][] grid = new Electrode[sizeX][sizeY];
        for (int dx = 0; dx < sizeX; dx++) {
            for (int dy = 0; dy < sizeY; dy++) {
                grid[dx][dy] = new Electrode("Electrode" + dx + dy, dx + dy * dx, dx + dy * dx, 1, dx * 20, dy * 20, electrodeSize, electrodeSize, 0);
            }
        }
        electrode2dArray = grid;
        return new ElectrodeGrid(grid);
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
    void testGetElectrodeSizeOfElectrodeInGrid() {
        Assertions.assertEquals(20, sut.getElectrodeSizeOfElectrodeInGrid());

        sut = createElectrodeGrid(2, 2, 10);
        Assertions.assertEquals(10, sut.getElectrodeSizeOfElectrodeInGrid());

        sut.removeElectrode(0,0);
        Assertions.assertEquals(10, sut.getElectrodeSizeOfElectrodeInGrid());

        sut.removeElectrode(1,0);
        Assertions.assertEquals(10, sut.getElectrodeSizeOfElectrodeInGrid());

        sut.removeElectrode(0,1);
        Assertions.assertEquals(10, sut.getElectrodeSizeOfElectrodeInGrid());

        sut.removeElectrode(1,1);
        Assertions.assertThrows(IllegalStateException.class, () -> sut.getElectrodeSizeOfElectrodeInGrid());
    }

    @Test
    void testRemoveElectrodes() {
        GridArea gridArea = new GridArea(1, 2, 3, 4);
        sut.removeElectrodes(gridArea);
        for (int x = 1; x <= 3; x++) {
            for (int y = 2; y <= 4; y++) {
                Assertions.assertNull(sut.getElectrode(x, y));
            }
        }
    }

    @Test
    void testGetSetElectrodeCommands() {
        int x1 = 2;
        int x2 = 7;
        int y1 = 6;
        int y2 = 8;

        GridArea gridArea = mock(GridArea.class);
        when(gridArea.getX1()).thenReturn(x1);
        when(gridArea.getX2()).thenReturn(x2);
        when(gridArea.getY1()).thenReturn(y1);
        when(gridArea.getY2()).thenReturn(y2);

        List<IDmfCommand> setCommands =  sut.getSetElectrodeCommands(gridArea);
        Assertions.assertEquals(18, setCommands.size());

        int i = 0;
        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                String expected = DmfPlatformUtils.getSetElectrodeCommand(sut.getElectrode(x,y).getID());
                Assertions.assertEquals(expected, setCommands.get(i).getDmfCommand());
                i++;
            }
        }
    }

    @Test
    void testGetClearElectrodeCommands() {
        int x1 = 2;
        int x2 = 7;
        int y1 = 6;
        int y2 = 8;

        GridArea gridArea = mock(GridArea.class);
        when(gridArea.getX1()).thenReturn(x1);
        when(gridArea.getX2()).thenReturn(x2);
        when(gridArea.getY1()).thenReturn(y1);
        when(gridArea.getY2()).thenReturn(y2);

        List<IDmfCommand> clearCommands =  sut.getClearElectrodeCommands(gridArea);
        Assertions.assertEquals(18, clearCommands.size());

        int i = 0;
        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                String expected = DmfPlatformUtils.getClearElectrodeCommand(sut.getElectrode(x,y).getID());
                Assertions.assertEquals(expected, clearCommands.get(i).getDmfCommand());
                i++;
            }
        }
    }

    @Test
    void testIsAllElectrodesAvailableWithinArea() {
        int x1 = 2;
        int x2 = 7;
        int y1 = 6;
        int y2 = 8;

        GridArea gridArea = mock(GridArea.class);
        when(gridArea.getX1()).thenReturn(x1);
        when(gridArea.getX2()).thenReturn(x2);
        when(gridArea.getY1()).thenReturn(y1);
        when(gridArea.getY2()).thenReturn(y2);

        Assertions.assertTrue(sut.isAllElectrodesAvailableWithinArea(gridArea));

        sut.removeElectrode(3, 6);
        Assertions.assertFalse(sut.isAllElectrodesAvailableWithinArea(gridArea));

        sut.removeElectrode(5, 7);
        Assertions.assertFalse(sut.isAllElectrodesAvailableWithinArea(gridArea));

        sut.removeElectrode(7, 8);
        Assertions.assertFalse(sut.isAllElectrodesAvailableWithinArea(gridArea));

        sut.removeElectrode(2, 6);
        Assertions.assertFalse(sut.isAllElectrodesAvailableWithinArea(gridArea));

        sut.removeElectrode(7, 6);
        Assertions.assertFalse(sut.isAllElectrodesAvailableWithinArea(gridArea));

        sut.removeElectrode(2, 8);
        Assertions.assertFalse(sut.isAllElectrodesAvailableWithinArea(gridArea));

        sut.removeElectrode(7, 8);
        Assertions.assertFalse(sut.isAllElectrodesAvailableWithinArea(gridArea));

        sut.removeElectrode(2, 7);
        Assertions.assertFalse(sut.isAllElectrodesAvailableWithinArea(gridArea));

        sut.removeElectrode(7, 7);
        Assertions.assertFalse(sut.isAllElectrodesAvailableWithinArea(gridArea));
    }

}
