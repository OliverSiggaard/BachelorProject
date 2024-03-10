package com.digitalmicrofluidicbiochips.bachelorProject.model.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.DropletStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class OutputAction extends ActionBase{

    public int posX;
    public int posY;

    @Setter
    public ActionBase nextAction = null;
    @Setter
    private Droplet droplet = null;

    public OutputAction(
            String id,
            int posX,
            int posY
    ) {
        super(id);
        this.posX = posX;
        this.posY = posY;
    }

}
