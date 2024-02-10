package com.digitalmicrofluidicbiochips.bachelorProject.model.actions;

import lombok.Getter;
import lombok.Setter;

@Getter
public class IfAction extends ActionBase {

    private final String condition;

    @Setter
    private ActionBase trueNextAction = null;
    @Setter
    private ActionBase falseNextAction = null;

    public IfAction(
            String id,
            String condition
    ) {
        super(id);
        this.condition = condition;
    }



}
