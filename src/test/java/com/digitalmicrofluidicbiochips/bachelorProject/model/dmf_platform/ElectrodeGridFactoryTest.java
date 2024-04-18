package com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform;

import com.digitalmicrofluidicbiochips.bachelorProject.executor.path_finding.DropletMove;
import com.digitalmicrofluidicbiochips.bachelorProject.testUtils.MockElectrodeGridSetupUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ElectrodeGridFactoryTest {

    private ElectrodeGrid electrodeGrid;
    private ElectrodeGridVisualizer electrodeGridVisualizer;
    private static final int SIZE_X = 12;
    private static final int SIZE_Y = 10;


    @BeforeEach
    void setUp() {
        electrodeGrid= MockElectrodeGridSetupUtil.createMockElectrodeGrid(SIZE_X, SIZE_Y);
        electrodeGridVisualizer = new ElectrodeGridVisualizer();
    }


    @Test
    void testGridHasCorrectDimensions() {
        // Assume SIZE_X * SIZE_Y grid
        assertEquals(SIZE_X, electrodeGrid.getXSize());
        assertEquals(SIZE_Y, electrodeGrid.getYSize());
    }


    @Test
    void testAvailableElectrodesRetrievedCorrectly_activeDia1_obstacleDia1() {
        Droplet activeDroplet = new Droplet("1", 0, 0, 1);

        List<Droplet> obstacleDroplets = new ArrayList<>();
        Droplet obstacleDroplet = new Droplet("2", 4, 4, 1);
        obstacleDroplets.add(obstacleDroplet);

        ElectrodeGrid availableGrid = ElectrodeGridFactory.getAvailableElectrodeGrid(electrodeGrid, activeDroplet, obstacleDroplets);

        electrodeGridVisualizer.visualizeGrid(availableGrid);

        assertTrue(electrodeSquareIsNull(3, 5, 3, 5, availableGrid));
    }


    @Test
    void testAvailableElectrodesRetrievedCorrectly_activeDia1_obstacleDia2() {
        Droplet activeDroplet = new Droplet("1", 0, 0, 1);

        List<Droplet> obstacleDroplets = new ArrayList<>();
        Droplet obstacleDroplet = new Droplet("2", 4, 2, 3);
        obstacleDroplets.add(obstacleDroplet);

        ElectrodeGrid availableGrid = ElectrodeGridFactory.getAvailableElectrodeGrid(electrodeGrid, activeDroplet, obstacleDroplets);

        electrodeGridVisualizer.visualizeGrid(availableGrid);

        assertTrue(electrodeSquareIsNull(3, 6, 1, 4, availableGrid));
    }


    @Test
    void testAvailableElectrodesRetrievedCorrectly_overflowEdgeCase_topLeft() {
        Droplet activeDroplet = new Droplet("1", 6, 6, 1);

        List<Droplet> obstacleDroplets = new ArrayList<>();
        Droplet obstacleDroplet = new Droplet("2", 0, 0, 3);
        obstacleDroplets.add(obstacleDroplet);

        ElectrodeGrid availableGrid = ElectrodeGridFactory.getAvailableElectrodeGrid(electrodeGrid, activeDroplet, obstacleDroplets);

        electrodeGridVisualizer.visualizeGrid(availableGrid);

        assertTrue(electrodeSquareIsNull(0, 1, 0, 1, availableGrid));
    }


    @Test
    void testAvailableElectrodesRetrievedCorrectly_overflowEdgeCase_bottomRight() {
        Droplet activeDroplet = new Droplet("1", 0, 0, 1);

        List<Droplet> obstacleDroplets = new ArrayList<>();
        Droplet obstacleDroplet = new Droplet("2", 9, 7, 3);
        obstacleDroplets.add(obstacleDroplet);

        ElectrodeGrid availableGrid = ElectrodeGridFactory.getAvailableElectrodeGrid(electrodeGrid, activeDroplet, obstacleDroplets);

        electrodeGridVisualizer.visualizeGrid(availableGrid);

        assertTrue(electrodeSquareIsNull(8, 9, 6, 7, availableGrid));
    }


    @Test
    void testAvailableElectrodesRetrievedCorrectly_activeDia2_obstacleDia1() {
        Droplet activeDroplet = new Droplet("1", 0, 0, 3);

        List<Droplet> obstacleDroplets = new ArrayList<>();
        Droplet obstacleDroplet = new Droplet("2", 4, 4, 1);
        obstacleDroplets.add(obstacleDroplet);

        ElectrodeGrid availableGrid = ElectrodeGridFactory.getAvailableElectrodeGrid(electrodeGrid, activeDroplet, obstacleDroplets);

        electrodeGridVisualizer.visualizeGrid(availableGrid);

        assertTrue(electrodeSquareIsNull(3, 5, 3, 5, availableGrid));
        assertTrue(borderIsNull(1, availableGrid));
    }

    @Test
    void testAvailableElectrodesRetrievedCorrectly_activeDia3_obstacleDia1() {
        Droplet activeDroplet = new Droplet("1", 0, 0, 8);

        List<Droplet> obstacleDroplets = new ArrayList<>();
        Droplet obstacleDroplet = new Droplet("2", 4, 4, 1);
        obstacleDroplets.add(obstacleDroplet);

        ElectrodeGrid availableGrid = ElectrodeGridFactory.getAvailableElectrodeGrid(electrodeGrid, activeDroplet, obstacleDroplets);

        electrodeGridVisualizer.visualizeGrid(availableGrid);

        assertTrue(electrodeSquareIsNull(2, 5, 2, 5, availableGrid));
        assertTrue(borderIsNull(2, availableGrid));
    }

    @Test
    void testAvailableElectrodesRetrievedCorrectly_activeDia2_obstacleDia2() {
        Droplet activeDroplet = new Droplet("1", 0, 0, 3);

        List<Droplet> obstacleDroplets = new ArrayList<>();
        Droplet obstacleDroplet = new Droplet("2", 4, 4, 3);
        obstacleDroplets.add(obstacleDroplet);

        ElectrodeGrid availableGrid = ElectrodeGridFactory.getAvailableElectrodeGrid(electrodeGrid, activeDroplet, obstacleDroplets);

        electrodeGridVisualizer.visualizeGrid(availableGrid);

        assertTrue(electrodeSquareIsNull(2, 6, 2, 6, availableGrid));
        assertTrue(borderIsNull(1, availableGrid));
    }

    @Test
    void testAvailableElectrodesRetrievedCorrectly_activeDia2_obstacleDia2MovingUp() {
        Droplet activeDroplet = new Droplet("1", 0, 0, 3);

        List<Droplet> obstacleDroplets = new ArrayList<>();
        Droplet obstacleDroplet = new Droplet("2", 4, 4, 3);
        obstacleDroplet.setDropletMove(DropletMove.UP); // Set the move of the obstacle droplet to UP
        obstacleDroplets.add(obstacleDroplet);

        ElectrodeGrid availableGrid = ElectrodeGridFactory.getAvailableElectrodeGrid(electrodeGrid, activeDroplet, obstacleDroplets);

        electrodeGridVisualizer.visualizeGrid(availableGrid);

        assertTrue(electrodeSquareIsNull(2, 6, 1, 6, availableGrid));
        assertTrue(borderIsNull(1, availableGrid));
    }

    @Test
    void testAvailableElectrodesRetrievedCorrectly_activeDia2_obstacleDia2MovingDown() {
        Droplet activeDroplet = new Droplet("1", 0, 0, 3);

        List<Droplet> obstacleDroplets = new ArrayList<>();
        Droplet obstacleDroplet = new Droplet("2", 4, 4, 3);
        obstacleDroplet.setDropletMove(DropletMove.DOWN); // Set the move of the obstacle droplet to UP
        obstacleDroplets.add(obstacleDroplet);

        ElectrodeGrid availableGrid = ElectrodeGridFactory.getAvailableElectrodeGrid(electrodeGrid, activeDroplet, obstacleDroplets);

        electrodeGridVisualizer.visualizeGrid(availableGrid);

        assertTrue(electrodeSquareIsNull(2, 6, 2, 7, availableGrid));
        assertTrue(borderIsNull(1, availableGrid));
    }

    @Test
    void testAvailableElectrodesRetrievedCorrectly_activeDia2_obstacleDia2MovingLeft() {
        Droplet activeDroplet = new Droplet("1", 0, 0, 3);

        List<Droplet> obstacleDroplets = new ArrayList<>();
        Droplet obstacleDroplet = new Droplet("2", 4, 4, 3);
        obstacleDroplet.setDropletMove(DropletMove.LEFT); // Set the move of the obstacle droplet to UP
        obstacleDroplets.add(obstacleDroplet);

        ElectrodeGrid availableGrid = ElectrodeGridFactory.getAvailableElectrodeGrid(electrodeGrid, activeDroplet, obstacleDroplets);

        electrodeGridVisualizer.visualizeGrid(availableGrid);

        assertTrue(electrodeSquareIsNull(1, 6, 2, 6, availableGrid));
        assertTrue(borderIsNull(1, availableGrid));
    }

    @Test
    void testAvailableElectrodesRetrievedCorrectly_activeDia2_obstacleDia2MovingRight() {
        Droplet activeDroplet = new Droplet("1", 0, 0, 3);

        List<Droplet> obstacleDroplets = new ArrayList<>();
        Droplet obstacleDroplet = new Droplet("2", 4, 4, 3);
        obstacleDroplet.setDropletMove(DropletMove.RIGHT); // Set the move of the obstacle droplet to UP
        obstacleDroplets.add(obstacleDroplet);

        ElectrodeGrid availableGrid = ElectrodeGridFactory.getAvailableElectrodeGrid(electrodeGrid, activeDroplet, obstacleDroplets);

        electrodeGridVisualizer.visualizeGrid(availableGrid);

        assertTrue(electrodeSquareIsNull(2, 7, 2, 6, availableGrid));
        assertTrue(borderIsNull(1, availableGrid));
    }


    // Helper method for checking that a square of electrodes is null
    private boolean electrodeSquareIsNull(int x1, int x2, int y1, int y2, ElectrodeGrid grid) {
        boolean isNull = true;

        for (int y = y1; y <= y2; y++) {
            for (int x = x1; x <= x2; x++) {
                if(grid.getElectrode(x,y) != null) {
                    isNull = false;
                    break;
                }
            }
        }

        return isNull;
    }


    // Helper method for checking that the borders of the bottom and right side are null with a given width
    private boolean borderIsNull(int width, ElectrodeGrid grid) {
        boolean isNull = true;

        for (int i = 1; i <= width; i++) {
            // Remove right border
            for (int y = 0; y < SIZE_Y; y++) {
                if (grid.getElectrode(SIZE_X - i,y) != null) {
                    isNull = false;
                    break;
                }
            }
            // Remove bottom border
            for (int x = 0; x < SIZE_X; x++) {
                if (grid.getElectrode(x,SIZE_Y - i) != null) {
                    isNull = false;
                    break;
                }
            }
        }

        return isNull;
    }
}