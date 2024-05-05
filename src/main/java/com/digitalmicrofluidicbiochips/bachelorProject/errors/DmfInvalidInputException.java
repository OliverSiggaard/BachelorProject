package com.digitalmicrofluidicbiochips.bachelorProject.errors;

/**
 * This class is used to throw an error when the parser encounters an error.
 * This could be a syntax error, a parsing error, or any other error that causes
 * the parser to not being able to parse the received input.
 */
public class DmfInvalidInputException extends DmfException {
    public DmfInvalidInputException(String errorMessage) {
        super(errorMessage);
    }
}
