package com.digitalmicrofluidicbiochips.bachelorProject.reader.json.model.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.reader.misc.StrictNonEmptyDoubleDeserializer;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.misc.StrictNonEmptyIntegerDeserializer;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.misc.StrictNonEmptyStringDeserializer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;

/*
 * Simple DTO class for the InputAction, loaded from the JSON file.
 */
@Getter
public class JsonInputAction extends JsonActionBase {
    @JsonDeserialize(using = StrictNonEmptyStringDeserializer.class)
    private final String dropletId;
    @JsonDeserialize(using = StrictNonEmptyIntegerDeserializer.class)
    private final int posX;
    @JsonDeserialize(using = StrictNonEmptyIntegerDeserializer.class)
    private final int posY;
    @JsonDeserialize(using = StrictNonEmptyDoubleDeserializer.class)
    private final double volume;
    @JsonDeserialize(using = StrictNonEmptyStringDeserializer.class)
    private final String nextActionId;

    @JsonCreator
    public JsonInputAction(
            @JsonProperty("id") String id,
            @JsonProperty("dropletId") String dropletId,
            @JsonProperty("posX") int posX,
            @JsonProperty("posY") int posY,
            @JsonProperty("volume") double volume,
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
