package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations;

import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.ActionTickResult;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.ClearElectrodeCommand;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.SetElectrodeCommand;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.*;
import com.digitalmicrofluidicbiochips.bachelorProject.utils.DmfPlatformUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
public class SplitAction extends ActionBase {

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

    private final Queue<ActionTickResult> tickQueue;

    public SplitAction(
            String id,
            int posX1,
            int posY1,
            int posX2,
            int posY2
    ) {
        super(id);
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

        // TODO: There is a area in each corner, where the droplet can't be split. This should be checked,
        //  and an exception thrown if the droplet is in such area.

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


        // Boundaries of origin droplet
        ElectrodeGrid electrodeGrid = programConfiguration.getElectrodeGrid();
        int electrodeSize = electrodeGrid.getElectrodeSizeOfElectrodeInGrid();
        int originDropletSize = DmfPlatformUtils.electrodeSpanRequiredToMoveDroplet(originDroplet, electrodeSize);
        GridArea originDropletArea = new GridArea(
                originDroplet.getPositionX(),
                originDroplet.getPositionY(),
                originDroplet.getPositionX() + originDropletSize - 1,
                originDroplet.getPositionY() + originDropletSize - 1);

        if(originDropletCanSplitHorizontally(programConfiguration)) {
            splitHorizontally(electrodeGrid, originDropletArea);
        } else if(originDropletCanSplitVertically(programConfiguration)) {
            splitVertically(electrodeGrid, originDropletArea);
        }

        return tickQueue.isEmpty() ? new ActionTickResult() : tickQueue.poll();
    }

    @Override
    public void afterExecution() {
        originDroplet.setStatus(DropletStatus.CONSUMED);
        resultDroplet1.setStatus(DropletStatus.AVAILABLE);
        resultDroplet2.setStatus(DropletStatus.AVAILABLE);
    }

    private void splitHorizontally(ElectrodeGrid electrodeGrid, GridArea originDropletArea) {
        ActionTickResult actionTickResult = new ActionTickResult();
        int x1 = originDropletArea.getX1();
        int y1 = originDropletArea.getY1();
        int x2 = originDropletArea.getX2();
        int y2 = originDropletArea.getY2();
        int originDropletSize = x2 - x1 + 1;

        if(originDropletSize >= 2) {
            // Clear bottom row of electrodes in origin droplet
            actionTickResult.addCommands(electrodeGrid.getClearElectrodeCommands(new GridArea(x1, y2, x2, y2)));
            y2 -= 1;

            //If origin droplet has an even number of electrodes vertically, add a column to the right of the origin droplet.
            if(originDropletSize % 2 == 0) {
                actionTickResult.addCommands(electrodeGrid.getSetElectrodeCommands(new GridArea(x2 + 1, y1, x2 + 1, y2)));
                x2 += 1;
            }
            tickQueue.add(actionTickResult);
            actionTickResult = new ActionTickResult();
        }

        int middleX = (x1 + x2)/2;
        // Remove middle, expand horizontally
        actionTickResult.addCommands(electrodeGrid.getClearElectrodeCommands(new GridArea(middleX, y1, middleX, y2)));
        actionTickResult.addCommands(electrodeGrid.getSetElectrodeCommands(new GridArea(x1 - 1, y1, x1 - 1, y2)));
        actionTickResult.addCommands(electrodeGrid.getSetElectrodeCommands(new GridArea(x2 + 1, y1, x2 + 1, y2)));

        tickQueue.add(actionTickResult);
        actionTickResult = new ActionTickResult();

        // Remove additional columns from middle, add to the sides
        actionTickResult.addCommands(electrodeGrid.getClearElectrodeCommands(new GridArea(middleX - 1, y1, middleX - 1, y2)));
        actionTickResult.addCommands(electrodeGrid.getClearElectrodeCommands(new GridArea(middleX + 1, y1, middleX + 1, y2)));
        actionTickResult.addCommands(electrodeGrid.getSetElectrodeCommands(new GridArea(x1 - 2, y1, x1 - 2, y2)));
        actionTickResult.addCommands(electrodeGrid.getSetElectrodeCommands(new GridArea(x2 + 2, y1, x2 + 2, y2)));
        tickQueue.add(actionTickResult);


        // The droplets are now split. Last step is to constrain the electrodes below the droplets to the correct size.
        int resDropletHeight = y2 - y1 + 1;
        int resDropletWidth = middleX - x1 + 1;
        resultDroplet1.setPositionX(originDroplet.getPositionX() - 2);
        resultDroplet1.setPositionY(originDroplet.getPositionY());
        resultDroplet2.setPositionX(resultDroplet1.getPositionX() + resDropletWidth + 3);
        resultDroplet2.setPositionY(originDroplet.getPositionY());

        constrainElectrodesBelowDropletToDropletSize(resultDroplet1, electrodeGrid, resDropletWidth, resDropletHeight);
        constrainElectrodesBelowDropletToDropletSize(resultDroplet2, electrodeGrid, resDropletWidth, resDropletHeight);
    }

    private void splitVertically(ElectrodeGrid electrodeGrid, GridArea originDropletArea) {
        ActionTickResult actionTickResult = new ActionTickResult();
        int x1 = originDropletArea.getX1();
        int y1 = originDropletArea.getY1();
        int x2 = originDropletArea.getX2();
        int y2 = originDropletArea.getY2();
        int originDropletSize = x2 - x1 + 1;

        if(originDropletSize >= 2) {
            // Clear right-most column of electrodes in origin droplet
            actionTickResult.addCommands(electrodeGrid.getClearElectrodeCommands(new GridArea(x2, y1, x2, y2)));
            x2 -= 1;

            //If origin droplet has an even number of electrodes vertically, add a row to the bottom of the origin droplet.
            if(originDropletSize % 2 == 0) {
                actionTickResult.addCommands(electrodeGrid.getSetElectrodeCommands(new GridArea(x1, y2 + 1, x2, y2 + 1)));
                y2 += 1;
            }
            tickQueue.add(actionTickResult);
        }

        actionTickResult = new ActionTickResult();
        int middleY = (y1 + y2)/2;
        // Remove middle, expand vertically
        actionTickResult.addCommands(electrodeGrid.getClearElectrodeCommands(new GridArea(x1, middleY, x2, middleY)));
        actionTickResult.addCommands(electrodeGrid.getSetElectrodeCommands(new GridArea(x1, y1 - 1, x2, y1 - 1)));
        actionTickResult.addCommands(electrodeGrid.getSetElectrodeCommands(new GridArea(x1, y2 + 1, x2, y2 + 1)));
        tickQueue.add(actionTickResult);

        // Remove additional rows from middle, add to the sides
        actionTickResult = new ActionTickResult();
        actionTickResult.addCommands(electrodeGrid.getClearElectrodeCommands(new GridArea(x1, middleY - 1, x2, middleY - 1)));
        actionTickResult.addCommands(electrodeGrid.getClearElectrodeCommands(new GridArea(x1, middleY + 1, x2, middleY + 1)));
        actionTickResult.addCommands(electrodeGrid.getSetElectrodeCommands(new GridArea(x1, y1 - 2, x2, y1 - 2)));
        actionTickResult.addCommands(electrodeGrid.getSetElectrodeCommands(new GridArea(x1, y2 + 2, x2, y2 + 2)));
        tickQueue.add(actionTickResult);

        // The droplets are now split. Last step is to constrain the electrodes below the droplets to the correct size.
        int splitYHeight = middleY - y1 + 1;
        int splitXWidth = x2 - x1 + 1;
        resultDroplet1.setPositionX(originDroplet.getPositionX());
        resultDroplet1.setPositionY(originDroplet.getPositionY() - 2);
        resultDroplet2.setPositionX(originDroplet.getPositionX());
        resultDroplet2.setPositionY(resultDroplet1.getPositionY() + splitYHeight + 3);

        constrainElectrodesBelowDropletToDropletSize(resultDroplet1, electrodeGrid, splitXWidth, splitYHeight);
        constrainElectrodesBelowDropletToDropletSize(resultDroplet2, electrodeGrid, splitXWidth, splitYHeight);

    }

    /**
     * After having split, the electrodes below the droplet, are not necessarily the correct size.
     * E.g. if the result droplets are each 2x2 cells, but the split was done in such way, that each droplet was split
     * by a 2x3 snake, this will be corrected, such that only 2x2 cells below the droplets are enabled.
     *
     * @param droplet The droplet should contain the correct volume. The droplet position should be
     *                the position in the top left corner of the split droplets.
     * @param xWidth width of electrodes initially below droplet
     * @param yWidth width of electrodes initially below droplet
     */
    private void constrainElectrodesBelowDropletToDropletSize(Droplet droplet, ElectrodeGrid eGrid, int xWidth, int yWidth) {
        int x = droplet.getPositionX();
        int y = droplet.getPositionY();

        int electrodeSize = eGrid.getElectrodeSizeOfElectrodeInGrid();
        int dropletSize = DmfPlatformUtils.electrodeSpanRequiredToMoveDroplet(droplet, electrodeSize);

        while(xWidth < dropletSize) {
            GridArea gridArea = new GridArea(x + xWidth, y, x + xWidth, y + yWidth - 1);
            tickQueue.add(new ActionTickResult(eGrid.getSetElectrodeCommands(gridArea)));
            xWidth += 1;
        }
        while(xWidth > dropletSize) {
            GridArea gridArea = new GridArea(x + xWidth - 1, y, x + xWidth - 1, y + yWidth - 1);
            tickQueue.add(new ActionTickResult(eGrid.getClearElectrodeCommands(gridArea)));
            xWidth -= 1;
        }
        while(yWidth < dropletSize) {
            GridArea gridArea = new GridArea(x, y + yWidth, x + xWidth - 1, y + yWidth);
            tickQueue.add(new ActionTickResult(eGrid.getSetElectrodeCommands(gridArea)));
            yWidth += 1;
        }
        while(yWidth > dropletSize) {
            GridArea gridArea = new GridArea(x, y + yWidth - 1, x + xWidth - 1, y + yWidth - 1);
            tickQueue.add(new ActionTickResult(eGrid.getClearElectrodeCommands(gridArea)));
            yWidth -= 1;
        }
    }

    private boolean originDropletCanSplitHorizontally(ProgramConfiguration programConfiguration) {
        ElectrodeGrid availableGrid = getAvailableGridForOriginDroplet(programConfiguration);
        int ElectrodeSize = programConfiguration.getElectrodeGrid().getElectrodeSizeOfElectrodeInGrid();
        int originDropletSize = DmfPlatformUtils.electrodeSpanRequiredToMoveDroplet(originDroplet, ElectrodeSize);
        int y = originDroplet.getPositionY();
        int x1 = originDroplet.getPositionX() - 2;
        int x2 = (originDroplet.getPositionX() * 2 + originDropletSize)/2 + 2;

        // Boundary check to the left.
        if(x1 < 0 || x2 + originDropletSize >= availableGrid.getXSize()) {
            return false;
        }

        // Check that origin droplet can move 2 to either side.
        GridArea gridArea = new GridArea(x1, y, x2, y);
        return availableGrid.isAllElectrodesAvailableWithinArea(gridArea);
    }

    private boolean originDropletCanSplitVertically(ProgramConfiguration programConfiguration) {
        ElectrodeGrid availableGrid = getAvailableGridForOriginDroplet(programConfiguration);
        int ElectrodeSize = programConfiguration.getElectrodeGrid().getElectrodeSizeOfElectrodeInGrid();
        int originDropletSize = DmfPlatformUtils.electrodeSpanRequiredToMoveDroplet(originDroplet, ElectrodeSize);
        int x = originDroplet.getPositionX();
        int y1 = originDroplet.getPositionY() - 2;
        int y2 = (originDroplet.getPositionY() * 2 + originDropletSize)/2 + 2;

        // Boundary check
        if(y1 < 0 || y2 + originDropletSize >= availableGrid.getYSize()) {
            return false;
        }

        // Check that origin droplet can move 2 up or down.
        GridArea gridArea = new GridArea(x, y1, x, y2);
        return availableGrid.isAllElectrodesAvailableWithinArea(gridArea);
    }

    private ElectrodeGrid getAvailableGridForOriginDroplet(ProgramConfiguration programConfiguration) {
        List<Droplet> obstacleDroplets = programConfiguration.getDropletsOnDmfPlatform().stream()
                .filter(d -> !Set.of(originDroplet, resultDroplet1, resultDroplet2).contains(d))
                .toList();
        ElectrodeGrid electrodeGrid = programConfiguration.getElectrodeGrid();
        return ElectrodeGridFactory.getAvailableElectrodeGrid(electrodeGrid, originDroplet, obstacleDroplets);
    }
}
