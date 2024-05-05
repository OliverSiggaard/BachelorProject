package com.digitalmicrofluidicbiochips.bachelorProject.restControllers;

import com.digitalmicrofluidicbiochips.bachelorProject.errors.DmfExceptionMessage;
import com.digitalmicrofluidicbiochips.bachelorProject.executor.ExecutionResult;
import com.digitalmicrofluidicbiochips.bachelorProject.services.ProgramExecutionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ProgramDataControllerTest {

    @InjectMocks
    private ProgramDataController programDataController;

    private MockedStatic<ProgramExecutionService> programExecutionServiceMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        programExecutionServiceMock = mockStatic(ProgramExecutionService.class);
    }

    @AfterEach
    void tearDown() {
        programExecutionServiceMock.close();
    }

    @Test
    void dataRetrievedFromFrontend_Success() {
        String testProgram = "{\"program_actions\":[{\"action\":\"input\",\"id\":1,\"next\":-1,\"dropletId\":\"2\",\"posX\":\"2\",\"posY\":\"2\",\"volume\":\"1\"},{\"action\":\"move\",\"id\":0,\"next\":1,\"dropletId\":\"2\",\"posX\":\"10\",\"posY\":\"2\"}]}";
        String testProgramConfiguration = "{\"key\":\"value\"}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode mockProgramConfigurationJsonNode = objectMapper.valueToTree(testProgramConfiguration);

        ExecutionResult executionResult = new ExecutionResult(new ArrayList<>(), mockProgramConfigurationJsonNode);
        try {
            when(ProgramExecutionService.executeProgram(anyString())).thenReturn(executionResult);
        } catch (Exception ignored) {
            /* Ignored */
        }

        ResponseEntity<ExecutionResult> response = programDataController.dataFromFrontend(testProgram);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(executionResult.getCompiledProgram(), Objects.requireNonNull(response.getBody()).getCompiledProgram());
        assertEquals(executionResult.getDmfConfiguration(), Objects.requireNonNull(response.getBody()).getDmfConfiguration());
    }

    @Test
    void dataRecievedFromFrontend_Failure() {
        String testProgram = "invalid program";
        String errorMessage = "Error occurred";
        try {
            when(ProgramExecutionService.executeProgram(anyString()))
                    .thenThrow(new RuntimeException(errorMessage));
        } catch (Exception ignored) {
            /* Ignored */
        }

        ResponseEntity<ExecutionResult> response = programDataController.dataFromFrontend(testProgram);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(DmfExceptionMessage.UNKNOWN_ERROR_MESSAGE.getMessage(), Objects.requireNonNull(response.getBody()).getErrorMessage());
        assertEquals("", Objects.requireNonNull(response.getBody()).getCompiledProgram());
        assertNull(Objects.requireNonNull(response.getBody()).getDmfConfiguration());
    }
}
