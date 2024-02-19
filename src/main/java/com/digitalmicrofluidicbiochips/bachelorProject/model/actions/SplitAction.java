package com.digitalmicrofluidicbiochips.bachelorProject.model.actions;

import lombok.Getter;
import lombok.Setter;

@Getter
public class SplitAction extends ActionBase {

    private final String originDroplet;
    private final String resultDroplet1;
    private final String resultDroplet2;
    private final double ratio;
    private final int posX1;
    private final int posY1;
    private final int posX2;
    private final int posY2;

    @Setter
    private ActionBase nextAction = null;

    public SplitAction(
            String id,
            String originDroplet,
            String resultDroplet1,
            String resultDroplet2,
            double ratio,
            int posX1,
            int posY1,
            int posX2,
            int posY2
    ) {
        super(id);
        this.originDroplet = originDroplet;
        this.resultDroplet1 = resultDroplet1;
        this.resultDroplet2 = resultDroplet2;
        this.ratio = ratio;
        this.posX1 = posX1;
        this.posY1 = posY1;
        this.posX2 = posX2;
        this.posY2 = posY2;
    }
}
