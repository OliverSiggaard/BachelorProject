package com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/*
 * Simple DTO class for the SplitAction, loaded from the JSON file.
 */
@Getter
public class JsonSplitAction extends JsonActionBase {

    private final String originDropletId;
    private final String resultDropletId1;
    private final String resultDropletId2;
    private final double ratio;
    private final int posX1;
    private final int posY1;
    private final int posX2;
    private final int posY2;
    private final String nextActionId;

    @JsonCreator
    public JsonSplitAction(
            @JsonProperty("id") String id,
            @JsonProperty("originDroplet") String originDroplet,
            @JsonProperty("resultDroplet1") String resultDroplet1,
            @JsonProperty("resultDroplet2") String resultDroplet2,
            @JsonProperty("ratio") double ratio,
            @JsonProperty("posX1") int posX1,
            @JsonProperty("posY1") int posY1,
            @JsonProperty("posX2") int posX2,
            @JsonProperty("posY2") int posY2,
            @JsonProperty("next") String nextActionId
    ) {
        super(id);
        this.originDropletId = originDroplet;
        this.resultDropletId1 = resultDroplet1;
        this.resultDropletId2 = resultDroplet2;
        this.ratio = ratio;
        this.posX1 = posX1;
        this.posY1 = posY1;
        this.posX2 = posX2;
        this.posY2 = posY2;
        this.nextActionId = nextActionId;
    }

}
