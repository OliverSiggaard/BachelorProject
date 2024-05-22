package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations;

import com.digitalmicrofluidicbiochips.bachelorProject.errors.DmfExceptionMessage;
import com.digitalmicrofluidicbiochips.bachelorProject.errors.DmfExecutorException;
import com.digitalmicrofluidicbiochips.bachelorProject.errors.DmfInputReaderException;
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
        setStatus(ActionStatus.IN_PROGRESS);
    }

    @Override
    public ActionTickResult executeTick(ProgramConfiguration programConfiguration) {
        ActionTickResult actionTickResult = new ActionTickResult();

        droplet.setPositionX(posX);
        droplet.setPositionY(posY);
        droplet.setDropletMove(DropletMove.NONE);
        droplet.setVolume(volume);

        if(droplet.getDiameter() <= programConfiguration.getElectrodeGrid().getElectrodeSizeOfElectrodeInGrid()) {
            throw new DmfExecutorException(DmfExceptionMessage.INPUT_ACTION_DROPLET_DIAMETER_SMALLER_THAN_ELECTRODE_SIZE.getMessage(droplet));
        }

        Collection<Droplet> obstacleDroplets = programConfiguration.getDropletsOnDmfPlatform().stream().filter(o -> o != droplet).toList();
        boolean dropletCanBePlacedOnPlatform = obstacleDroplets.stream().allMatch(obstacleDroplet -> {
            GridArea gridArea = droplet.getDropletSafeArea(programConfiguration.getElectrodeGrid(), obstacleDroplet);
            return !gridArea.contains(posX, posY);
        });

        if(!dropletCanBePlacedOnPlatform) {
            return actionTickResult;
        }

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

        droplet.setStatus(DropletStatus.UNAVAILABLE);
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
            throw new DmfInputReaderException(DmfExceptionMessage.DROPLET_NOT_DEFINED_ON_ACTION.getMessage());
        }
        if (!programConfiguration.getElectrodeGrid().isWithinBounds(posX, posY)) {
            int maxX = programConfiguration.getElectrodeGrid().getXSize();
            int maxY = programConfiguration.getElectrodeGrid().getYSize();
            throw new DmfInputReaderException(DmfExceptionMessage.POSITION_OUT_OF_BOUND.getMessage(posX, posY, maxX, maxY));
        }
        if (volume <= 0) {
            throw new DmfInputReaderException(DmfExceptionMessage.INPUT_ACTION_INVALID_VOLUME.getMessage());
        }

        return true;
    }


}
