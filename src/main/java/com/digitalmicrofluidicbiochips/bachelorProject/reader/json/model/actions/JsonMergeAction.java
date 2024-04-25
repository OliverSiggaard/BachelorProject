package com.digitalmicrofluidicbiochips.bachelorProject.reader.json.model.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/*
 * Simple DTO class for the MergeAction, loaded from the JSON file.
 */
@Getter
public class JsonMergeAction extends JsonActionBase {
    private final String resultDropletId;
    private final String dropletId1;
    private final String dropletId2;
    private final int posX;
    private final int posY;
    private final String nextActionId;

    @JsonCreator
    public JsonMergeAction(
            @JsonProperty("id") String id,
            @JsonProperty("resultDropletId") String resultDropletId,
            @JsonProperty("originDropletId1") String dropletId1,
            @JsonProperty("originDropletId2") String dropletId2,
            @JsonProperty("posX") int posX,
            @JsonProperty("posY") int posY,
            @JsonProperty("next") String nextActionId
    ) {
        super(id);
        this.resultDropletId = resultDropletId;
        this.dropletId1 = dropletId1;
        this.dropletId2 = dropletId2;
        this.posX = posX;
        this.posY = posY;
        this.nextActionId = nextActionId;
    }

}
