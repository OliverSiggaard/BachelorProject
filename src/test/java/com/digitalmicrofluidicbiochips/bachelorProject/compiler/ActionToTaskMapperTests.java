package com.digitalmicrofluidicbiochips.bachelorProject.compiler;

import com.digitalmicrofluidicbiochips.bachelorProject.compiler.factory.ActionToTaskMapperFactory;
import com.digitalmicrofluidicbiochips.bachelorProject.compiler.mappers.IActionToTaskMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations.InputAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations.MixAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations.MoveAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.task.implementations.InputTask;
import com.digitalmicrofluidicbiochips.bachelorProject.model.task.implementations.MoveTask;
import com.digitalmicrofluidicbiochips.bachelorProject.model.task.TaskBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.task.implementations.TaskQueue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ActionToTaskMapperTests {


    private TaskBase getTaskUsingActionToTaskMapperFactory(ActionBase actionBase) {
        IActionToTaskMapper mapper = ActionToTaskMapperFactory.getMapper(actionBase.getClass());
        return mapper.mapToTask(actionBase);
    }

    @Test
    public void inputActionMappedCorrectByFactory() {

        InputAction inputAction = new InputAction("someId", 1, 2, 3);
        inputAction.setDroplet(new Droplet("someDroplet", 1, 2, 3));

        TaskBase taskBase = getTaskUsingActionToTaskMapperFactory(inputAction);
        Assertions.assertTrue(taskBase instanceof InputTask);

        InputTask inputTask = (InputTask) taskBase;

        Assertions.assertEquals(inputAction.getDroplet(), inputTask.getDroplet());
        Assertions.assertEquals(inputAction.getPosX(), inputTask.getPosX());
        Assertions.assertEquals(inputAction.getPosY(), inputTask.getPosY());
        Assertions.assertEquals(inputAction.getVolume(), inputTask.getVolume());
        Assertions.assertEquals(1, inputTask.affectedDroplets().size());
        Assertions.assertTrue(inputTask.affectedDroplets().contains(inputAction.getDroplet()));
    }

    @Test
    public void moveActionMappedCorrectByFactory() {

        MoveAction moveAction = new MoveAction("someId", 1, 2);
        moveAction.setDroplet(new Droplet("someDroplet", 3, 4, 5));

        TaskBase taskBase = getTaskUsingActionToTaskMapperFactory(moveAction);
        Assertions.assertTrue(taskBase instanceof MoveTask);

        MoveTask moveTask = (MoveTask) taskBase;

        Assertions.assertEquals(moveAction.getDroplet(), moveTask.getDroplet());
        Assertions.assertEquals(moveAction.getPosX(), moveTask.getPosX());
        Assertions.assertEquals(moveAction.getPosY(), moveTask.getPosY());
        Assertions.assertEquals(1, moveTask.affectedDroplets().size());
        Assertions.assertTrue(moveTask.affectedDroplets().contains(moveAction.getDroplet()));
    }

    @Test
    public void mixActionMappedCorrectByFactory() {

        MixAction mixAction = new MixAction("someId", 1, 2, 3, 4);
        mixAction.setDroplet(new Droplet("someDroplet", 5, 6, 7));

        TaskBase taskBase = getTaskUsingActionToTaskMapperFactory(mixAction);
        Assertions.assertTrue(taskBase instanceof TaskQueue);

        TaskQueue taskQueue = (TaskQueue) taskBase;

        Assertions.assertEquals(1, taskQueue.affectedDroplets().size());
        Assertions.assertTrue(taskQueue.affectedDroplets().contains(mixAction.getDroplet()));

        // There should be 5 tasks in the list. 1 to move to start position, and 4 to mix in a square.
        Assertions.assertEquals(5, taskQueue.getTasks().size());
        List<TaskBase> tasks = taskQueue.getTasks();

        // Check that the tasks are MoveTasks and that they are placed correctly.
        Assertions.assertTrue(tasks.get(0) instanceof MoveTask);
        MoveTask t0 = (MoveTask) tasks.get(0);
        Assertions.assertEquals(mixAction.getDroplet(), t0.getDroplet());
        Assertions.assertEquals(mixAction.getPosX(), t0.getPosX());
        Assertions.assertEquals(mixAction.getPosY(), t0.getPosY());

        Assertions.assertTrue(tasks.get(1) instanceof MoveTask);
        MoveTask t1 = (MoveTask) tasks.get(1);
        Assertions.assertEquals(mixAction.getDroplet(), t1.getDroplet());
        Assertions.assertEquals(mixAction.getPosX() + mixAction.getSizeX(), t1.getPosX());
        Assertions.assertEquals(mixAction.getPosY(), t1.getPosY());

        Assertions.assertTrue(tasks.get(2) instanceof MoveTask);
        MoveTask t2 = (MoveTask) tasks.get(2);
        Assertions.assertEquals(mixAction.getDroplet(), t2.getDroplet());
        Assertions.assertEquals(mixAction.getPosX() + mixAction.getSizeX(), t2.getPosX());
        Assertions.assertEquals(mixAction.getPosY() + mixAction.getSizeY(), t2.getPosY());

        Assertions.assertTrue(tasks.get(3) instanceof MoveTask);
        MoveTask t3 = (MoveTask) tasks.get(3);
        Assertions.assertEquals(mixAction.getDroplet(), t3.getDroplet());
        Assertions.assertEquals(mixAction.getPosX(), t3.getPosX());
        Assertions.assertEquals(mixAction.getPosY() + mixAction.getSizeY(), t3.getPosY());

        Assertions.assertTrue(tasks.get(4) instanceof MoveTask);
        MoveTask t4 = (MoveTask) tasks.get(4);
        Assertions.assertEquals(mixAction.getDroplet(), t4.getDroplet());
        Assertions.assertEquals(mixAction.getPosX(), t4.getPosX());
        Assertions.assertEquals(mixAction.getPosY(), t4.getPosY());
    }










}
