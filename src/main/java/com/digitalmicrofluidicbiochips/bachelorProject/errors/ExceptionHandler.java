package com.digitalmicrofluidicbiochips.bachelorProject.errors;

public class ExceptionHandler {

    public static String getErrorMessage(Exception e) {

        if(!(e instanceof DmfException)) {
            return DmfExceptionMessage.UNKNOWN_ERROR_MESSAGE.getMessage();
        }

        /*
         * Any DmfException-specific error messages should be handled here.
         */
        return e.getMessage();
    }

}
