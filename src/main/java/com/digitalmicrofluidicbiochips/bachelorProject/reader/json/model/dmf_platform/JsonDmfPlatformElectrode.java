package com.digitalmicrofluidicbiochips.bachelorProject.reader.json.model.dmf_platform;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class JsonDmfPlatformElectrode {
    @JsonProperty("name")
    private String name;

    @JsonProperty("ID")
    private int ID;

    @JsonProperty("electrodeID")
    private int electrodeID;

    @JsonProperty("driverID")
    private int driverID;

    @JsonProperty("shape")
    private int shape;

    @JsonProperty("positionX")
    private int positionX;

    @JsonProperty("positionY")
    private int positionY;

    @JsonProperty("sizeX")
    private int sizeX;

    @JsonProperty("sizeY")
    private int sizeY;

    @JsonProperty("status")
    private int status;

    @JsonProperty("corners")
    private int[] corners;


}
