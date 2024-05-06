package com.digitalmicrofluidicbiochips.bachelorProject.reader.json.model.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.reader.misc.StrictNonEmptyIntegerDeserializer;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.misc.StrictNonEmptyStringDeserializer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;

/*
 * Simple DTO class for the OutputAction, loaded from the JSON file.
 */
@Getter
public class JsonOutputAction extends JsonActionBase {
    @JsonDeserialize(using = StrictNonEmptyStringDeserializer.class)
    private final String dropletId;
    @JsonDeserialize(using = StrictNonEmptyIntegerDeserializer.class)
    private final int posX;
    @JsonDeserialize(using = StrictNonEmptyIntegerDeserializer.class)
    private final int posY;
    @JsonDeserialize(using = StrictNonEmptyStringDeserializer.class)
    private final String nextActionId;

    @JsonCreator
    public JsonOutputAction(
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
