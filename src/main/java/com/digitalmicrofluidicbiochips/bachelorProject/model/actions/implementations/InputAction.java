package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations;

import com.digitalmicrofluidicbiochips.bachelorProject.errors.DmfExceptionMessage;
import com.digitalmicrofluidicbiochips.bachelorProject.errors.DmfInvalidInputException;
import com.digitalmicrofluidicbiochips.bachelorProject.executor.path_finding.DropletMove;
import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.ActionTickResult;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.IDmfCommand;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.SetElectrodeCommand;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.*;
import com.digitalmicrofluidicbiochips.bachelorProject.utils.DmfPlatformUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
public class InputAction extends ActionBase {

    private final int posX;
    private final int posY;
    private final double volume;

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
    }

    @Override
    public Set<Droplet> dropletsProducedByExecution() {
        return Set.of(droplet);
    }

    @Override
    public void beforeExecution(ProgramConfiguration programConfiguration) {
        droplet.setStatus(DropletStatus.UNAVAILABLE);
        setStatus(ActionStatus.IN_PROGRESS);
    }

    @Override
    public ActionTickResult executeTick(ProgramConfiguration programConfiguration) {
        ActionTickResult actionTickResult = new ActionTickResult();

        // Droplet can be placed on the platform
        droplet.setPositionX(posX);
        droplet.setPositionY(posY);
        droplet.setDropletMove(DropletMove.NONE);

        ElectrodeGrid electrodeGrid = programConfiguration.getElectrodeGrid();
        int electrodeWidth = electrodeGrid.getElectrode(0, 0).getSizeX();
        int diameterInElectrodes = DmfPlatformUtils.electrodeSpanRequiredToMoveDroplet(droplet, electrodeWidth);
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
    public void afterExecution(ProgramConfiguration programConfiguration) {
        droplet.setStatus(DropletStatus.AVAILABLE);
    }

    @Override
    public boolean verifyProperties(ProgramConfiguration programConfiguration) {
        if (droplet == null) {
            throw new DmfInvalidInputException(DmfExceptionMessage.DROPLET_NOT_DEFINED_ON_ACTION.getMessage());
        }
        if (!programConfiguration.getElectrodeGrid().isWithinBounds(posX, posY)) {
            int maxX = programConfiguration.getElectrodeGrid().getXSize();
            int maxY = programConfiguration.getElectrodeGrid().getYSize();
            throw new DmfInvalidInputException(DmfExceptionMessage.POSITION_OUT_OF_BOUND.getMessage(posX, posY, maxX, maxY));
        }
        if (volume <= 0) {
            throw new DmfInvalidInputException(DmfExceptionMessage.INPUT_ACTION_INVALID_VOLUME.getMessage());
        }

        return true;
    }


}
