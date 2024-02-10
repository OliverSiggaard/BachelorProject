package com.digitalmicrofluidicbiochips.bachelorProject.model.actions;

import lombok.Getter;
import lombok.Setter;

@Getter
public class RepeatAction extends ActionBase {

    private final int times;

    @Setter
    private ActionBase nextRepeatAction = null;
    @Setter
    private ActionBase nextExitAction = null;

    public RepeatAction(
            String id,
            int times
    ) {
        super(id);
        this.times = times;
    }
}
