package com.digitalmicrofluidicbiochips.bachelorProject.reader.json.model.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/*
 * Simple DTO class for the StartAction, loaded from the JSON file.
 */
@Getter
public class JsonStartAction extends JsonActionBase {
    private final String nextActionId;

    @JsonCreator
    public JsonStartAction(
            @JsonProperty("id") String id,
            @JsonProperty("next") String nextActionId
    ) {
        super(id);

        this.nextActionId = nextActionId;
    }

}
