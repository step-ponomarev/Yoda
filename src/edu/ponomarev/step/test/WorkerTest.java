package edu.ponomarev.step.test;

import edu.ponomarev.step.MVC.model.Worker;
import edu.ponomarev.step.MVC.model.component.BoxType;
import edu.ponomarev.step.MVC.model.component.project.Project;
import edu.ponomarev.step.MVC.model.component.task.Task;
import edu.ponomarev.step.MVC.model.component.task.TaskRelations;

import edu.ponomarev.step.system.ApplicationConfigure;
import org.junit.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WorkerTest {
  private static ApplicationContext context;
  private final static int PROJECT_AMOUNT = 10;

  private Worker worker;
  private HashMap<BoxType, Task> exampleTasks;
  private List<Project> projects;


  @BeforeClass
  public static void beforeTestClass() {
    context = new AnnotationConfigApplicationContext(ApplicationConfigure.class);
  }

  @Before
  public void beforeTest() {
    exampleTasks = new HashMap<>();
    worker = context.getBean("worker", Worker.class);

    for (var boxType : BoxType.values()) {
      String statement = boxType.toString();
      exampleTasks.put(boxType, new Task(statement));
    }


    projects = new ArrayList<>(PROJECT_AMOUNT);
    for (int i = 0; i < PROJECT_AMOUNT; ++i) {
      Project project = new Project(Integer.toString(i));
      projects.add(project);
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

  @Test
  public void projectShouldBeAddedAndRemovedCorrect() {
    final int SIZE_BEFORE_ADDING = worker.getProjects().size();

    for (var project : projects) {
      worker.addProject(project);
    }

    final int SIZE_AFTER_ADDING = worker.getProjects().size();

    boolean SIZE_AFTER_ADDING_IS_CORRECT = (SIZE_AFTER_ADDING - SIZE_BEFORE_ADDING) == PROJECT_AMOUNT;
    boolean EVERY_PROJECT_WAS_ADDED = true;
    for (var project : projects) {
      EVERY_PROJECT_WAS_ADDED &= worker.getProjects().contains(project);
    }

    Assert.assertTrue(EVERY_PROJECT_WAS_ADDED);
    Assert.assertTrue(SIZE_AFTER_ADDING_IS_CORRECT);

    for (var project : projects) {
      worker.removeProject(project);
    }

    final int SIZE_AFTER_REMOVING = worker.getProjects().size();

    boolean EVERY_PROJECT_WAS_REMOVED = true;
    for (var project : projects) {
      EVERY_PROJECT_WAS_ADDED &= !worker.getProjects().contains(project);
    }

    Assert.assertEquals(SIZE_BEFORE_ADDING, SIZE_AFTER_REMOVING);
    Assert.assertTrue(EVERY_PROJECT_WAS_REMOVED);
  }
 }
