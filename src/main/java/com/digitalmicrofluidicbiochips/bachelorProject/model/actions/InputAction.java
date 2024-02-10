package com.digitalmicrofluidicbiochips.bachelorProject.model.actions;

import lombok.Getter;
import lombok.Setter;

@Getter
public class InputAction extends ActionBase {

    private final String dropletId;
    private final int posX;
    private final int posY;
    private final int volume;

    @Setter
    private ActionBase nextAction = null;
    public InputAction(
            String id,
            String dropletId,
            int posX,
            int posY,
            int volume
    ) {
        super(id);
        this.dropletId = dropletId;
        this.posX = posX;
        this.posY = posY;
        this.volume = volume;
    }
}
