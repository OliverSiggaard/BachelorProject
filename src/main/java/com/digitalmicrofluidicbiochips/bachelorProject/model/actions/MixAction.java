package com.digitalmicrofluidicbiochips.bachelorProject.model.actions;

import lombok.Getter;
import lombok.Setter;

@Getter
public class MixAction extends ActionBase {

    private final String dropletId;

    @Setter
    private ActionBase nextAction = null;

    public MixAction(
            String id,
            String dropletId
    ) {
        super(id);
        this.dropletId = dropletId;
    }

}
