package com.digitalmicrofluidicbiochips.bachelorProject.services;

import com.digitalmicrofluidicbiochips.bachelorProject.errors.DmfException;
import com.digitalmicrofluidicbiochips.bachelorProject.executor.ExecutionResult;
import com.digitalmicrofluidicbiochips.bachelorProject.executor.Executor;
import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.mappers.json.JsonToInternalMapper;
import org.springframework.stereotype.Service;

@Service
public class ProgramExecutionService {

    /**
     * Takes data sent from the frontend, creates an executor and then executes the program.
     *
     * @param jsonString - the data received from the frontend (the program)
     */
    public static ExecutionResult executeProgram(String jsonString) {
        try {
            // Get the JsonFileToInternalMapper, and use it to get the program configuration.
            JsonToInternalMapper dataMapper = new JsonToInternalMapper(jsonString);
            ProgramConfiguration programConfiguration = dataMapper.getProgramConfiguration();

            // Create the executor, passing the program configuration to it.
            Executor executor = new Executor(programConfiguration);

            // Execute the program
            return executor.compileProgramToDmf();
        } catch (DmfException e) {
            // If there is a DMF exception, return the error message
            return new ExecutionResult(e.getMessage());
        }
    }
}
