package com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/*
 * Simple DTO class for the InputAction, loaded from the JSON file.
 */
@Getter
public class JsonInputAction extends JsonActionBase {

    private final String dropletId;
    private final int posX;
    private final int posY;
    private final int volume;
    private final String nextActionId;

    @JsonCreator
    public JsonInputAction(
            @JsonProperty("id") String id,
            @JsonProperty("dropletId") String dropletId,
            @JsonProperty("posX") int posX,
            @JsonProperty("posY") int posY,
            @JsonProperty("volume") int volume,
            @JsonProperty("next") String nextActionId
    ) {
        super(id);
        this.dropletId = dropletId;
        this.posX = posX;
        this.posY = posY;
        this.volume = volume;
        this.nextActionId = nextActionId;
    }

}
