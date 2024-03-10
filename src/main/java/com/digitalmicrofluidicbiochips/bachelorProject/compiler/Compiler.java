package com.digitalmicrofluidicbiochips.bachelorProject.compiler;

import com.digitalmicrofluidicbiochips.bachelorProject.compiler.factory.ActionToTaskMapperFactory;
import com.digitalmicrofluidicbiochips.bachelorProject.compiler.mappers.IActionToTaskMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.task.TaskBase;

import java.util.ArrayList;
import java.util.List;

/**
 * The Compiler class is responsible for compiling high-level actions into Tasks, as well as
 * ensuring proper scheduling of tasks,
 * Tasks can contain 1 or more operations, which, when executed, will
 */
public class Compiler {

    public static Schedule compile(List<ActionBase> actions) {

        List<TaskBase> tasks = new ArrayList<>();
        actions.forEach(action -> {
            IActionToTaskMapper mapper = ActionToTaskMapperFactory.getMapper(action.getClass());
            TaskBase taskBase = mapper.mapToTask(action);
            tasks.add(taskBase);
        });

        return new Schedule(tasks);
    }

}
