package com.digitalmicrofluidicbiochips.bachelorProject.compiler.mappers;

import com.digitalmicrofluidicbiochips.bachelorProject.model.task.TaskBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;

public interface IActionToTaskMapper <T extends ActionBase, U extends TaskBase>{

    U mapToTask(T action);

}
