package com.digitalmicrofluidicbiochips.bachelorProject;

import com.digitalmicrofluidicbiochips.bachelorProject.mappers.json.JsonFileToInternalMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class BachelorProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(BachelorProjectApplication.class, args);
	}

	@CrossOrigin(origins = "http://localhost:3000") // Allows requests from localhost:3000
	@GetMapping("/root")
	public String apiRoot() {

		// Load in data from file, and execute the program, when client requests the root endpoint.
		//executeProgram();

		return "Hello from Spring Boot!";
	}

	/**
	 * Showcase of how to load in the data, and then create an executor, to execute the program.
	 * For now, the program is loaded directly from hardcoded JSON files, but in the future,
	 * this will of cause be loaded from the frontend (by sending it to the backend, using Spring Boot).
	 */
	private void executeProgram() {
		//The path to the JSON file containing the actions / dmf configuration (dmf config is currently loaded from a
		// separate file). See the JsonFileToInternalMapper for more information.
		String actionJsonFilePath = "src/main/resources/actions.json";

		// Get the JsonFileToInternalMapper, and use it to get the program configuration.
		JsonFileToInternalMapper fileMapper = new JsonFileToInternalMapper(actionJsonFilePath);
		ProgramConfiguration programConfiguration = fileMapper.getProgramConfiguration();

		// Create the executor, passing the program configuration to it.
		Executor executor = new Executor(programConfiguration);

		// Execute the program
		executor.startExecution();
	}



}