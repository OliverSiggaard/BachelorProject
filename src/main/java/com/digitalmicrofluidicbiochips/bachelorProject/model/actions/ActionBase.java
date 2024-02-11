package com.digitalmicrofluidicbiochips.bachelorProject.model.actions;

import lombok.Getter;

@Getter
public class ActionBase {

    private final String id;

    public ActionBase(String id) {
        this.id = id;
    }


}
