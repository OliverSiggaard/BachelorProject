package com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/*
 * Simple DTO class for the RepeatAction, loaded from the JSON file.
 */
@Getter
public class JsonRepeatAction extends JsonActionBase {
    private final int repeatCount;
    private final String RepeatNextActionId;
    private final String exitNextActionId;

    @JsonCreator
    public JsonRepeatAction(
            @JsonProperty("id") String id,
            @JsonProperty("times") int repeatCount,
            @JsonProperty("nextRepeat") String nextRepeatId,
            @JsonProperty("nextExit") String nextExitId
    ) {
        super(id);
        this.repeatCount = repeatCount;
        this.RepeatNextActionId = nextRepeatId;
        this.exitNextActionId = nextExitId;
    }

}
