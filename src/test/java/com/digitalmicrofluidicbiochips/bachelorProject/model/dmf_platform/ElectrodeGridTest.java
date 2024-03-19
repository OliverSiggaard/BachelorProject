package com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform;

import com.digitalmicrofluidicbiochips.bachelorProject.utils.MockElectrodeGridSetupUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ElectrodeGridTest {

    private ElectrodeGrid electrodeGrid;
    private ElectrodeGridVisualizer electrodeGridVisualizer;
    private static final int SIZE_X = 10;
    private static final int SIZE_Y = 8;


    @BeforeEach
    void setUp() {
        electrodeGrid = MockElectrodeGridSetupUtil.createMockElectrodeGrid(SIZE_X, SIZE_Y);
        electrodeGridVisualizer = new ElectrodeGridVisualizer();
    }


    @Test
    void testGridHasCorrectDimensions() {
        // Assume SIZE_X * SIZE_Y grid
        assertEquals(SIZE_X, electrodeGrid.getGrid().length);
        assertEquals(SIZE_Y, electrodeGrid.getGrid()[0].length);
    }


    @Test
    void testAvailableElectrodesRetrievedCorrectly_activeDia1_obstacleDia1() {
        Droplet activeDroplet = new Droplet("1", 0, 0, 0.1);

        List<Droplet> obstacleDroplets = new ArrayList<>();
        Droplet obstacleDroplet = new Droplet("2", 4, 4, 0.1);
        obstacleDroplets.add(obstacleDroplet);

        Electrode[][] availableGrid = electrodeGrid.getAvailableElectrodeGrid(activeDroplet, obstacleDroplets);

        electrodeGridVisualizer.visualizeGrid(availableGrid);

        assertTrue(electrodeSquareIsNull(3, 5, 3, 5, availableGrid));
    }


    @Test
    void testAvailableElectrodesRetrievedCorrectly_activeDia1_obstacleDia2() {
        Droplet activeDroplet = new Droplet("1", 0, 0, 0.1);

        List<Droplet> obstacleDroplets = new ArrayList<>();
        Droplet obstacleDroplet = new Droplet("2", 4, 2, 0.3);
        obstacleDroplets.add(obstacleDroplet);

        Electrode[][] availableGrid = electrodeGrid.getAvailableElectrodeGrid(activeDroplet, obstacleDroplets);

        electrodeGridVisualizer.visualizeGrid(availableGrid);

        assertTrue(electrodeSquareIsNull(3, 6, 1, 4, availableGrid));
    }


    @Test
    void testAvailableElectrodesRetrievedCorrectly_overflowEdgeCase_topLeft() {
        Droplet activeDroplet = new Droplet("1", 6, 6, 0.1);

        List<Droplet> obstacleDroplets = new ArrayList<>();
        Droplet obstacleDroplet = new Droplet("2", 0, 0, 0.3);
        obstacleDroplets.add(obstacleDroplet);

        Electrode[][] availableGrid = electrodeGrid.getAvailableElectrodeGrid(activeDroplet, obstacleDroplets);

        electrodeGridVisualizer.visualizeGrid(availableGrid);

        assertTrue(electrodeSquareIsNull(0, 1, 0, 1, availableGrid));
    }


    @Test
    void testAvailableElectrodesRetrievedCorrectly_overflowEdgeCase_bottomRight() {
        Droplet activeDroplet = new Droplet("1", 0, 0, 0.1);

        List<Droplet> obstacleDroplets = new ArrayList<>();
        Droplet obstacleDroplet = new Droplet("2", 9, 7, 0.3);
        obstacleDroplets.add(obstacleDroplet);

        Electrode[][] availableGrid = electrodeGrid.getAvailableElectrodeGrid(activeDroplet, obstacleDroplets);

        electrodeGridVisualizer.visualizeGrid(availableGrid);

        assertTrue(electrodeSquareIsNull(8, 9, 6, 7, availableGrid));
    }


    @Test
    void testAvailableElectrodesRetrievedCorrectly_activeDia2_obstacleDia1() {
        Droplet activeDroplet = new Droplet("1", 0, 0, 0.3);

        List<Droplet> obstacleDroplets = new ArrayList<>();
        Droplet obstacleDroplet = new Droplet("2", 4, 4, 0.1);
        obstacleDroplets.add(obstacleDroplet);

        Electrode[][] availableGrid = electrodeGrid.getAvailableElectrodeGrid(activeDroplet, obstacleDroplets);

        electrodeGridVisualizer.visualizeGrid(availableGrid);

        assertTrue(electrodeSquareIsNull(3, 5, 3, 5, availableGrid));
        assertTrue(borderIsNull(1, availableGrid));
    }

    @Test
    void testAvailableElectrodesRetrievedCorrectly_activeDia3_obstacleDia1() {
        Droplet activeDroplet = new Droplet("1", 0, 0, 0.8);

        List<Droplet> obstacleDroplets = new ArrayList<>();
        Droplet obstacleDroplet = new Droplet("2", 4, 4, 0.1);
        obstacleDroplets.add(obstacleDroplet);

        Electrode[][] availableGrid = electrodeGrid.getAvailableElectrodeGrid(activeDroplet, obstacleDroplets);

        electrodeGridVisualizer.visualizeGrid(availableGrid);

        assertTrue(electrodeSquareIsNull(2, 5, 2, 5, availableGrid));
        assertTrue(borderIsNull(2, availableGrid));
    }

    @Test
    void testAvailableElectrodesRetrievedCorrectly_activeDia2_obstacleDia2() {
        Droplet activeDroplet = new Droplet("1", 0, 0, 0.3);

        List<Droplet> obstacleDroplets = new ArrayList<>();
        Droplet obstacleDroplet = new Droplet("2", 4, 4, 0.3);
        obstacleDroplets.add(obstacleDroplet);

        Electrode[][] availableGrid = electrodeGrid.getAvailableElectrodeGrid(activeDroplet, obstacleDroplets);

        electrodeGridVisualizer.visualizeGrid(availableGrid);

        assertTrue(electrodeSquareIsNull(2, 6, 2, 6, availableGrid));
        assertTrue(borderIsNull(1, availableGrid));
    }


    // Helper method for checking that a square of electrodes is null
    private boolean electrodeSquareIsNull(int x1, int x2, int y1, int y2, Electrode[][] grid) {
        boolean isNull = true;

        for (int y = y1; y <= y2; y++) {
            for (int x = x1; x <= x2; x++) {
                if(grid[x][y] != null) {
                    isNull = false;
                    break;
                }
            }
        }

        return isNull;
    }


    // Helper method for checking that the borders of the bottom and right side are null with a given width
    private boolean borderIsNull(int width, Electrode[][] grid) {
        boolean isNull = true;

        for (int i = 1; i <= width; i++) {
            // Remove right border
            for (int y = 0; y < SIZE_Y; y++) {
                if (grid[SIZE_X - i][y] != null) {
                    isNull = false;
                    break;
                }
            }
            // Remove bottom border
            for (int x = 0; x < SIZE_X; x++) {
                if (grid[x][SIZE_Y - i] != null) {
                    isNull = false;
                    break;
                }
            }
        }

        return isNull;
    }
}