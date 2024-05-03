package com.digitalmicrofluidicbiochips.bachelorProject.errors;

/**
 * This class is used to throw an error when the compiler encounters an error.
 * This could be a deadlock, unsolvable state, or any other error that the compiler encounters.
 */
public class DmfCompilerException extends DmfException {
    public DmfCompilerException(String errorMessage) {
        super(errorMessage);
    }
}
