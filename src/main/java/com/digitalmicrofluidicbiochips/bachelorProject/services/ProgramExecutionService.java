package com.digitalmicrofluidicbiochips.bachelorProject.services;

import com.digitalmicrofluidicbiochips.bachelorProject.executor.Executor;
import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.mappers.json.JsonToInternalMapper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class ProgramExecutionService {

    /**
     * Showcase of how to load in the data, and then create an executor, to execute the program.
     * For now, the program is loaded directly from hardcoded JSON files, but in the future,
     * this will of cause be loaded from the frontend (by sending it to the backend, using Spring Boot).
     *
     * @param jsonString - the data received from the frontend (the program)
     */
    public static void executeProgram(String jsonString) {

        // Get the JsonFileToInternalMapper, and use it to get the program configuration.
        JsonToInternalMapper dataMapper = new JsonToInternalMapper(jsonString);
        ProgramConfiguration programConfiguration = dataMapper.getProgramConfiguration();

        // Create the executor, passing the program configuration to it.
        Executor executor = new Executor(programConfiguration);

        // Execute the program
        executor.startExecution();
    }
}
