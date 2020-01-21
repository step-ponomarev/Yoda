package edu.ponomarev.step.test;

import edu.ponomarev.step.MVC.model.repository.RepositoryFactory;
import edu.ponomarev.step.MVC.model.repository.task.TaskSerializator;

import edu.ponomarev.step.MVC.model.repository.task.TaskSpecification;
import edu.ponomarev.step.component.BoxType;
import edu.ponomarev.step.component.task.Task;

import edu.ponomarev.step.component.task.TaskRelations;
import edu.ponomarev.step.system.ApplicationConfigure;
import org.junit.*;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.*;

public class TaskSerializatorTest {
  private static RepositoryFactory repositoryFactory;

  private TaskSerializator taskSerializator;

  private HashMap<BoxType, Task> exampleTasks;

  @BeforeClass
  public static void setConnection() {
    var context = new AnnotationConfigApplicationContext(ApplicationConfigure.class);

    repositoryFactory = context.getBean("repositoryFactory", RepositoryFactory.class);
  }

  @Before
  public void setState() {
    exampleTasks = new HashMap<>();

    taskSerializator = (TaskSerializator) repositoryFactory.getRepository(RepositoryFactory.RepositoryType.TASK_OFFLINE);

    for (var boxType : BoxType.values()) {
      String statement = boxType.toString();
      exampleTasks.put(boxType, new Task(statement));
    }
  }

  @Test
  public void taskShouldBeAddedAndRemovedCorrect() {
    for (var boxType : BoxType.values()) {
      final var taskRelations = new TaskRelations(boxType);
      final var taskSpecification = new TaskSpecification(taskRelations);

      final int SIZE_BEFORE_MANIPULATIONS = taskSerializator.getList(taskSpecification).size();

      //push task to bd.
      final var statement = boxType.toString();
      Task newTask = new Task(statement);

      taskSerializator.add(newTask, taskSpecification);

      //Check task adding
      List<Task> sqlTasks = taskSerializator.getList(taskSpecification);

      final int SIZE_AFTER_TASK_ADDING = sqlTasks.size();
      final boolean CONTAINER_CONTAINS_ADDED_TASK = sqlTasks.contains(newTask);
      final boolean ONLY_ONE_TASK_WAS_ADDED = ( SIZE_AFTER_TASK_ADDING - SIZE_BEFORE_MANIPULATIONS ) == 1;

      Assert.assertTrue(CONTAINER_CONTAINS_ADDED_TASK);
      Assert.assertTrue(ONLY_ONE_TASK_WAS_ADDED);

      //Check removing
      taskSerializator.remove(newTask, taskSpecification);
      sqlTasks = taskSerializator.getList(taskSpecification);

      final boolean CONTAINER_CONTAINS_REMOVED_TASK = sqlTasks.contains(newTask);
      final int SIZE_AFTER_MANIPULATIONS = sqlTasks.size();

      Assert.assertFalse(CONTAINER_CONTAINS_REMOVED_TASK);
      Assert.assertEquals(SIZE_BEFORE_MANIPULATIONS, SIZE_AFTER_MANIPULATIONS);
    }
  }

  @Test
  public void taskListShouldBeAddedAndRemovedCorrect() {
    for (var boxType : BoxType.values()) {
      final var taskRelations = new TaskRelations(boxType);
      final var taskSpecification = new TaskSpecification(taskRelations);

      //Set up list to adding.
      final int AMOUNT_OF_CREATED_TASKS = 10;

      List<Task> createdTasks = new ArrayList<>(AMOUNT_OF_CREATED_TASKS);

      String statement;
      for (int i = 0; i < AMOUNT_OF_CREATED_TASKS; ++i) {
        statement = Integer.toString(i);
        createdTasks.add(new Task(statement));
      }

      //Add task list
      final int SIZE_BEFORE_ADDING = taskSerializator.getList(taskSpecification).size();

      taskSerializator.add(createdTasks, taskSpecification);

      var sqlTasks = taskSerializator.getList(taskSpecification);

      final int SIZE_AFTER_ADDING = sqlTasks.size();

      final boolean SIZE_WAS_INCREASED_CORRECT = (SIZE_AFTER_ADDING - SIZE_BEFORE_ADDING) == AMOUNT_OF_CREATED_TASKS;

      boolean EVERY_TASK_WAS_ADDED = true;
      for (var task : createdTasks) {
        EVERY_TASK_WAS_ADDED &= sqlTasks.contains(task);
      }

      Assert.assertTrue(SIZE_WAS_INCREASED_CORRECT);
      Assert.assertTrue(EVERY_TASK_WAS_ADDED);

      //Remove
      Queue<Task> removingTasks = new LinkedList<>(createdTasks);
      taskSerializator.remove(removingTasks);

      //Check removing
      sqlTasks = taskSerializator.getList(taskSpecification);

      final int SIZE_AFTER_REMOVING = sqlTasks.size();
      boolean EVERY_TASK_WAS_REMOVED = true;
      for (var task : createdTasks) {
        EVERY_TASK_WAS_ADDED &= !sqlTasks.contains(task);
      }


      Assert.assertEquals(SIZE_BEFORE_ADDING, SIZE_AFTER_REMOVING);
      Assert.assertTrue(EVERY_TASK_WAS_REMOVED);
    }
  }
}
