package com.digitalmicrofluidicbiochips.bachelorProject.model.actions;

import lombok.Getter;
import lombok.Setter;

@Getter
public class MergeAction extends ActionBase {

    private final String resultDropletId;
    private final String dropletId1;
    private final String dropletId2;
    private final int destX;
    private final int destY;

    @Setter
    private ActionBase nextAction = null;

    public MergeAction(
            String id,
            String resultDropletId,
            String dropletId1,
            String dropletId2,
            int destX,
            int destY
    ) {
        super(id);
        this.resultDropletId = resultDropletId;
        this.dropletId1 = dropletId1;
        this.dropletId2 = dropletId2;
        this.destX = destX;
        this.destY = destY;
    }
}
