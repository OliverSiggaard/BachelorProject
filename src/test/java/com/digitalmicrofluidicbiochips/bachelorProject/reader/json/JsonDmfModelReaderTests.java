package com.digitalmicrofluidicbiochips.bachelorProject.reader.json;

import com.digitalmicrofluidicbiochips.bachelorProject.reader.json.model.JsonProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.json.model.dmf_platform.JsonDmfPlatformElectrode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.fail;

public class JsonDmfModelReaderTests {

    private final String filePath = "src/test/resources/reader/dmf_configuration.JSON"; // Adjust the file name if needed
    private JsonProgramConfiguration programConfiguration;

    @BeforeEach
    public void setUp() {
        File programFile = new File(filePath);
        if (!programFile.exists()) {
            Assertions.fail("The program file does not exist: " + filePath);
        }

        try {
            programConfiguration = JsonModelLoader.loadProgramConfigurationFromJson(programFile);
        } catch (IOException e) {
            Assertions.fail("Could not read the program configuration from the JSON file. Error: " + e.getMessage());
        }

    }

    @Test
    public void informationReadCorrect() {
        if (programConfiguration.getDmfPlatformState().getInformation() == null) {
            fail("The loaded program configuration does not contain any actions.");
        }
        Assertions.assertEquals("Platform 640 V2", programConfiguration.getDmfPlatformState().getInformation().getPlatform_name());
        Assertions.assertEquals("platform_640_v2", programConfiguration.getDmfPlatformState().getInformation().getPlatform_type());
        Assertions.assertEquals(6401, programConfiguration.getDmfPlatformState().getInformation().getPlatform_ID());
        Assertions.assertEquals(640, programConfiguration.getDmfPlatformState().getInformation().getSizeX());
        Assertions.assertEquals(400, programConfiguration.getDmfPlatformState().getInformation().getSizeY());
    }

    @Test
    public void electrodesReadCorrect() {
        if (programConfiguration.getDmfPlatformState().getElectrodes() == null) {
            fail("The loaded program configuration does not contain any actions.");
        }
        List<JsonDmfPlatformElectrode> electrodes = programConfiguration.getDmfPlatformState().getElectrodes();

        Assertions.assertEquals(640, electrodes.size());

        JsonDmfPlatformElectrode electrode1 = electrodes.get(0);
        Assertions.assertEquals("arrel1", electrode1.getName());
        Assertions.assertEquals(1, electrode1.getID());
        Assertions.assertEquals(360, electrode1.getElectrodeID());
        Assertions.assertEquals(0, electrode1.getDriverID());
        Assertions.assertEquals(0, electrode1.getShape());
        Assertions.assertEquals(0, electrode1.getPositionX());
        Assertions.assertEquals(0, electrode1.getPositionY());
        Assertions.assertEquals(20, electrode1.getSizeX());
        Assertions.assertEquals(20, electrode1.getSizeY());
        Assertions.assertEquals(0, electrode1.getStatus());
        Assertions.assertEquals(0, electrode1.getCorners().length);

        JsonDmfPlatformElectrode electrode2 = electrodes.get(1);
        Assertions.assertEquals("arrel2", electrode2.getName());
        Assertions.assertEquals(2, electrode2.getID());
        Assertions.assertEquals(370, electrode2.getElectrodeID());
        Assertions.assertEquals(0, electrode2.getDriverID());
        Assertions.assertEquals(0, electrode2.getShape());
        Assertions.assertEquals(20, electrode2.getPositionX());
        Assertions.assertEquals(0, electrode2.getPositionY());
        Assertions.assertEquals(20, electrode2.getSizeX());
        Assertions.assertEquals(20, electrode2.getSizeY());
        Assertions.assertEquals(0, electrode2.getStatus());
        Assertions.assertEquals(0, electrode2.getCorners().length);

       /* Assumes that the rest of the electrodes are correct */
    }

}
