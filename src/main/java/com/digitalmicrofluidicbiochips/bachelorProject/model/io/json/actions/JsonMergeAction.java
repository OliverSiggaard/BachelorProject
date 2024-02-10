package com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.actions;

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
    private final int destX;
    private final int destY;
    private final String nextActionId;

    @JsonCreator
    public JsonMergeAction(
            @JsonProperty("id") String id,
            @JsonProperty("resultDropletId") String resultDropletId,
            @JsonProperty("dropletId1") String dropletId1,
            @JsonProperty("dropletId2") String dropletId2,
            @JsonProperty("destX") int destX,
            @JsonProperty("destY") int destY,
            @JsonProperty("next") String nextActionId
    ) {
        super(id);
        this.resultDropletId = resultDropletId;
        this.dropletId1 = dropletId1;
        this.dropletId2 = dropletId2;
        this.destX = destX;
        this.destY = destY;
        this.nextActionId = nextActionId;
    }

}
