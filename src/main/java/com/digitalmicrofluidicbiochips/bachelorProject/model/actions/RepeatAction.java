package com.digitalmicrofluidicbiochips.bachelorProject.model.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

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
