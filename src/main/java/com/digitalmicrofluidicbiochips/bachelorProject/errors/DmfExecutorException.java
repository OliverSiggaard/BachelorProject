package com.digitalmicrofluidicbiochips.bachelorProject.errors;

/**
 * This class is used to throw an error when the executor encounters an error.
 * This could be a deadlock, unsolvable state, or any other error that the compiler encounters.
 */
public class DmfExecutorException extends DmfException {
    public DmfExecutorException(String errorMessage) {
        super(errorMessage);
    }
}
