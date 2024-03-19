package com.digitalmicrofluidicbiochips.bachelorProject.utils;

import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Electrode;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.ElectrodeGrid;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.PlatformInformation;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * This class is responsible for creating a mock electrode grid of a given size,
 * that can be used for testing purposes.
 */

public class MockElectrodeGridSetupUtil {

    public static ElectrodeGrid createMockElectrodeGrid(int sizeX, int sizeY) {
        ProgramConfiguration programConfiguration = mock(ProgramConfiguration.class);
        PlatformInformation platformInformation = mock(PlatformInformation.class);
        List<Electrode> electrodes = createMockElectrodes(sizeX, sizeY);

        when(programConfiguration.getPlatformInformation()).thenReturn(platformInformation);
        when(platformInformation.getElectrodes()).thenReturn(electrodes);

        // Mock platform size
        when(platformInformation.getSizeX()).thenReturn(sizeX * 20);
        when(platformInformation.getSizeY()).thenReturn(sizeY * 20);

        return new ElectrodeGrid(programConfiguration);
    }

    // Helper method to create mock electrodes for an m * n grid (x * y)
    private static List<Electrode> createMockElectrodes(int sizeX, int sizeY) {
        List<Electrode> electrodes = new ArrayList<>();

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                int electrodeID = getElectrodeID(x, y, sizeY);
                int ID = electrodeID - 100;
                electrodes.add(new Electrode("Electrode" + ID, ID,
                        electrodeID, 1,
                        x * 20, y * 20, 20, 20, 0));
            }
        }

        return electrodes;
    }

    // Helper method for calculating ElectrodeID for (x,y)
    private static int getElectrodeID(int x, int y, int sizeY) {
        return x + y * sizeY + 101;
    }
}
