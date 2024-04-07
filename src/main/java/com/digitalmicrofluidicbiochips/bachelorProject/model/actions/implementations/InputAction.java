package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations;

import com.digitalmicrofluidicbiochips.bachelorProject.executor.path_finding.DropletMove;
import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.ActionTickResult;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.IDmfCommand;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.SetElectrodeCommand;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class InputAction extends ActionBase {


    private final int posX;
    private final int posY;
    private final double volume;

    @Setter
    private ActionBase nextAction = null;

    @Setter
    private Droplet droplet = null;

    public InputAction(
            String id,
            int posX,
            int posY,
            double volume
    ) {
        super(id);
        this.posX = posX;
        this.posY = posY;
        this.volume = volume;
    }

    @Override
    public Set<Droplet> dropletsRequiredForExecution() {
        return new HashSet<>();
        //return new HashSet<>(Set.of(droplet));
    }

    @Override
    public void beforeExecution() {
        droplet.setStatus(DropletStatus.UNAVAILABLE);
        setStatus(ActionStatus.IN_PROGRESS);
    }

    @Override
    public ActionTickResult executeTick(ProgramConfiguration programConfiguration) {
        droplet.setPositionX(posX);
        droplet.setPositionY(posY);
        droplet.setDropletMove(DropletMove.NONE);

        ElectrodeGrid electrodeGrid = programConfiguration.getElectrodeGrid();
        ActionTickResult actionTickResult = new ActionTickResult();
        int diameterInElectrodes = (int) Math.ceil((double)droplet.getDiameter() / 20);
        for(int dx = 0; dx < diameterInElectrodes ; dx++) {
            for (int dy = 0; dy < diameterInElectrodes; dy++) {
                int x = droplet.getPositionX() + dx;
                int y = droplet.getPositionY() + dy;
                IDmfCommand command = new SetElectrodeCommand(electrodeGrid.getElectrode(x, y));

                actionTickResult.addCommand(command);
            }
        }

        setStatus(ActionStatus.COMPLETED);
        return actionTickResult;
    }

    @Override
    public void afterExecution() {
        droplet.setStatus(DropletStatus.AVAILABLE);
    }
}
