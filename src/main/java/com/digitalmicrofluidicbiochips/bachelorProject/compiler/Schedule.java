package com.digitalmicrofluidicbiochips.bachelorProject.compiler;

import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;

import com.digitalmicrofluidicbiochips.bachelorProject.model.task.TaskBase;

import java.util.*;

/**
 * A schedule is a collection of tasks that are to be executed by the executor. The schedule is responsible for
 * ensuring that tasks are executed in the correct order, and that that only available tasks are executable.
 */
public class Schedule {

    private final Map<Droplet, Queue<TaskBase>> dropletTasks;

    public Schedule(List<TaskBase> tasks) {
        this.dropletTasks = new HashMap<>();

        tasks.forEach(task -> {
            task.affectedDroplets().forEach(droplet -> {
                if (dropletTasks.containsKey(droplet)) {
                    dropletTasks.get(droplet).add(task);
                } else {
                    Queue<TaskBase> newQueue = new LinkedList<>();
                    newQueue.add(task);
                    dropletTasks.put(droplet, newQueue);
                }
            });
        });
    }















}
