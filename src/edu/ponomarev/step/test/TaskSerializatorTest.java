package edu.ponomarev.step.test;

import edu.ponomarev.step.MVC.model.repository.RepositoryFactory;
import edu.ponomarev.step.MVC.model.repository.task.TaskSerializator;
import edu.ponomarev.step.component.task.InformatedTask;
import edu.ponomarev.step.component.task.Task;
import edu.ponomarev.step.component.taskContainer.termContainer.ContainerVariable;

import edu.ponomarev.step.component.taskContainer.termContainer.TermTaskContainer;
import org.junit.*;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskSerializatorTest {
  private static RepositoryFactory repositoryFactory;

  private TaskSerializator taskSerializator;

  private HashMap<ContainerVariable.ContainerType, InformatedTask> exampleTasks;

  @BeforeClass
  public static void setConnection() {
    var context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

    repositoryFactory = context.getBean("repositoryFactory", RepositoryFactory.class);
  }

  @Before
  public void setState() {
    exampleTasks = new HashMap<>();

    taskSerializator = (TaskSerializator) repositoryFactory.getTaskSerializator();

    for (var taskType : ContainerVariable.BOX_VARIABLES) {
      String statement = taskType.name;
      ContainerVariable.ContainerType boxType = taskType.type;

      exampleTasks.put(boxType, new InformatedTask(new Task(statement), boxType));
    }
  }

  @Test
  public void taskShouldBeAddedAndRemovedCorrect() {
    for (var taskType : ContainerVariable.BOX_VARIABLES) {
      //Saving new task
      taskSerializator.add(exampleTasks.get(taskType.type));

      //Check existing of added tasks
      ArrayList<Task> tasksFromDist = (ArrayList<Task>) taskSerializator.getList(taskType.type);
      InformatedTask savedTask = exampleTasks.get(taskType.type);

      Assert.assertTrue(tasksFromDist.contains(savedTask));

      //Check correctness of removing
      taskSerializator.remove(exampleTasks.get(taskType.type));
      tasksFromDist = (ArrayList<Task>) taskSerializator.getList(taskType.type);

      Assert.assertFalse(tasksFromDist.contains(savedTask));
    }
  }

  @Test
  public void taskListShouldBeSavedOnDiskCorrect() {
    for (var taskType : ContainerVariable.BOX_VARIABLES) {
      //Add list
      var container = new TermTaskContainer(taskType.type);
      taskSerializator.add(container);

      //Get taskList from disk
      var containerFromDisk = new TermTaskContainer(taskType.type);
      containerFromDisk.setList(taskSerializator.getList(taskType.type));

      //Check equals of lists;
      Assert.assertArrayEquals(container.getList().toArray(), containerFromDisk.getList().toArray());
    }
  }
}
