package com.digitalmicrofluidicbiochips.bachelorProject.model.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.DropletStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class InputAction extends ActionBase {


    private final int posX;
    private final int posY;
    private final int volume;

    @Setter
    private ActionBase nextAction = null;

    @Setter
    private Droplet droplet = null;

    public InputAction(
            String id,
            int posX,
            int posY,
            int volume
    ) {
        super(id);
        this.posX = posX;
        this.posY = posY;
        this.volume = volume;
    }

}
