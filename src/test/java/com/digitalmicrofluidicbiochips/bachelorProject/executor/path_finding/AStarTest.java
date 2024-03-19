package com.digitalmicrofluidicbiochips.bachelorProject.executor.path_finding;

import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.*;
import com.digitalmicrofluidicbiochips.bachelorProject.utils.MockElectrodeGridSetupUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AStarTest {

    private ElectrodeGrid electrodeGrid;
    private ElectrodeGridVisualizer electrodeGridVisualizer;
    private static final int SIZE_X = 10;
    private static final int SIZE_Y = 8;

    /**
     * These tests are meant to be mostly visual with the grid being drawn in the terminal
     * and the path being logged
     */

    @BeforeEach
    void setUp() {
        electrodeGrid = MockElectrodeGridSetupUtil.createMockElectrodeGrid(SIZE_X, SIZE_Y);
        electrodeGridVisualizer = new ElectrodeGridVisualizer();
    }

    @Test
    void testAStar_success() {
        AStar aStar = new AStar(true);

        Droplet activeDroplet = new Droplet("1", 0, 0, 3);

        List<Droplet> obstacleDroplets = new ArrayList<>();
        Droplet obstacleDroplet = new Droplet("2", 4, 4, 1);
        obstacleDroplets.add(obstacleDroplet);

        Electrode[][] availableGrid = electrodeGrid.getAvailableElectrodeGrid(activeDroplet, obstacleDroplets);

        electrodeGridVisualizer.visualizeGrid(availableGrid);

        DropletMove move = aStar.getMove(activeDroplet, availableGrid, 6, 6);
        assertNotNull(move);
    }

    @Test
    void testAStar_failure() {
        AStar aStar = new AStar(true);

        Droplet activeDroplet = new Droplet("1", 0, 0, 3);

        List<Droplet> obstacleDroplets = new ArrayList<>();
        Droplet obstacleDroplet = new Droplet("2", 4, 4, 1);
        obstacleDroplets.add(obstacleDroplet);

        Electrode[][] availableGrid = electrodeGrid.getAvailableElectrodeGrid(activeDroplet, obstacleDroplets);

        electrodeGridVisualizer.visualizeGrid(availableGrid);

        DropletMove move = aStar.getMove(activeDroplet, availableGrid, 4, 4);
        assertEquals(DropletMove.BLOCKED, move);
    }

    @Test
    void testAStar_moveUp() {
        AStar aStar = new AStar(true);

        Droplet activeDroplet = new Droplet("1", 2, 2, 1);

        List<Droplet> obstacleDroplets = new ArrayList<>();

        Electrode[][] availableGrid = electrodeGrid.getAvailableElectrodeGrid(activeDroplet, obstacleDroplets);

        electrodeGridVisualizer.visualizeGrid(availableGrid);

        DropletMove move = aStar.getMove(activeDroplet, availableGrid, 2, 1);
        assertEquals(DropletMove.UP, move);
    }

    @Test
    void testAStar_moveDown() {
        AStar aStar = new AStar(true);

        Droplet activeDroplet = new Droplet("1", 2, 2, 1);

        List<Droplet> obstacleDroplets = new ArrayList<>();

        Electrode[][] availableGrid = electrodeGrid.getAvailableElectrodeGrid(activeDroplet, obstacleDroplets);

        electrodeGridVisualizer.visualizeGrid(availableGrid);

        DropletMove move = aStar.getMove(activeDroplet, availableGrid, 2, 3);
        assertEquals(DropletMove.DOWN, move);
    }

    @Test
    void testAStar_moveLeft() {
        AStar aStar = new AStar(true);

        Droplet activeDroplet = new Droplet("1", 2, 2, 1);

        List<Droplet> obstacleDroplets = new ArrayList<>();

        Electrode[][] availableGrid = electrodeGrid.getAvailableElectrodeGrid(activeDroplet, obstacleDroplets);

        electrodeGridVisualizer.visualizeGrid(availableGrid);

        DropletMove move = aStar.getMove(activeDroplet, availableGrid, 1, 2);
        assertEquals(DropletMove.LEFT, move);
    }

    @Test
    void testAStar_moveRight() {
        AStar aStar = new AStar(true);

        Droplet activeDroplet = new Droplet("1", 2, 2, 1);

        List<Droplet> obstacleDroplets = new ArrayList<>();

        Electrode[][] availableGrid = electrodeGrid.getAvailableElectrodeGrid(activeDroplet, obstacleDroplets);

        electrodeGridVisualizer.visualizeGrid(availableGrid);

        DropletMove move = aStar.getMove(activeDroplet, availableGrid, 3, 2);
        assertEquals(DropletMove.RIGHT, move);
    }

    @Test
    void testAStar_moveNone() {
        AStar aStar = new AStar(true);

        Droplet activeDroplet = new Droplet("1", 2, 2, 1);

        List<Droplet> obstacleDroplets = new ArrayList<>();

        Electrode[][] availableGrid = electrodeGrid.getAvailableElectrodeGrid(activeDroplet, obstacleDroplets);

        electrodeGridVisualizer.visualizeGrid(availableGrid);

        DropletMove move = aStar.getMove(activeDroplet, availableGrid, 2, 2);
        assertEquals(DropletMove.NONE, move);
    }
}
