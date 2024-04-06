package com.digitalmicrofluidicbiochips.bachelorProject.restControllers;

import com.digitalmicrofluidicbiochips.bachelorProject.services.ProgramExecutionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        String compiledProgram = "compiled program";
        when(ProgramExecutionService.executeProgram(anyString())).thenReturn(compiledProgram);

        ResponseEntity<String> response = programDataController.dataFromFrontend(testProgram);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(compiledProgram, response.getBody());
    }

    @Test
    void dataRecievedFromFrontend_Failure() {
        String testProgram = "invalid program";
        String errorMessage = "Error occurred";
        when(ProgramExecutionService.executeProgram(anyString()))
                .thenThrow(new RuntimeException(errorMessage));

        ResponseEntity<String> response = programDataController.dataFromFrontend(testProgram);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error processing data: " + errorMessage, response.getBody());
    }
}
