package com.digitalmicrofluidicbiochips.bachelorProject;

import com.digitalmicrofluidicbiochips.bachelorProject.errors.DmfExceptionMessage;
import com.digitalmicrofluidicbiochips.bachelorProject.executor.ExecutionResult;
import com.digitalmicrofluidicbiochips.bachelorProject.services.ProgramExecutionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class IntegrationTests {

    @Test
    void integration_SingleDropletMove10Right() {
        // Arrange
        String jsonProtocolString;
        try {
            jsonProtocolString = new String(Files.readAllBytes(Paths.get("src/test/resources/integrationTestProtocols/SingleMoveRight.json")));
        } catch (IOException e) {
            throw new RuntimeException("Was not able to read test file containing JSON protocol");
        }

        // Act
        ExecutionResult executionResult = ProgramExecutionService.executeProgram(jsonProtocolString);

        // Assert
        Assertions.assertNull(executionResult.getErrorMessage());
        int dropletsInDmfConfig = executionResult.getDmfConfiguration().get("droplets").size();

        Assertions.assertEquals(1, dropletsInDmfConfig);

        // 4 initial SETEL for input. Droplet is 2x2, moving 10 right, so 20 SETELs to move. 24 is expected.
        int setelCount = executionResult.getCompiledProgram().split("SETELI").length - 1;
        Assertions.assertEquals(24, setelCount);
        // no initial CLREL, Droplet is 2x2, moving 10 right, so 20 CLRELs to clear. 20 is expected.
        int clrelCount = executionResult.getCompiledProgram().split("CLRELI").length - 1;
        Assertions.assertEquals(20, clrelCount);
        // 1 tick for insert. 20 ticks for moving. 2 extra after TSTOP. 23 is expected.
        int ticks = executionResult.getCompiledProgram().split("TICK").length - 1;
        Assertions.assertEquals(23, ticks);
    }

    @Test
    void integration_DoubleDropletMove10Right() {
        // Arrange
        String jsonProtocolString;
        try {
            jsonProtocolString = new String(Files.readAllBytes(Paths.get("src/test/resources/integrationTestProtocols/DoubleMoveRight.json")));
        } catch (IOException e) {
            throw new RuntimeException("Was not able to read test file containing JSON protocol");
        }

        // Act
        ExecutionResult executionResult = ProgramExecutionService.executeProgram(jsonProtocolString);

        // Assert
        Assertions.assertNull(executionResult.getErrorMessage());
        Assertions.assertEquals(2, executionResult.getDmfConfiguration().get("droplets").size());

        // 2 droplets, both do: 4 initial SETEL for input. Droplet is 2x2, moving 10 right, so 20 SETELs to move. 48 is expected.
        Assertions.assertEquals(48, executionResult.getCompiledProgram().split("SETEL").length - 1);
        // 2 droplets, both do: no initial CLREL, Droplet is 2x2, moving 10 right, so 20 CLRELs to clear. 40 is expected.
        Assertions.assertEquals(40, executionResult.getCompiledProgram().split("CLREL").length - 1);
        // 1 tick for inserts. 20 ticks for moving. 2 extra after TSTOP. 23 is expected. Both droplets should move concurrently.
        Assertions.assertEquals(23, executionResult.getCompiledProgram().split("TICK").length - 1);
    }

    @Test
    void integration_SimpleDeadlock_noError() {
        // Arrange
        String jsonProtocolString;
        try {
            jsonProtocolString = new String(Files.readAllBytes(Paths.get("src/test/resources/integrationTestProtocols/SimpleDeadlockSolvable.json")));
        } catch (IOException e) {
            throw new RuntimeException("Was not able to read test file containing JSON protocol");
        }

        // Act
        ExecutionResult executionResult = ProgramExecutionService.executeProgram(jsonProtocolString);

        // Assert
        Assertions.assertNull(executionResult.getErrorMessage());
        Assertions.assertEquals(2, executionResult.getDmfConfiguration().get("droplets").size());

        // Deadlocks is not deterministic, so we cant check precise number of ticks / Commands.
        int ticks = executionResult.getCompiledProgram().split("TICK").length - 1;
        Assertions.assertTrue(ticks > 10 );
        Assertions.assertTrue(ticks < 30 );
    }

    @Test
    void integration_UnresolvableDeadlock_deadlockError() {
        // Arrange
        String jsonProtocolString;
        try {
            jsonProtocolString = new String(Files.readAllBytes(Paths.get("src/test/resources/integrationTestProtocols/UnresolvableDeadlock.json")));
        } catch (IOException e) {
            throw new RuntimeException("Was not able to read test file containing JSON protocol");
        }

        // Act
        ExecutionResult executionResult = ProgramExecutionService.executeProgram(jsonProtocolString);

        // Assert
        Assertions.assertEquals(DmfExceptionMessage.PROGRAM_GOT_STUCK.getMessage(), executionResult.getErrorMessage());

        // ensure partial program is still returned

        // there is 2 droplets.
        Assertions.assertEquals(2, executionResult.getDmfConfiguration().get("droplets").size());

        // 2 droplets, both do: 13 inserts on intial insert. Droplet is 2x2, moving 6 right, so 12 SETELs to move. 25 is expected.
        Assertions.assertEquals(25, executionResult.getCompiledProgram().split("SETEL").length - 1);
        // 2 droplets, both do: no initial CLREL, Droplet is 2x2, moving 6 right, so 12 CLRELs to clear. 12 is expected.
        Assertions.assertEquals(12, executionResult.getCompiledProgram().split("CLREL").length - 1);

        // 1 tick for intial insert. drop1 can move 6 right, using 2 ticks per step. 2 ticks after TSTOP.
        int ticks = executionResult.getCompiledProgram().split("TICK").length - 1;
        Assertions.assertEquals(15, ticks);
    }

    @Test
    void integration_DilutionSeriesCaseStudy_noErrors() {
        // Arrange
        String jsonProtocolString;
        try {
            jsonProtocolString = new String(Files.readAllBytes(Paths.get("src/test/resources/integrationTestProtocols/DilutionSeriesCaseStudy.json")));
        } catch (IOException e) {
            throw new RuntimeException("Was not able to read test file containing JSON protocol");
        }

        // Act
        ExecutionResult executionResult = ProgramExecutionService.executeProgram(jsonProtocolString);

        // Assert

        // Ensure no error is given.
        Assertions.assertNull(executionResult.getErrorMessage());

        // Initially 2 droplets is inserted.
        Assertions.assertEquals(2, executionResult.getDmfConfiguration().get("droplets").size());

        // Exact number of commands and ticks may change, if code is changed. But it should be over 500.
        int setelCount = executionResult.getCompiledProgram().split("SETEL").length - 1;
        Assertions.assertTrue(setelCount > 500);

        int clrelCount = executionResult.getCompiledProgram().split("CLREL").length - 1;
        Assertions.assertTrue(clrelCount > 500);

        int ticks = executionResult.getCompiledProgram().split("TICK").length - 1;
        Assertions.assertTrue(ticks > 500);
    }





}
