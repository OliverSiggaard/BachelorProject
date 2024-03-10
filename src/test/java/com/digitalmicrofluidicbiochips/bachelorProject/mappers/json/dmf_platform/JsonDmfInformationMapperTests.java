package com.digitalmicrofluidicbiochips.bachelorProject.mappers.json.dmf_platform;

import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Electrode;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.PlatformInformation;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.json.model.JsonProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.json.model.dmf_platform.JsonDmfPlatformElectrode;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.json.model.dmf_platform.JsonDmfPlatformState;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.json.JsonModelLoader;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.mappers.json.dmf_platform.JsonDmfInformationMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class JsonDmfInformationMapperTests {

    private PlatformInformation platformInfo;
    private JsonDmfPlatformState jsonInfo;

    private final JsonDmfInformationMapper sut = new JsonDmfInformationMapper();

    private final String filePath = "src/test/resources/reader/dmf_configuration.JSON"; // Adjust the file name if needed

    @BeforeEach
    public void setUp() {
        File programFile = new File(filePath);
        if (!programFile.exists()) {
            Assertions.fail("The program file does not exist: " + filePath);
        }

        try {
            JsonProgramConfiguration programConfiguration = JsonModelLoader.loadProgramConfigurationFromJson(filePath);
            jsonInfo = programConfiguration.getDmfPlatformState();
            platformInfo = sut.mapToInternal(jsonInfo);
        } catch (IOException e) {
            Assertions.fail("Could not read the program configuration from the JSON file. Error: " + e.getMessage());
        }
    }

    // Test if the information is read correctly
    @Test
    public void platformSizeReadCorrect() {
        Assertions.assertEquals(860, platformInfo.getSizeX());
        Assertions.assertEquals(400, platformInfo.getSizeY());
    }

    // Test the correct number of electrodes is read
    @Test
    public void correctNumberOfElectrodesRead() {
        Assertions.assertEquals(724, platformInfo.getElectrodes().size());
    }

    @Test
    public void jsonElectrodesIsMappedCorrectly() {
       for(int i = 0; i < platformInfo.getElectrodes().size(); i++){
           Electrode e = platformInfo.getElectrodes().get(i);
           JsonDmfPlatformElectrode j = jsonInfo.getElectrodes().get(i);

           Assertions.assertEquals(e.getName(), j.getName());
           Assertions.assertEquals(e.getID(), j.getID());
           Assertions.assertEquals(e.getElectrodeID(), j.getElectrodeID());
           Assertions.assertEquals(e.getDriverID(), j.getDriverID());
           Assertions.assertEquals(e.getPositionX(), j.getPositionX());
           Assertions.assertEquals(e.getPositionY(), j.getPositionY());
           Assertions.assertEquals(e.getSizeX(), j.getSizeX());
           Assertions.assertEquals(e.getSizeY(), j.getSizeY());
           Assertions.assertEquals(e.getStatus(), j.getStatus());
       }
    }








}
