package com.digitalmicrofluidicbiochips.bachelorProject.reader.json.model.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.reader.misc.StrictNonEmptyIntegerDeserializer;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.misc.StrictNonEmptyStringDeserializer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;

/*
 * Simple DTO class for the MixAction, loaded from the JSON file.
 */
@Getter
public class JsonMixAction extends JsonActionBase {
    @JsonDeserialize(using = StrictNonEmptyStringDeserializer.class)
    private final String dropletId;
    @JsonDeserialize(using = StrictNonEmptyStringDeserializer.class)
    private final String nextActionId;
    @JsonDeserialize(using = StrictNonEmptyIntegerDeserializer.class)
    private final int posX;
    @JsonDeserialize(using = StrictNonEmptyIntegerDeserializer.class)
    private final int posY;
    @JsonDeserialize(using = StrictNonEmptyIntegerDeserializer.class)
    private final int sizeX;
    @JsonDeserialize(using = StrictNonEmptyIntegerDeserializer.class)
    private final int sizeY;

    @JsonCreator
    public JsonMixAction(
            @JsonProperty("id") String id,
            @JsonProperty("dropletId") String dropletId,
            @JsonProperty("next") String nextActionId,
            @JsonProperty("posX") int posX,
            @JsonProperty("posY") int posY,
            @JsonProperty("xSize") int sizeX,
            @JsonProperty("ySize") int sizeY
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
