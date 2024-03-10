package com.digitalmicrofluidicbiochips.bachelorProject.reader.json.model.actions;

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
    private final int posX;
    private final int posY;
    private final int sizeX;
    private final int sizeY;

    @JsonCreator
    public JsonMixAction(
            @JsonProperty("id") String id,
            @JsonProperty("dropletId") String dropletId,
            @JsonProperty("next") String nextActionId,
            @JsonProperty("posX") int posX,
            @JsonProperty("posY") int posY,
            @JsonProperty("sizeX") int sizeX,
            @JsonProperty("sizeY") int sizeY
    ) {
        super(id);
        this.dropletId = dropletId;
        this.nextActionId = nextActionId;
        this.posX = posX;
        this.posY = posY;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

}
