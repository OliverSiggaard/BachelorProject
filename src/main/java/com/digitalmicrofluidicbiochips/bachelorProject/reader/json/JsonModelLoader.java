package com.digitalmicrofluidicbiochips.bachelorProject.reader.json;

import com.digitalmicrofluidicbiochips.bachelorProject.reader.json.model.actions.JsonActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.json.model.JsonProgramConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * This class is responsible for reading the programmed actions from a GUI-generated JSON file.
 * It reads the actions from a JSON file and converts them to a list of ActionBase objects.
 * <br>
 * The mapping of the JSON file to the ActionBase objects is done using the Jackson library.
 */
public class JsonModelLoader {
    public static JsonProgramConfiguration loadProgramConfigurationFromJson(File jsonFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        // Read the JSON file and convert it to a list of ActionBase objects
        JsonProgramConfiguration programConfiguration = objectMapper.readValue(jsonFile, JsonProgramConfiguration.class);

        return programConfiguration;
    }

    public static JsonProgramConfiguration loadProgramConfigurationFromJsonString(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        // Read the JSON string and convert it to a JsonProgramConfiguration object
        JsonProgramConfiguration programConfiguration = objectMapper.readValue(jsonString, JsonProgramConfiguration.class);

        return programConfiguration;
    }

}
