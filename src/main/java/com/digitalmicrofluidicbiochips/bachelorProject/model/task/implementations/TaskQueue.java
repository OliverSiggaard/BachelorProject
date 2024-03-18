package com.digitalmicrofluidicbiochips.bachelorProject.model.task.implementations;

import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.task.TaskBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.task.TaskStatus;
import lombok.Getter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A Task queue consists of a list of tasks that are executed after each other.
 * The next task is only executed if and when the previous task was completed.
 *
 * The queue is completed when all tasks are completed.
 */

@Getter
public class TaskQueue extends TaskBase {

    private final List<TaskBase> tasks;
    private int currentTaskIndex = 0;

    public TaskQueue(List<TaskBase> tasks) {
        this.tasks = tasks;
    }

    @Override
    public Set<Droplet> affectedDroplets() {
        return tasks.stream()
                .flatMap(task -> task.affectedDroplets().stream())
                .collect(Collectors.toSet());
    }

    @Override
    public void beforeExecution() {
        setStatus(TaskStatus.IN_PROGRESS);
    }

    @Override
    public void executeTick() {

        if(currentTaskIndex >= tasks.size()) return;

        TaskBase currentTask = tasks.get(currentTaskIndex);
        if(currentTask.getStatus() == TaskStatus.COMPLETED) {
            throw new IllegalStateException("Current task can not be completed. Has to be Not Started or In Progress.");
        }

        switch (currentTask.getStatus()) {
            case NOT_STARTED -> {
                currentTask.beforeExecution();
                currentTask.executeTick();
            }
            case IN_PROGRESS -> currentTask.executeTick();
            case FAILED -> {
                setStatus(TaskStatus.FAILED);
                return;
            }
        }

        //Was the task completed in this tick?
        if(currentTask.getStatus() == TaskStatus.COMPLETED) {
            currentTask.afterExecution();
            currentTaskIndex++;
            if (currentTaskIndex < tasks.size()) return;

            //If the last task was completed, the task list is also completed.
            setStatus(TaskStatus.COMPLETED);
        }
    }

    @Override
    public void afterExecution() {

    }

}
