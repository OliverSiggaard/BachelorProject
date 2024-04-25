package com.digitalmicrofluidicbiochips.bachelorProject.reader.json.model.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.reader.model.DtoActionBase;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;

/*
 * This class is the base DTO class for all the different action types, and is to be used as a superclass for all actions.
 * The class contains the id and actionType fields, which are common for all the different action types.
 *
 * The class is annotated with Jackson annotations, which are used to map the JSON file to the correct ActionBase object.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "action") //This is the property used to determine the type of action.

//All the different action types are listed here.
// This is required for Jackson to be able to map the JSON file to the correct ActionBase object.
@JsonSubTypes({
        @JsonSubTypes.Type(value = JsonInputAction.class, name = "input"),
        @JsonSubTypes.Type(value = JsonOutputAction.class, name = "output"),
        @JsonSubTypes.Type(value = JsonMoveAction.class, name = "move"),
        @JsonSubTypes.Type(value = JsonMergeAction.class, name = "merge"),
        @JsonSubTypes.Type(value = JsonSplitAction.class, name = "split"),
        @JsonSubTypes.Type(value = JsonMixAction.class, name = "mix"),
        @JsonSubTypes.Type(value = JsonStoreAction.class, name = "store"),
        @JsonSubTypes.Type(value = JsonRepeatAction.class, name = "repeat"),
})
@Getter
public class JsonActionBase extends DtoActionBase {
    public JsonActionBase(String id) {
        super(id);
    }
}
