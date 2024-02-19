package com.digitalmicrofluidicbiochips.bachelorProject.model.actions;

import lombok.Getter;
import lombok.Setter;

@Getter
public class MergeAction extends ActionBase {

    private final String resultDropletId;
    private final String dropletId1;
    private final String dropletId2;
    private final int posX;
    private final int posY;

    @Setter
    private ActionBase nextAction = null;

    public MergeAction(
            String id,
            String resultDropletId,
            String dropletId1,
            String dropletId2,
            int posX,
            int posY
    ) {
        super(id);
        this.resultDropletId = resultDropletId;
        this.dropletId1 = dropletId1;
        this.dropletId2 = dropletId2;
        this.posX = posX;
        this.posY = posY;
    }
}
