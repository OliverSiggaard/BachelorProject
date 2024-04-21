package com.digitalmicrofluidicbiochips.bachelorProject.restControllers;

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
        try {
            System.out.println("Received data from frontend: " + data);
            ExecutionResult executionResult = ProgramExecutionService.executeProgram(data);
            System.out.println("Executed program successfully!");

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment");
            headers.setContentType(MediaType.APPLICATION_JSON);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(executionResult);
        } catch (Exception e) {
            System.err.println("Error processing data: " + e.getMessage());
            return ResponseEntity.badRequest().body(new ExecutionResult("Error processing data: " + e.getMessage(), null));
        }
    }
}
