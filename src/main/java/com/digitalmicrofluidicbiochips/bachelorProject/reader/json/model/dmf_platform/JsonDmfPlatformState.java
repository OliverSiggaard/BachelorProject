package com.digitalmicrofluidicbiochips.bachelorProject.reader.json.model.dmf_platform;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

/**
 * This class represents the state of the DMF platform.
 *
 * The JSON structure of said state, is given by the provided DMFasJSON.pdf file (see resources/DMFasJSON.pdf).
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class JsonDmfPlatformState {

    // The initial state of the DMF platform. This is the state that the platform will be in when the program starts.
    @JsonProperty("information")
    private JsonDmfPlatformInformation information;

    @JsonProperty("electrodes")
    private List<JsonDmfPlatformElectrode> electrodes;
}
