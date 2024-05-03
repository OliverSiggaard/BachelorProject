package com.digitalmicrofluidicbiochips.bachelorProject.errors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ExceptionHandlerTests {

    @Test
    public void testExceptionHandler() {
        String errorMessage = "Test";
        Exception exception = new Exception(errorMessage);
        Assertions.assertEquals(errorMessage, exception.getMessage());
        Assertions.assertEquals(ExceptionHandler.UNKNOWN_ERROR_MESSAGE, ExceptionHandler.getErrorMessage(exception));
    }

    @Test
    public void testDmfCompilerException() {
        String errorMessage = "Test";
        DmfCompilerException exception = new DmfCompilerException(errorMessage);
        Assertions.assertEquals(errorMessage, exception.getMessage());
        Assertions.assertEquals(errorMessage, ExceptionHandler.getErrorMessage(exception));
    }

    @Test
    public void testDmfException() {
        String errorMessage = "Test";
        DmfException exception = new DmfException(errorMessage);
        Assertions.assertEquals(errorMessage, exception.getMessage());
        Assertions.assertEquals(errorMessage, ExceptionHandler.getErrorMessage(exception));
    }

    @Test
    public void testDmfParsingException() {
        String errorMessage = "Test";
        DmfParsingException exception = new DmfParsingException(errorMessage);
        Assertions.assertEquals(errorMessage, exception.getMessage());
        Assertions.assertEquals(errorMessage, ExceptionHandler.getErrorMessage(exception));
    }

}
