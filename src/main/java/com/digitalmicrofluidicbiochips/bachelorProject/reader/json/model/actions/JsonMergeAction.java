package com.digitalmicrofluidicbiochips.bachelorProject.reader.json.model.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.reader.misc.StrictNonEmptyIntegerDeserializer;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.misc.StrictNonEmptyStringDeserializer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;

/*
 * Simple DTO class for the MergeAction, loaded from the JSON file.
 */
@Getter
public class JsonMergeAction extends JsonActionBase {
    @JsonDeserialize(using = StrictNonEmptyStringDeserializer.class)
    private final String resultDropletId;
    @JsonDeserialize(using = StrictNonEmptyStringDeserializer.class)
    private final String dropletId1;
    @JsonDeserialize(using = StrictNonEmptyStringDeserializer.class)
    private final String dropletId2;
    @JsonDeserialize(using = StrictNonEmptyIntegerDeserializer.class)
    private final int posX;
    @JsonDeserialize(using = StrictNonEmptyIntegerDeserializer.class)
    private final int posY;
    @JsonDeserialize(using = StrictNonEmptyStringDeserializer.class)
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
