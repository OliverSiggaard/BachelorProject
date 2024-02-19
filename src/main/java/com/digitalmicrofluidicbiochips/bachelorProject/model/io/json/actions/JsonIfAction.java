package com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/*
 * Simple DTO class for the IfAction, loaded from the JSON file.
 */
@Getter
public class JsonIfAction extends JsonActionBase {
    private final String condition;
    private final String trueNextActionId;
    private final String falseNextActionId;

    @JsonCreator
    public JsonIfAction(
            @JsonProperty("id") String id,
            @JsonProperty("condition") String condition,
            @JsonProperty("trueNext") String trueNext,
            @JsonProperty("falseNext") String falseNext
    ) {
        super(id);
        this.condition = condition;
        this.trueNextActionId = trueNext;
        this.falseNextActionId = falseNext;
    }

}
