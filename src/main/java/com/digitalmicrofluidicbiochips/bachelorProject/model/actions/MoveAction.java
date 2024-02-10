package com.digitalmicrofluidicbiochips.bachelorProject.model.actions;

import lombok.Getter;
import lombok.Setter;

@Getter
public class MoveAction extends ActionBase {

    private final String dropletId;
    private final int destX;
    private final int destY;
    @Setter
    private ActionBase nextAction = null;

    public MoveAction(
            String id,
            String dropletId,
            int destX,
            int destY
    ) {
        super(id);
        this.dropletId = dropletId;
        this.destX = destX;
        this.destY = destY;
    }
}
