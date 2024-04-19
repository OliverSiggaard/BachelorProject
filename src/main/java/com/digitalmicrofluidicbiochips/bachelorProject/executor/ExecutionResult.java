package com.digitalmicrofluidicbiochips.bachelorProject.executor;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

@Getter
public class ExecutionResult {
    private final String compiledProgram;
    private final JsonNode dmfConfiguration;

    public ExecutionResult(String tickResults, JsonNode dmfConfiguration) {
        this.compiledProgram = tickResults;
        this.dmfConfiguration = dmfConfiguration;
    }
}
