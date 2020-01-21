package edu.ponomarev.step.test;

import edu.ponomarev.step.MVC.model.Worker;
import edu.ponomarev.step.component.BoxType;
import edu.ponomarev.step.component.task.Task;
import edu.ponomarev.step.component.task.TaskRelations;

import org.junit.*;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.List;

public class WorkerTest {
  private static ClassPathXmlApplicationContext context;

  private Worker worker;
  private HashMap<BoxType, Task> exampleTasks;


  @BeforeClass
  public static void beforeTestClass() {
    context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
  }

  @AfterClass
  public static void afterTestClass() {
    context.close();
  }

  @Before
  public void beforeTest() {
    exampleTasks = new HashMap<>();
    worker = context.getBean("worker", Worker.class);

    for (var boxType : BoxType.values()) {
      String statement = boxType.toString();
      exampleTasks.put(boxType, new Task(statement));
    }
  }

  @Test
  public void containerOfNewTaskMasterShouldBeEmpty() {
    for (var boxType : BoxType.values()) {
      var taskBoxe = worker.getTaskBox(boxType);
      Assert.assertTrue(taskBoxe.isEmpty());
    }
  }

  @Test
  public void taskMustBeAddedAndRemovedCorrect() {
    for (var boxType : BoxType.values()) {
      final var taskRelations = new TaskRelations(boxType);

      final int SIZE_BEFORE_ADDING = worker.getTaskBox(boxType).size();
      //Add new task
      final String statement = boxType.toString();
      Task task = new Task(statement);
      worker.addTask(task, taskRelations);

      final int SIZE_AFTER_ADDING = worker.getTaskBox(boxType).size();
      final boolean TASK_WAS_ADDED = worker.getTaskBox(boxType).contains(task)
          && ( (SIZE_AFTER_ADDING - SIZE_BEFORE_ADDING) == 1);

      //Check existing of added tasks
      Assert.assertTrue(TASK_WAS_ADDED);

      //Check correctness of removing
      worker.removeTask(task, taskRelations);

      final int SIZE_AFTER_REMOVING = worker.getTaskBox(boxType).size();
      final boolean TASK_WAS_DELETED = !worker.getTaskBox(boxType).contains(task);

      Assert.assertEquals(SIZE_BEFORE_ADDING, SIZE_AFTER_REMOVING);
      Assert.assertTrue(TASK_WAS_DELETED);
    }
  }

  @Test
  public void taskShouldBeUpdatedCorrect() {
    for (var boxType : BoxType.values()) {
      final var taskRelations = new TaskRelations(boxType);

      final int SIZE_BEFORE_ADDING = worker.getTaskBox(boxType).size();
      //Add new task
      final String statement = boxType.toString();
      Task task = new Task(statement);
      worker.addTask(task, taskRelations);

      //Make copy of task
      Task copyTask = new Task(task.getStatement());

      //Check existing of added tasks
      final String newStatement = "New Statement";
      task.setStatement(newStatement);

      //Update task on Worker
      worker.updateTask(task, taskRelations);

      //Get updated task
      List<Task> tasksInWorker = worker.getTaskBox(boxType);
      Task updatedTask = tasksInWorker.get(tasksInWorker.indexOf(task));

      //The same tasks, but statement updated
      Assert.assertEquals(updatedTask, task);
      Assert.assertNotEquals(updatedTask.getStatement(), copyTask.getStatement());

      worker.removeTask(task, taskRelations);
    }
  }
}
