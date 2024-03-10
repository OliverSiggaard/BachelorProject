package com.digitalmicrofluidicbiochips.bachelorProject.model.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.DropletStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class MixAction extends ActionBase {

    @Setter
    private ActionBase nextAction = null;
    @Setter
    private Droplet droplet = null;

    private final int posX;
    private final int posY;
    private final int sizeX;
    private final int sizeY;

    public MixAction(
            String id,
            int posX,
            int posY,
            int sizeX,
            int sizeY
    ) {
        super(id);
        this.posX = posX;
        this.posY = posY;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

}
