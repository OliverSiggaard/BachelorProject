package com.digitalmicrofluidicbiochips.bachelorProject.model.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import lombok.Getter;

import java.util.Set;

@Getter
public abstract class ActionBase {

    private final String id;

    public ActionBase(String id) {
        this.id = id;
    }


}
