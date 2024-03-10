package com.digitalmicrofluidicbiochips.bachelorProject.reader.json.model;

import com.digitalmicrofluidicbiochips.bachelorProject.reader.json.model.actions.JsonActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.reader.json.model.dmf_platform.JsonDmfPlatformState;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

/**
 * This class represents the initial configuration of the program, as well as the actions that the program will execute.
 * <br>
 * A Program Configuration JSON file can be generated using the complimentary GUI application, which can be found:
 * here: https://github.com/OliverSiggaard/bachelor-project-ui
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class JsonProgramConfiguration {

    // The initial state of the DMF platform. This is the state that the platform will be in when the program starts.
    @JsonProperty("dmf_configuration")
    private JsonDmfPlatformState dmfPlatformState;

    // The list of actions that the program will execute.
    @JsonProperty("program_actions")
    private List<JsonActionBase> programActions;
}
