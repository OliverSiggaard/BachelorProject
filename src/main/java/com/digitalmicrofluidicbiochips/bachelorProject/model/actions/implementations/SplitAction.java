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
        // Splitting can happen in 2 different ways, namely horizontally and vertically.
        // When splitting, 2 droplets split out from the origin droplets in 2 ticks, resulting in the droplets being 3 or 4 cells apart.

        // Horizontal split

        if(originDropletCanSplitHorizontally(programConfiguration)) {
            splitHorizontally(programConfiguration);
        } else if(originDropletCanSplitVertically(programConfiguration)) {
            splitVertically(programConfiguration);
        }

        if(tickQueue.isEmpty()) {
            return new ActionTickResult();
        }

        return tickQueue.poll();
    }

    @Override
    public void afterExecution() {
        originDroplet.setStatus(DropletStatus.CONSUMED);
        resultDroplet1.setStatus(DropletStatus.AVAILABLE);
        resultDroplet2.setStatus(DropletStatus.AVAILABLE);
    }

    private void splitHorizontally(ProgramConfiguration programConfiguration) {
        ActionTickResult actionTickResult = new ActionTickResult();

        ElectrodeGrid electrodeGrid = programConfiguration.getElectrodeGrid();
        int electrodeSize = electrodeGrid.getElectrodeSizeOfElectrodeInGrid();
        int originDropletSize = DmfPlatformUtils.electrodeSpanRequiredToMoveDroplet(originDroplet, electrodeSize);

        // Boundaries of origin droplet
        int x1 = originDroplet.getPositionX();
        int x2 = originDroplet.getPositionX() + originDropletSize - 1;
        int y1 = originDroplet.getPositionY();
        int y2 = originDroplet.getPositionY() + originDropletSize - 1;


        if(originDropletSize >= 2) {
            // Clear bottom row of electrodes in origin droplet
            actionTickResult.addTickResult(getClearElectrodeCommands(electrodeGrid, x1, y2, x2, y2));
            y2 -= 1;

            //If origin droplet has an even number of electrodes vertically, add a column to the right of the origin droplet.
            if(originDropletSize % 2 == 0) {
                actionTickResult.addTickResult(getSetElectrodeCommands(electrodeGrid, x2 + 1, y1, x2 + 1, y2));
                x2 += 1;
            }
            tickQueue.add(actionTickResult);
            actionTickResult = new ActionTickResult();
        }

        int middleX = (x1 + x2)/2;
        // Remove middle, expand horizontally
        actionTickResult.addTickResult(getClearElectrodeCommands(electrodeGrid, middleX, y1, middleX, y2));
        actionTickResult.addTickResult(getSetElectrodeCommands(electrodeGrid, x1 - 1, y1, x1 - 1, y2));
        x1 -= 1;
        actionTickResult.addTickResult(getSetElectrodeCommands(electrodeGrid, x2 + 1, y1, x2 + 1, y2));
        x2 += 1;

        tickQueue.add(actionTickResult);
        actionTickResult = new ActionTickResult();

        // Remove additional columns from middle, add to the sides
        actionTickResult.addTickResult(getClearElectrodeCommands(electrodeGrid, middleX - 1, y1, middleX - 1, y2));
        actionTickResult.addTickResult(getClearElectrodeCommands(electrodeGrid, middleX + 1, y1, middleX + 1, y2));
        actionTickResult.addTickResult(getSetElectrodeCommands(electrodeGrid, x1 - 1, y1, x1 - 1, y2));
        x1 -= 1;
        actionTickResult.addTickResult(getSetElectrodeCommands(electrodeGrid, x2 + 1, y1, x2 + 1, y2));
        x2 += 1;
        tickQueue.add(actionTickResult);


        // The droplets are now split. Last step is to constrain the electrodes below the droplets to the correct size.
        int splitYHeight = y2 - y1 + 1;
        int splitXWidth = (x2 - x1 - 2)/2;
        resultDroplet1.setPositionX(originDroplet.getPositionX() - 2);
        resultDroplet1.setPositionY(originDroplet.getPositionY());
        resultDroplet2.setPositionX(resultDroplet1.getPositionX() + splitXWidth + 3);
        resultDroplet2.setPositionY(originDroplet.getPositionY());

        constrainElectrodesBelowDropletToDropletSize(resultDroplet1, electrodeGrid, resultDroplet1.getPositionX(), resultDroplet1.getPositionY(), splitXWidth, splitYHeight);
        constrainElectrodesBelowDropletToDropletSize(resultDroplet2, electrodeGrid, resultDroplet2.getPositionX(), resultDroplet2.getPositionY(), splitXWidth, splitYHeight);
    }

    private void splitVertically(ProgramConfiguration programConfiguration) {
        ActionTickResult actionTickResult = new ActionTickResult();

        ElectrodeGrid electrodeGrid = programConfiguration.getElectrodeGrid();
        int electrodeSize = electrodeGrid.getElectrodeSizeOfElectrodeInGrid();
        int originDropletSize = DmfPlatformUtils.electrodeSpanRequiredToMoveDroplet(originDroplet, electrodeSize);

        // Boundaries of origin droplet
        int x1 = originDroplet.getPositionX();
        int x2 = originDroplet.getPositionX() + originDropletSize - 1;
        int y1 = originDroplet.getPositionY();
        int y2 = originDroplet.getPositionY() + originDropletSize - 1;


        if(originDropletSize >= 2) {
            // Clear right-most column of electrodes in origin droplet
            actionTickResult.addTickResult(getClearElectrodeCommands(electrodeGrid, x2, y1, x2, y2));
            x2 -= 1;

            //If origin droplet has an even number of electrodes vertically, add a row to the bottom of the origin droplet.
            if(originDropletSize % 2 == 0) {
                actionTickResult.addTickResult(getSetElectrodeCommands(electrodeGrid, x1, y2 + 1, x2, y2 + 1));
                y2 += 1;
            }
            tickQueue.add(actionTickResult);
            actionTickResult = new ActionTickResult();
        }

        int middleY = (y1 + y2)/2;
        // Remove middle, expand vertically
        actionTickResult.addTickResult(getClearElectrodeCommands(electrodeGrid, x1, middleY, x2, middleY));
        actionTickResult.addTickResult(getSetElectrodeCommands(electrodeGrid, x1, y1 - 1, x2, y1 - 1));
        y1 -= 1;
        actionTickResult.addTickResult(getSetElectrodeCommands(electrodeGrid, x1, y2 + 1, x2, y2 + 1));
        y2 += 1;

        tickQueue.add(actionTickResult);
        actionTickResult = new ActionTickResult();

        // Remove additional rows from middle, add to the sides
        actionTickResult.addTickResult(getClearElectrodeCommands(electrodeGrid, x1, middleY - 1, x2, middleY - 1));
        actionTickResult.addTickResult(getClearElectrodeCommands(electrodeGrid, x1, middleY + 1, x2, middleY + 1));
        actionTickResult.addTickResult(getSetElectrodeCommands(electrodeGrid, x1, y1 - 1, x2, y1 - 1));
        y1 -= 1;
        actionTickResult.addTickResult(getSetElectrodeCommands(electrodeGrid, x1, y2 + 1, x2, y2 + 1));
        y2 += 1;
        tickQueue.add(actionTickResult);

        // The droplets are now split. Last step is to constrain the electrodes below the droplets to the correct size.
        int splitYHeight = (y2 - y1 - 2)/2;
        int splitXWidth = x2 - x1 + 1;
        resultDroplet1.setPositionX(originDroplet.getPositionX());
        resultDroplet1.setPositionY(originDroplet.getPositionY() - 2);
        resultDroplet2.setPositionX(originDroplet.getPositionX());
        resultDroplet2.setPositionY(resultDroplet1.getPositionY() + splitYHeight + 3);

        constrainElectrodesBelowDropletToDropletSize(resultDroplet1, electrodeGrid, resultDroplet1.getPositionX(), resultDroplet1.getPositionY(), splitXWidth, splitYHeight);
        constrainElectrodesBelowDropletToDropletSize(resultDroplet2, electrodeGrid, resultDroplet2.getPositionX(), resultDroplet2.getPositionY(), splitXWidth, splitYHeight);

    }

    private ActionTickResult getSetElectrodeCommands(ElectrodeGrid electrodeGrid, int x1, int y1, int x2, int y2) {
        ActionTickResult actionTickResult = new ActionTickResult();
        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                actionTickResult.addCommand(new SetElectrodeCommand(electrodeGrid.getElectrode(x, y)));
            }
        }
        return actionTickResult;
    }

    private ActionTickResult getClearElectrodeCommands(ElectrodeGrid electrodeGrid, int x1, int y1, int x2, int y2) {
        ActionTickResult actionTickResult = new ActionTickResult();
        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                actionTickResult.addCommand(new ClearElectrodeCommand(electrodeGrid.getElectrode(x, y)));
            }
        }
        return actionTickResult;
    }

    /**
     * After having split, the electrodes below the droplet, are not necessarily the correct size.
     * Eg. if the result droplets are each 2x2 cells, but the split was done in such way, that each droplet was split
     * by a 2x3 snake, this will be corrected, such that only 2x2 cells below the droplets are enabled.
     *
     * @param droplet The droplet should contain the correct volume. The droplet position should be (x1,y1).
     * @param x x coordinate of top left electrode initially below droplet
     * @param y y coordinate of top left electrode initially below droplet
     * @param xWidth width of electrodes initially below droplet
     * @param yWidth width of electrodes initially below droplet
     */
    private void constrainElectrodesBelowDropletToDropletSize(Droplet droplet, ElectrodeGrid electrodeGrid, int x, int y, int xWidth, int yWidth) {
        if(droplet.getPositionX() != x || droplet.getPositionY() != y) {
            throw new IllegalArgumentException("Droplet position must be (x1,y1)");
        }

        int electrodeSize = electrodeGrid.getElectrodeSizeOfElectrodeInGrid();
        int dropletSize = DmfPlatformUtils.electrodeSpanRequiredToMoveDroplet(droplet, electrodeSize);

        while(xWidth < dropletSize) {
            tickQueue.add(getSetElectrodeCommands(electrodeGrid, x + xWidth, y, x + xWidth, y + yWidth - 1));
            xWidth += 1;
        }
        while(xWidth > dropletSize) {
            tickQueue.add(getClearElectrodeCommands(electrodeGrid, x + xWidth - 1, y, x + xWidth - 1, y + yWidth - 1));
            xWidth -= 1;
        }
        while(yWidth < dropletSize) {
            tickQueue.add(getSetElectrodeCommands(electrodeGrid, x, y + yWidth, x + xWidth - 1, y + yWidth));
            yWidth += 1;
        }
        while(yWidth > dropletSize) {
            tickQueue.add(getClearElectrodeCommands(electrodeGrid, x, y + yWidth - 1, x + xWidth - 1, y + yWidth - 1));
            yWidth -= 1;
        }
    }

    private boolean originDropletCanSplitHorizontally(ProgramConfiguration programConfiguration) {
        ElectrodeGrid availableGrid = getAvailableGridForOriginDroplet(programConfiguration);
        int originDropletSize = DmfPlatformUtils.electrodeSpanRequiredToMoveDroplet(originDroplet, availableGrid.getElectrodeSizeOfElectrodeInGrid());
        int y = originDroplet.getPositionY();
        int x1 = originDroplet.getPositionX() - 2;
        int x2 = (originDroplet.getPositionX() * 2 + originDropletSize)/2 + 2;

        // Boundary check to the left.
        if(x1 <= 2 || x2 + originDropletSize >= availableGrid.getXSize()) {
            return false;
        }

        // Check that origin droplet can move 2 to the left.
        return isAllElectrodesAvailableWithinBounds(availableGrid, x1, y, x2, y);
    }

    private boolean originDropletCanSplitVertically(ProgramConfiguration programConfiguration) {
        ElectrodeGrid availableGrid = getAvailableGridForOriginDroplet(programConfiguration);
        int originDropletSize = DmfPlatformUtils.electrodeSpanRequiredToMoveDroplet(originDroplet, availableGrid.getElectrodeSizeOfElectrodeInGrid());
        int x = originDroplet.getPositionX();
        int y1 = originDroplet.getPositionY() - 2;
        int y2 = (originDroplet.getPositionY() * 2 + originDropletSize)/2 + 2;

        // Boundary check
        if(y1 <= 2 || y2 + originDropletSize >= availableGrid.getYSize()) {
            return false;
        }

        // Check that origin droplet can move 2 to the left.
        return isAllElectrodesAvailableWithinBounds(availableGrid, x, y1, x, y2);
    }

    private ElectrodeGrid getAvailableGridForOriginDroplet(ProgramConfiguration programConfiguration) {
        List<Droplet> obstacleDroplets = programConfiguration.getDropletsOnDmfPlatform().stream()
                .filter(d -> !Set.of(originDroplet, resultDroplet1, resultDroplet2).contains(d))
                .toList();
        ElectrodeGrid electrodeGrid = programConfiguration.getElectrodeGrid();
        return ElectrodeGridFactory.getAvailableElectrodeGrid(electrodeGrid, originDroplet, obstacleDroplets);
    }

    private boolean isAllElectrodesAvailableWithinBounds(ElectrodeGrid electrodeGrid, int x1, int y1, int x2, int y2) {
        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                if(electrodeGrid.getElectrode(x, y) == null) {
                    return false;
                }
            }
        }
        return true;
    }
}
