package com.digitalmicrofluidicbiochips.bachelorProject.model.actions;

import lombok.Getter;
import lombok.Setter;

@Getter
public class MoveAction extends ActionBase {

    private final String dropletId;
    private final int posX;
    private final int posY;
    @Setter
    private ActionBase nextAction = null;

    public MoveAction(
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
