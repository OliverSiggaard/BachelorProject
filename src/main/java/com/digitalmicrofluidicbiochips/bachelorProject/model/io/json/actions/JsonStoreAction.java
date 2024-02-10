package com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/*
 * Simple DTO class for the StoreAction, loaded from the JSON file.
 */
@Getter
public class JsonStoreAction extends JsonActionBase {

    private final String dropletId;
    private final int posX;
    private final int posY;
    private final int time;
    private final String nextActionId;

    @JsonCreator
    public JsonStoreAction(
            @JsonProperty("id") String id,
            @JsonProperty("dropletId") String dropletId,
            @JsonProperty("posX") int posX,
            @JsonProperty("posY") int posY,
            @JsonProperty("time") int time,
            @JsonProperty("next") String nextActionId
    ) {
        super(id);
        this.dropletId = dropletId;
        this.posX = posX;
        this.posY = posY;
        this.time = time;
        this.nextActionId = nextActionId;
    }

}
