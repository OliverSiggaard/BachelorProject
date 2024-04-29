package com.digitalmicrofluidicbiochips.bachelorProject.executor;

import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.ActionTickResult;
import com.digitalmicrofluidicbiochips.bachelorProject.utils.TickResultsToStringConverter;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ExecutionResult {
    private final String compiledProgramString;
    private final JsonNode dmfConfiguration;
    @Setter
    private String errorMessage;

    public ExecutionResult(List<ActionTickResult> tickResults, JsonNode dmfConfiguration) {
        this.compiledProgramString = getCompiledProgram(tickResults);
        this.dmfConfiguration = dmfConfiguration;
        this.errorMessage = null;
    }

    public ExecutionResult(String error) {
        this.compiledProgramString = "";
        this.dmfConfiguration = null;
        this.errorMessage = error;
    }

    private String getCompiledProgram(List<ActionTickResult> tickResults) {
        return TickResultsToStringConverter.convertTickResultsToString(tickResults);
    }

    public boolean hasError() {
        return errorMessage != null;
    }

}
