package com.digitalmicrofluidicbiochips.bachelorProject.reader.json.model.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/*
 * Simple DTO class for the MoveAction, loaded from the JSON file.
 */
@Getter
public class JsonMoveAction extends JsonActionBase {

    private final String dropletId;
    private final int posX;
    private final int posY;
    private final String nextActionId;

    @JsonCreator
    public JsonMoveAction(
            @JsonProperty("id") String id,
            @JsonProperty("dropletId") String dropletId,
            @JsonProperty("posX") int posX,
            @JsonProperty("posY") int posY,
            @JsonProperty("next") String nextActionId
    ) {
        super(id);
        this.dropletId = dropletId;
        this.posX = posX;
        this.posY = posY;
        this.nextActionId = nextActionId;
    }

}
