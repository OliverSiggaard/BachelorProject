package com.digitalmicrofluidicbiochips.bachelorProject.reader.misc;

import com.digitalmicrofluidicbiochips.bachelorProject.errors.DmfExceptionMessage;
import com.digitalmicrofluidicbiochips.bachelorProject.errors.DmfInvalidInputException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class StrictNonEmptyStringDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, DmfInvalidInputException {
        String value = p.getText();
        if (value == null || value.isEmpty()) {
            throw new DmfInvalidInputException(DmfExceptionMessage.ACTION_FIELD_EMPTY.getMessage());
        }
        return value;
    }
}
