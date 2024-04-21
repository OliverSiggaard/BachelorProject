package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations;

import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.ActionTickResult;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.ClearElectrodeCommand;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.SetElectrodeCommand;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.DropletStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.ElectrodeGrid;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.ElectrodeGridFactory;
import com.digitalmicrofluidicbiochips.bachelorProject.utils.DmfPlatformUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
public class SplitAction extends ActionBase {

    private final double ratio;
    private final int posX1;
    private final int posY1;
    private final int posX2;
    private final int posY2;

    @Setter
    private ActionBase nextAction = null;
    @Setter
    private Droplet originDroplet = null;
    @Setter
    private Droplet resultDroplet1 = null;
    @Setter
    private Droplet resultDroplet2 = null;

    private MoveAction moveAction1;
    private MoveAction moveAction2;
    private final Queue<ActionTickResult> tickQueue;

    public SplitAction(
            String id,
            double ratio,
            int posX1,
            int posY1,
            int posX2,
            int posY2
    ) {
        super(id);
        this.ratio = ratio;
        this.posX1 = posX1;
        this.posY1 = posY1;
        this.posX2 = posX2;
        this.posY2 = posY2;
        this.tickQueue = new LinkedList<>();
    }

    @Override
    public Set<Droplet> dropletsRequiredForExecution() {
        return Set.of(originDroplet);
    }

    @Override
    public void beforeExecution() {

        if(resultDroplet1.getStatus() != DropletStatus.NOT_CREATED ||
                resultDroplet2.getStatus() != DropletStatus.NOT_CREATED) {
            throw new IllegalStateException("Result droplets must be in NOT_CREATED state upon execution.");
        }

        originDroplet.setStatus(DropletStatus.UNAVAILABLE);
        resultDroplet1.setVolume(originDroplet.getVolume()/2);
        resultDroplet2.setVolume(originDroplet.getVolume()/2);

        setStatus(ActionStatus.IN_PROGRESS);
    }

    @Override
    public ActionTickResult executeTick(ProgramConfiguration programConfiguration) {

        if(!tickQueue.isEmpty()) {
            ActionTickResult tickResult = tickQueue.poll();
            if(tickQueue.isEmpty()) {
                setStatus(ActionStatus.COMPLETED);
            }
            return tickResult;
        }

        ActionTickResult actionTickResult = new ActionTickResult();
        // Splitting can happen in 2 different ways, namely horizontally and vertically.
        // When splitting, 2 droplets split out from the origin droplets in 2 ticks, resulting in the droplets being 3 or 4 cells apart.

        ElectrodeGrid electrodeGrid = programConfiguration.getElectrodeGrid();

        int electrodeSize = electrodeGrid.getElectrodeSizeOfElectrodeInGrid();
        int originDropletElectrodeSpan = DmfPlatformUtils.electrodeSpanRequiredToMoveDroplet(originDroplet, electrodeSize);
        int newDropletsElectrodeSpan = DmfPlatformUtils.electrodeSpanRequiredToMoveDroplet(resultDroplet1, electrodeSize);

        // Horizontal split

        // clear bottom to-be-unused electrodes
        for (int y = originDroplet.getPositionY() + newDropletsElectrodeSpan; y < originDroplet.getPositionY() + originDropletElectrodeSpan; y++) {
            for (int x = originDroplet.getPositionX(); x < originDroplet.getPositionX() + originDropletElectrodeSpan; x++) {
                actionTickResult.addCommand(new ClearElectrodeCommand(electrodeGrid.getElectrode(x, y)));
            }
        }

        // Snaking out horizontally 2 times.
        for (int y = originDroplet.getPositionY(); y < originDroplet.getPositionY() + newDropletsElectrodeSpan; y++) {
            actionTickResult.addCommand(new SetElectrodeCommand(electrodeGrid.getElectrode(originDroplet.getPositionX() - 1, y)));
            actionTickResult.addCommand(new SetElectrodeCommand(electrodeGrid.getElectrode(originDroplet.getPositionX() + newDropletsElectrodeSpan, y)));
        }

        // second time.
        ActionTickResult actionTickResult2 = new ActionTickResult();
        ActionTickResult actionTickResult3 = new ActionTickResult();
        for (int y = originDroplet.getPositionY(); y < originDroplet.getPositionY() + newDropletsElectrodeSpan; y++) {
            actionTickResult2.addCommand(new SetElectrodeCommand(electrodeGrid.getElectrode(originDroplet.getPositionX() - 2, y)));
            actionTickResult2.addCommand(new SetElectrodeCommand(electrodeGrid.getElectrode(originDroplet.getPositionX() + newDropletsElectrodeSpan + 1, y)));

            if(originDropletElectrodeSpan % 2 == 0) // Remove the middle 2 electrodes.
            {
                actionTickResult.addCommand(new ClearElectrodeCommand(electrodeGrid.getElectrode(originDroplet.getPositionX() + originDropletElectrodeSpan/2, y)));
                actionTickResult.addCommand(new ClearElectrodeCommand(electrodeGrid.getElectrode(originDroplet.getPositionX() + originDropletElectrodeSpan/2 - 1, y)));
            } else { // Remove the middle electrode.
                actionTickResult3.addCommand(new ClearElectrodeCommand(electrodeGrid.getElectrode(originDroplet.getPositionX() + originDropletElectrodeSpan/2, y)));

                //Remove the middle electrodes
                actionTickResult2.addCommand(new ClearElectrodeCommand(electrodeGrid.getElectrode(originDroplet.getPositionX() + originDropletElectrodeSpan/2 - 1, y)));
                actionTickResult2.addCommand(new ClearElectrodeCommand(electrodeGrid.getElectrode(originDroplet.getPositionX() + originDropletElectrodeSpan/2 - 1, y)));
            }
        }
        tickQueue.add(actionTickResult2);
        tickQueue.add(actionTickResult3);

        return actionTickResult;
    }

    @Override
    public void afterExecution() {

    }
}
