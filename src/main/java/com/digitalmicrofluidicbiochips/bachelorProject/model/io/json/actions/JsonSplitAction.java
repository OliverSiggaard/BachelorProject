package com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/*
 * Simple DTO class for the SplitAction, loaded from the JSON file.
 */
@Getter
public class JsonSplitAction extends JsonActionBase {

    private final String originDroplet;
    private final String resultDroplet1;
    private final String resultDroplet2;
    private final double ratio;
    private final int destX1;
    private final int destY1;
    private final int destX2;
    private final int destY2;
    private final String nextActionId;

    @JsonCreator
    public JsonSplitAction(
            @JsonProperty("id") String id,
            @JsonProperty("originDroplet") String originDroplet,
            @JsonProperty("resultDroplet1") String resultDroplet1,
            @JsonProperty("resultDroplet2") String resultDroplet2,
            @JsonProperty("ratio") double ratio,
            @JsonProperty("destX1") int destX1,
            @JsonProperty("destY1") int destY1,
            @JsonProperty("destX2") int destX2,
            @JsonProperty("destY2") int destY2,
            @JsonProperty("next") String nextActionId
    ) {
        super(id);
        this.originDroplet = originDroplet;
        this.resultDroplet1 = resultDroplet1;
        this.resultDroplet2 = resultDroplet2;
        this.ratio = ratio;
        this.destX1 = destX1;
        this.destY1 = destY1;
        this.destX2 = destX2;
        this.destY2 = destY2;
        this.nextActionId = nextActionId;
    }

}
