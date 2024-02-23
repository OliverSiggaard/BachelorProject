package com.digitalmicrofluidicbiochips.bachelorProject;

import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;

public class Executor {

    private final ProgramConfiguration programConfiguration;

    //Electrodes
    /* TODO:
     *  The Electrode class should be provide a String to turn the electrode on or off, and a method to check if the electrode is on or off.
     */

    //Representation of the chip.
    /* TODO:
     *  The chip electrodes should be represented as a 2D array of references to the electrodes.
     *  This should prob. be in its own class, that takes a List<Electrode> in the constructor.
     *  Should be solvable by inserting into a Map of Lists, where the key is the x, and then lists all electrodes at that x value.
     *  The "grid" can then be deducted by sorting the lists by y, and the map by x - Or something like that.
     *  Unit tests should be written for this, to ensure that the grid is correctly constructed.
     *  Maybe the grid should even be references to a "grid-cell" that then have a reference to the electrode in the cell.
     *  This allows for us to assign more states to the cell, eg. if a droplet has its top left corner in the cell, etc.
     *  A PrettyPrint method should prob. be implemented, to visualize the state of the grid in the console.
     *
     * TODO:
     *  The board should prob. also be convertable to/from to some sort of 2d array of bytes, for efficient storage and
     *  manipulation, when we later have to simulate the board. Maybe this can wait for now though.
     *  Byte datatype should be sufficient, as each electrode can then take 256 states, which should be enough,
     *  to represent the different states of the electrodes. (eg. if a droplet is above, or if the electrode is on or off, etc.)
     */

    //Representation of the droplets. (+ droplets on board)
    /* TODO:
     *  Diameter of droplet has to be calculated from the volume. See comment in the bottom of the Droplet class.
     *  Luca agreed, that we just took Wenje's code.
     *
     * TODO:
     *  Given a "Chip Electrode" class (explained in the first TODOs) and a specific droplet, we should be able to
     *  receive all of the cells that the droplet may move to.
     *  See Wenjes rapport page. 56, figure 4.5. Luca agreed, that we could use a similar approach.
     *  We should here think about how to later be able integrate obstructed cells, in case of contaminated cells, etc.
     */

    //Coordination of actions
    /* TODO:
     *  Given a List of Actions, the executor should be able to deduct which of the actions are possible to execute next.
     *  This includes maybe running multiple actions at the same time (or at least before a tick).
     */


    public Executor(ProgramConfiguration programConfiguration) {
        this.programConfiguration = programConfiguration;
    }

    public void startExecution() {
        throw new UnsupportedOperationException("Not implemented yet");
    }









}
