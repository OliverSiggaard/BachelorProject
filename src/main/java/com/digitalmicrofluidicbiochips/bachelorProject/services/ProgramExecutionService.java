package com.digitalmicrofluidicbiochips.bachelorProject.services;

import com.digitalmicrofluidicbiochips.bachelorProject.Executor;
import com.digitalmicrofluidicbiochips.bachelorProject.mappers.json.JsonFileToInternalMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import org.springframework.stereotype.Service;

@Service
public class ProgramExecutionService {

    /**
     * Showcase of how to load in the data, and then create an executor, to execute the program.
     * For now, the program is loaded directly from hardcoded JSON files, but in the future,
     * this will of cause be loaded from the frontend (by sending it to the backend, using Spring Boot).
     *
     * @param data - the data received from the frontend (the program)
     */
    public void executeProgram(String data) {
        //The path to the JSON file containing the actions / dmf configuration (dmf config is currently loaded from a
        // separate file). See the JsonFileToInternalMapper for more information.
        String actionJsonFilePath = "src/main/resources/actions.json";

        // Get the JsonFileToInternalMapper, and use it to get the program configuration.
        JsonFileToInternalMapper fileMapper = new JsonFileToInternalMapper(actionJsonFilePath);
        ProgramConfiguration programConfiguration = fileMapper.getProgramConfiguration();

        // Create the executor, passing the program configuration to it.
        Executor executor = new Executor(programConfiguration);

        // Execute the program
        executor.startExecution();
    }
}
