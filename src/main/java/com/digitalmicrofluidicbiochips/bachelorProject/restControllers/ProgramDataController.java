package com.digitalmicrofluidicbiochips.bachelorProject.restControllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class ProgramDataController {

    // Temporary POC
    @GetMapping("/test")
    public String apiRoot() {
        return "Hello from Spring Boot!";
    }

    @PostMapping("/program")
    public ResponseEntity<String> dataFromFrontend(@RequestBody String data) {
        try {
            System.out.println("Received data from frontend: " + data);

            // TODO: Execute the program here with the data received from the frontend - using the ProgramExecutionService
            // programExecutionService.executeProgram(data);

            return ResponseEntity.ok("Data recieved successfully!");
        } catch (Exception e) {
            System.err.println("Error processing data: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error processing data: " + e.getMessage());
        }
    }
}
