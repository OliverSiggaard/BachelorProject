package com.digitalmicrofluidicbiochips.bachelorProject.model.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class StartAction extends ActionBase {

    @Setter
    private ActionBase nextAction = null;

    public StartAction(String id) {
        super(id);
    }

}
