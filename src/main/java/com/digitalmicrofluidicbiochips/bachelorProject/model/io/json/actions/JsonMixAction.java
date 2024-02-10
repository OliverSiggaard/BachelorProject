package com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/*
 * Simple DTO class for the MixAction, loaded from the JSON file.
 */
@Getter
public class JsonMixAction extends JsonActionBase {

    private final String dropletId;
    private final String nextActionId;

    @JsonCreator
    public JsonMixAction(
            @JsonProperty("id") String id,
            @JsonProperty("dropletId") String dropletId,
            @JsonProperty("next") String nextActionId
    ) {
        super(id);
        this.dropletId = dropletId;
        this.nextActionId = nextActionId;
    }

}
