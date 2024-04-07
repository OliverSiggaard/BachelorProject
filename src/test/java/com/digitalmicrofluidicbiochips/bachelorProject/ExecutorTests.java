package com.digitalmicrofluidicbiochips.bachelorProject;

import com.digitalmicrofluidicbiochips.bachelorProject.executor.Executor;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.mappers.json.JsonToInternalMapper;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;

public class ExecutorTests {

    private final String filePath = "src/test/resources/reader/simpleActionModel.JSON";
    private Executor executor;

    @BeforeEach
    public void setUp() {
        File programFile = new File(filePath);
        JsonToInternalMapper mapper = new JsonToInternalMapper(programFile);
        executor = new Executor(mapper.getProgramConfiguration());
    }
}
