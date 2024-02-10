package com.digitalmicrofluidicbiochips.bachelorProject.model.actions;

import lombok.Getter;
import lombok.Setter;

@Getter
public class SplitAction extends ActionBase {

    private final String originDroplet;
    private final String resultDroplet1;
    private final String resultDroplet2;
    private final double ratio;
    private final int destX1;
    private final int destY1;
    private final int destX2;
    private final int destY2;

    @Setter
    private ActionBase nextAction = null;

    public SplitAction(
            String id,
            String originDroplet,
            String resultDroplet1,
            String resultDroplet2,
            double ratio,
            int destX1,
            int destY1,
            int destX2,
            int destY2
    ) {
        super(id);
        this.originDroplet = originDroplet;
        this.resultDroplet1 = resultDroplet1;
        this.resultDroplet2 = resultDroplet2;
        this.ratio = ratio;
        this.destX1 = destX1;
        this.destY1 = destY1;
        this.destX2 = destX2;
        this.destY2 = destY2;
    }
}
