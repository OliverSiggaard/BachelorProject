package com.digitalmicrofluidicbiochips.bachelorProject.compiler.mappers;

import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.MoveAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.task.implementations.MoveTask;

public class MoveToTaskMapper implements IActionToTaskMapper<MoveAction, MoveTask>{
    @Override
    public MoveTask mapToTask(MoveAction action) {
        return new MoveTask(action.getDroplet(), action.getPosX(), action.getPosY());
    }

}
