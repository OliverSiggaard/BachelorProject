package com.digitalmicrofluidicbiochips.bachelorProject.restControllers;

import com.digitalmicrofluidicbiochips.bachelorProject.services.ProgramExecutionService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class ProgramDataController {

    @PostMapping("/compile")
    public ResponseEntity<String> dataFromFrontend(@RequestBody String data) {
        try {
            System.out.println("Received data from frontend: " + data);
            String compiledProgram = ProgramExecutionService.executeProgram(data);
            System.out.println("Executed program successfully!");

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=compiled_program.basm");
            headers.setContentType(new MediaType("application", "text-plain", StandardCharsets.UTF_8));

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(compiledProgram);
        } catch (Exception e) {
            System.err.println("Error processing data: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error processing data: " + e.getMessage());
        }
    }
}
