package com.digitalmicrofluidicbiochips.bachelorProject.compiler.mappers;

import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.MixAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.task.MoveTask;
import com.digitalmicrofluidicbiochips.bachelorProject.model.task.TaskList;

import java.util.Arrays;

public class MixToTaskMapper implements IActionToTaskMapper<MixAction, TaskList>{
    public TaskList mapToTask(MixAction action) {

        //Move to the first position
        MoveTask moveTask1 = new MoveTask(action.getDroplet(), action.getPosX(), action.getPosY());

        //Do a lap in the anti-clockwise direction
        MoveTask moveTask2 = new MoveTask(action.getDroplet(), action.getPosX() + action.getSizeX(), action.getPosY());
        MoveTask moveTask3 = new MoveTask(action.getDroplet(), action.getPosX() + action.getSizeX(), action.getPosY() + action.getSizeY());
        MoveTask moveTask4 = new MoveTask(action.getDroplet(), action.getPosX(), action.getPosY() + action.getSizeY());
        MoveTask moveTask5 = new MoveTask(action.getDroplet(), action.getPosX(), action.getPosY());

        //Return as a list of move tasks.
        return new TaskList(Arrays.asList(moveTask1, moveTask2, moveTask3, moveTask4, moveTask5));
    }
}
