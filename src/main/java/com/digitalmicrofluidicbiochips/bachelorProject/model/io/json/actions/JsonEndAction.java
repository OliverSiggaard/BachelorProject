package com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Simple DTO class for the EndAction, loaded from the JSON file.
 */
public class JsonEndAction extends JsonActionBase {
    @JsonCreator
    public JsonEndAction(
            @JsonProperty("id") String id
    ) {
        super(id);
    }
}
