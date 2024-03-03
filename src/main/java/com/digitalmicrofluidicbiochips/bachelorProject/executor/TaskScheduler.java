package com.digitalmicrofluidicbiochips.bachelorProject.executor;

import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.executor.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The task scheduler is responsible for managing the tasks.
 */
public class TaskScheduler {

    private final Map<Droplet, List<Task>> dropletTasks;

    public TaskScheduler(List<Task> tasks) {









        this.dropletTasks = new HashMap<>();
    }













}
