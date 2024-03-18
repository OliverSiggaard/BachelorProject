package com.digitalmicrofluidicbiochips.bachelorProject.compiler.mappers;

import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.InputAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.task.implementations.InputTask;

public class InputToTaskMapper implements IActionToTaskMapper<InputAction, InputTask> {
    @Override
    public InputTask mapToTask(InputAction action) {
        return new InputTask(action.getDroplet(), action.getPosX(), action.getPosY(), action.getVolume());
    }

}
