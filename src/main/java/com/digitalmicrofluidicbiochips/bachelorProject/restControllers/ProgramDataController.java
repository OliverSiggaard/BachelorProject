package com.digitalmicrofluidicbiochips.bachelorProject.restControllers;

import com.digitalmicrofluidicbiochips.bachelorProject.errors.DmfExceptionMessage;
import com.digitalmicrofluidicbiochips.bachelorProject.executor.ExecutionResult;
import com.digitalmicrofluidicbiochips.bachelorProject.services.ProgramExecutionService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class ProgramDataController {

    @PostMapping("/compile")
    public ResponseEntity<ExecutionResult> dataFromFrontend(@RequestBody String data) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment");
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            System.out.println("Received data from frontend: " + data);
            ExecutionResult executionResult = ProgramExecutionService.executeProgram(data);

            // Return error message if there is an error in the execution result
            if(executionResult.hasError()) {
                System.err.println(executionResult.getErrorMessage());
                return ResponseEntity.badRequest()
                        .headers(headers)
                        .body(executionResult);
            }

            // Otherwise, return the successful execution result
            System.out.println("Executed program successfully!");
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(executionResult);

        } catch (Exception e) { // Catch any exception that is not caught by the program (this should not happen)
            System.err.println("Error processing data: " + e.getMessage());
            ExecutionResult executionResult = new ExecutionResult(DmfExceptionMessage.UNKNOWN_ERROR_MESSAGE.getMessage());
            return ResponseEntity.badRequest()
                    .headers(headers)
                    .body(executionResult);
        }
    }
}
