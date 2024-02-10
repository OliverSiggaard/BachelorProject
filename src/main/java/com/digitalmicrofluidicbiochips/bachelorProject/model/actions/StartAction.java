package com.digitalmicrofluidicbiochips.bachelorProject.model.actions;

import lombok.Getter;
import lombok.Setter;

@Getter
public class StartAction extends ActionBase {

    @Setter
    private ActionBase nextAction = null;

    public StartAction(String id) {
        super(id);
    }

}
