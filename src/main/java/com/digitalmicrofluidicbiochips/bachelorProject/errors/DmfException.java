package com.digitalmicrofluidicbiochips.bachelorProject.errors;

/**
 * This class is the default exception thrown, when the DMF encounters an error.
 * All DMF specific exceptions should extend this class.
 */
public class DmfException extends RuntimeException {
    public DmfException(String errorMessage) {
        super(errorMessage);
    }
}
