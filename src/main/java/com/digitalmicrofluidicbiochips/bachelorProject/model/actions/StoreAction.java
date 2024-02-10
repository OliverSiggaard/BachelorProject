package com.digitalmicrofluidicbiochips.bachelorProject.model.actions;

import lombok.Getter;
import lombok.Setter;

@Getter
public class StoreAction extends ActionBase {

    private final String dropletId;
    private final int posX;
    private final int posY;
    private final int time;

    @Setter
    private ActionBase nextAction = null;

    public StoreAction(
            String id,
            String dropletId,
            int posX,
            int posY,
            int time
    ) {
        super(id);
        this.dropletId = dropletId;
        this.posX = posX;
        this.posY = posY;
        this.time = time;
    }
}
