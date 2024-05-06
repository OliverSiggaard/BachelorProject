package com.digitalmicrofluidicbiochips.bachelorProject.reader.json.model.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.reader.misc.StrictNonEmptyIntegerDeserializer;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.misc.StrictNonEmptyStringDeserializer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;

/*
 * Simple DTO class for the StoreAction, loaded from the JSON file.
 */
@Getter
public class JsonStoreAction extends JsonActionBase {
    @JsonDeserialize(using = StrictNonEmptyStringDeserializer.class)
    private final String dropletId;
    @JsonDeserialize(using = StrictNonEmptyIntegerDeserializer.class)
    private final int posX;
    @JsonDeserialize(using = StrictNonEmptyIntegerDeserializer.class)
    private final int posY;
    @JsonDeserialize(using = StrictNonEmptyIntegerDeserializer.class)
    private final int time;
    @JsonDeserialize(using = StrictNonEmptyStringDeserializer.class)
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
