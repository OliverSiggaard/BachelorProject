package com.digitalmicrofluidicbiochips.bachelorProject.errors;

public class ExceptionHandler {

    public final static String UNKNOWN_ERROR_MESSAGE = "A non-recoverable error occurred while compiling the program.";

    public static String getErrorMessage(Exception e) {

        if(!(e instanceof DmfException)) {
            return UNKNOWN_ERROR_MESSAGE;
        }

        /*
         * Any DmfException-specific error messages should be handled here.
         */
        return e.getMessage();
    }



}
