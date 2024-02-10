package com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/*
 * Simple DTO class for the MoveAction, loaded from the JSON file.
 */
@Getter
public class JsonMoveAction extends JsonActionBase {

    private final String dropletId;
    private final int destX;
    private final int destY;
    private final String nextActionId;

    @JsonCreator
    public JsonMoveAction(
            @JsonProperty("id") String id,
            @JsonProperty("dropletId") String dropletId,
            @JsonProperty("destX") int destX,
            @JsonProperty("destY") int destY,
            @JsonProperty("next") String nextActionId
    ) {
        super(id);
        this.dropletId = dropletId;
        this.destX = destX;
        this.destY = destY;
        this.nextActionId = nextActionId;
    }

}
