package com.digitalmicrofluidicbiochips.bachelorProject.reader.json.model.dmf_platform;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * DTO class for the platform information.
 *
 * Contains meta-information about the platform (for example, the platform name, platform type etc.)
 *
 * See the provided pdf DMFasJSON.pdf for more information.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class JsonDmfPlatformInformation {
    @JsonProperty("platform_name")
    private String platform_name;

    @JsonProperty("platform_type")
    private String platform_type;

    @JsonProperty("platform_ID")
    private int platform_ID;

    @JsonProperty("sizeX")
    private int sizeX;

    @JsonProperty("sizeY")
    private int sizeY;

}
