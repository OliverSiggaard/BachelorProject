package com.digitalmicrofluidicbiochips.bachelorProject.reader.misc;

import com.digitalmicrofluidicbiochips.bachelorProject.errors.DmfExceptionMessage;
import com.digitalmicrofluidicbiochips.bachelorProject.errors.DmfInvalidInputException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class StrictNonEmptyDoubleDeserializer extends JsonDeserializer<Double> {
    @Override
    public Double deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, DmfInvalidInputException {
        String value = p.getText();
        if (value == null || value.isEmpty()) {
            throw new DmfInvalidInputException(DmfExceptionMessage.ACTION_FIELD_EMPTY.getMessage());
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new DmfInvalidInputException(DmfExceptionMessage.ACTION_FIELD_DOUBLE_NOT_DOUBLE.getMessage());
        }
    }
}