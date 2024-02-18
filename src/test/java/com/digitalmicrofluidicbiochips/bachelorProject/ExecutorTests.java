package com.digitalmicrofluidicbiochips.bachelorProject;

import com.digitalmicrofluidicbiochips.bachelorProject.mappers.json.JsonFileToInternalMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.model.Executor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ExecutorTests {

    private final String filePath = "src/test/resources/reader/simpleActionModel.JSON";

    private Executor executor;

    @BeforeEach
    public void setUp() {
        JsonFileToInternalMapper mapper = new JsonFileToInternalMapper(filePath);
        executor = new Executor(mapper.getProgramConfiguration());
    }

    @Test
    public void programConfigIsPassedCorrectly() {

    }




}
