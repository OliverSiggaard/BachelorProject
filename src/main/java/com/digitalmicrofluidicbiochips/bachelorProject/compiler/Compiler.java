package com.digitalmicrofluidicbiochips.bachelorProject.compiler;

import com.digitalmicrofluidicbiochips.bachelorProject.compiler.factory.ActionToTaskMapperFactory;
import com.digitalmicrofluidicbiochips.bachelorProject.compiler.mappers.IActionToTaskMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.task.TaskBase;

import java.util.*;

/**
 * The Compiler class is responsible for compiling high-level actions into Tasks, as well as
 * ensuring proper scheduling of tasks,
 * Tasks can contain 1 or more operations, which, when executed, will
 */
public class Compiler {

    public static Schedule compile(List<ActionBase> actions) {

        Map<Droplet, Queue<ActionBase>> dropletActions = new HashMap<>();

        actions.forEach(task -> {
            task.affectedDroplets().forEach(droplet -> {
                if (!dropletActions.containsKey(droplet)) {
                    dropletActions.put(droplet, new LinkedList<>());
                }

                dropletActions.get(droplet).add(task);
            });
        });

        return new Schedule(dropletActions);
    }
}
