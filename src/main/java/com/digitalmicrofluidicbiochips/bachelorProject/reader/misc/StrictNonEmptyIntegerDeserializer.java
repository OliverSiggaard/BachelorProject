package com.digitalmicrofluidicbiochips.bachelorProject.reader.misc;

import com.digitalmicrofluidicbiochips.bachelorProject.errors.DmfExceptionMessage;
import com.digitalmicrofluidicbiochips.bachelorProject.errors.DmfInputReaderException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class StrictNonEmptyIntegerDeserializer extends JsonDeserializer<Integer> {
    @Override
    public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, DmfInputReaderException {
        String value = p.getText();
        if (value == null || value.isEmpty()) {
            throw new DmfInputReaderException(DmfExceptionMessage.ACTION_FIELD_EMPTY.getMessage());
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new DmfInputReaderException(DmfExceptionMessage.ACTION_FIELD_INTEGER_NOT_INTEGER.getMessage());
        }
    }
}
