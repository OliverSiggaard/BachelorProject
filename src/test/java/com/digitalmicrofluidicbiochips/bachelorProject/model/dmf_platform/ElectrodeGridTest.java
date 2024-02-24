package com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform;

import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ElectrodeGridTest {

    private ElectrodeGrid electrodeGrid;

    @BeforeEach
    void setUp() {
        ProgramConfiguration programConfiguration = mock(ProgramConfiguration.class);
        PlatformInformation platformInformation = mock(PlatformInformation.class);
        List<Electrode> electrodes = createMockElectrodes();

        when(programConfiguration.getPlatformInformation()).thenReturn(platformInformation);

        when(platformInformation.getElectrodes()).thenReturn(electrodes);
        // Mock platform size
        when(platformInformation.getSizeX()).thenReturn(60);
        when(platformInformation.getSizeY()).thenReturn(60);

        electrodeGrid = new ElectrodeGrid(programConfiguration);
    }

    @Test
    void testGridHasCorrectDimensions() {
        // Assume 3x3 grid
        assertEquals(3, electrodeGrid.getGrid().length);
        assertEquals(3, electrodeGrid.getGrid()[0].length);
    }

    @Test
    void testElectrodesAreOrderedCorrectly() {
        // Electrodes should be in an (x,y) format
        assertEquals(createMockElectrodes().get(0).getElectrodeID(), electrodeGrid.getGrid()[0][0].getElectrodeID());
        assertEquals(createMockElectrodes().get(1).getElectrodeID(), electrodeGrid.getGrid()[1][0].getElectrodeID());
        assertEquals(createMockElectrodes().get(4).getElectrodeID(), electrodeGrid.getGrid()[1][1].getElectrodeID());
        assertEquals(createMockElectrodes().get(8).getElectrodeID(), electrodeGrid.getGrid()[2][2].getElectrodeID());
    }


    @Test
    void testGetCorrectElectrodesFromDroplet_smallerDroplet() {
        Droplet droplet = new Droplet(1, "Water", 20, 20, 0.1);

        // Get electrodes that the droplet is touching
        List<Electrode> dropletElectrodes = electrodeGrid.getElectrodesFromDroplet(droplet);

        // This droplet should only touch the center electrode which is (1,1) with electrodeID 105.
        assertEquals(1, dropletElectrodes.size());
        assertEquals(105, dropletElectrodes.getFirst().getElectrodeID());
    }

    @Test
    void testGetCorrectElectrodesFromDroplet_biggerDroplet() {
        Droplet droplet = new Droplet(1, "Water", 20, 20, 0.3);

        List<Electrode> dropletElectrodes = electrodeGrid.getElectrodesFromDroplet(droplet);

        // This droplet should take up 4 electrodes with the top left corner at pos (20,20), which is electrode (1,1).
        // That is that the droplet electrodes should have the electrodeID's {105, 106, 108, 109}
        assertEquals(4, dropletElectrodes.size());
        assertEquals(105, dropletElectrodes.get(0).getElectrodeID());
        assertEquals(106, dropletElectrodes.get(1).getElectrodeID());
        assertEquals(108, dropletElectrodes.get(2).getElectrodeID());
        assertEquals(109, dropletElectrodes.get(3).getElectrodeID());
    }

    @Test
    void testGetDropletElectrodes_edgeCaseDiameterCausesOverflow() {
        Droplet droplet = new Droplet(1, "Water", 40, 40, 0.3);

        List<Electrode> dropletElectrodes = electrodeGrid.getElectrodesFromDroplet(droplet);

        // This droplet should take up 4 electrodes, but since the top left corner of the droplet is already in the
        // bottom left corner electrode (2,2), the diameter will cause overflow but only one electrode is returned
        assertEquals(1, dropletElectrodes.size());
        assertEquals(109, dropletElectrodes.getFirst().getElectrodeID());
    }


    @Test
    void testSafeAreaElectrodes() {
        Droplet droplet = new Droplet(1, "Water", 20, 20, 0.1);

        // Get electrodes that the droplet is touching + safe area around droplet
        List<Electrode> dropletElectrodes = electrodeGrid.getSafeAreaElectrodesFromDroplet(droplet);

        // This droplet should only touch the center electrode which is (1,1) with electrodeID 105.
        // The safe area should be all droplets around it, which is all droplets in the grid (9).
        assertEquals(9, dropletElectrodes.size());
    }

    @Test
    void testSafeAreaElectrodes_edgeCaseTopLeftCorner() {
        Droplet droplet = new Droplet(1, "Water", 0, 0, 0.1);

        List<Electrode> dropletElectrodes = electrodeGrid.getSafeAreaElectrodesFromDroplet(droplet);

        // Droplet should only touch top left corner electrode (0,0), with electrodeID 101.
        // The safe area should consist of electrodeID's 102, 104 and 105
        assertEquals(4, dropletElectrodes.size());
        assertEquals(101, dropletElectrodes.get(0).getElectrodeID());
        assertEquals(102, dropletElectrodes.get(1).getElectrodeID());
        assertEquals(104, dropletElectrodes.get(2).getElectrodeID());
        assertEquals(105, dropletElectrodes.get(3).getElectrodeID());
    }

    @Test
    void testSafeAreaElectrodes_edgeCaseBottomRightCorner() {
        Droplet droplet = new Droplet(1, "Water", 40, 40, 0.1);

        List<Electrode> dropletElectrodes = electrodeGrid.getSafeAreaElectrodesFromDroplet(droplet);

        // Droplet should only touch bottom right corner electrode (2,2), with electrodeID 109.
        // The safe area should consist of electrodeID's 105, 106 and 108
        assertEquals(4, dropletElectrodes.size());
        assertEquals(105, dropletElectrodes.get(0).getElectrodeID());
        assertEquals(106, dropletElectrodes.get(1).getElectrodeID());
        assertEquals(108, dropletElectrodes.get(2).getElectrodeID());
        assertEquals(109, dropletElectrodes.get(3).getElectrodeID());
    }


    public static List<Electrode> createMockElectrodes() {
        List<Electrode> electrodes = new ArrayList<>();

        electrodes.add(new Electrode("Electrode1", 1, 101, 201, 0, 0, 20, 20, 0));
        electrodes.add(new Electrode("Electrode2", 2, 102, 202, 20, 0, 20, 20, 0));
        electrodes.add(new Electrode("Electrode3", 3, 103, 203, 40, 0, 20, 20, 0));
        electrodes.add(new Electrode("Electrode4", 4, 104, 204, 0, 20, 20, 20, 0));
        electrodes.add(new Electrode("Electrode5", 5, 105, 205, 20, 20, 20, 20, 0));
        electrodes.add(new Electrode("Electrode6", 6, 106, 206, 40, 20, 20, 20, 0));
        electrodes.add(new Electrode("Electrode7", 7, 107, 207, 0, 40, 20, 20, 0));
        electrodes.add(new Electrode("Electrode8", 8, 108, 208, 20, 40, 20, 20, 0));
        electrodes.add(new Electrode("Electrode9", 9, 109, 209, 40, 40, 20, 20, 0));

        return electrodes;
    }
}