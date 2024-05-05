package com.digitalmicrofluidicbiochips.bachelorProject;

import com.digitalmicrofluidicbiochips.bachelorProject.executor.Executor;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.mappers.json.JsonToInternalMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;

public class ExecutorTests {

    private final String filePath = "src/test/resources/reader/simpleActionModel.JSON";
    private Executor executor;

    @BeforeEach
    public void setUp() {
        File programFile = new File(filePath);

        try {
            JsonToInternalMapper mapper = new JsonToInternalMapper(programFile);
            executor = new Executor(mapper.getProgramConfiguration());
        } catch (Exception e) {
            Assertions.fail("Could not map the JSON file to the internal model. Error: " + e.getMessage());
        }
    }
}
