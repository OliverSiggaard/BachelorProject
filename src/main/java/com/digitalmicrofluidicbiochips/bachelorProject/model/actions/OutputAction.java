package com.digitalmicrofluidicbiochips.bachelorProject.model.actions;

import lombok.Getter;
import lombok.Setter;

@Getter
public class OutputAction extends ActionBase{

    public String dropletId;
    public int posX;
    public int posY;

    @Setter
    public ActionBase nextAction = null;

    public OutputAction(
            String id,
            String dropletId,
            int posX,
            int posY
    ) {
        super(id);
        this.dropletId = dropletId;
        this.posX = posX;
        this.posY = posY;
    }
}
