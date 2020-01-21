package edu.ponomarev.step.test;

import edu.ponomarev.step.MVC.model.repository.RepositoryFactory;
import edu.ponomarev.step.MVC.model.repository.RepositoryFactory.RepositoryType;
import edu.ponomarev.step.MVC.model.repository.task.TaskSpecification;
import edu.ponomarev.step.MVC.model.repository.task.TaskSqlRepository;
import edu.ponomarev.step.component.BoxType;
import edu.ponomarev.step.component.task.Task;
import edu.ponomarev.step.component.task.TaskRelations;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.*;


// TODO Допилить остальные течты
public class TaskSqlRepositoryTest {
  private static RepositoryFactory repositoryFactory;

  private TaskSqlRepository sqlTaskRepository;

  private HashMap<BoxType, Task> exampleTasks;

  @BeforeClass
  public static void setConnection() {
    var context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

    repositoryFactory = context.getBean("repositoryFactory", RepositoryFactory.class);
  }

  @Before
  public void setState() {
    exampleTasks = new HashMap<>();

    sqlTaskRepository = (TaskSqlRepository) repositoryFactory.getRepository(RepositoryType.TASK_SQL);

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

      final int SIZE_BEFORE_MANIPULATIONS = sqlTaskRepository.getList(taskSpecification).size();

      //push task to bd.
      final var statement = boxType.toString();
      Task newTask = new Task(statement);

      sqlTaskRepository.add(newTask, taskSpecification);

      //Check task adding
      List<Task> sqlTasks = sqlTaskRepository.getList(taskSpecification);

      final int SIZE_AFTER_TASK_ADDING = sqlTasks.size();
      final boolean CONTAINER_CONTAINS_ADDED_TASK = sqlTasks.contains(newTask);
      final boolean ONLY_ONE_TASK_WAS_ADDED = ( SIZE_AFTER_TASK_ADDING - SIZE_BEFORE_MANIPULATIONS ) == 1;

      Assert.assertTrue(CONTAINER_CONTAINS_ADDED_TASK);
      Assert.assertTrue(ONLY_ONE_TASK_WAS_ADDED);

      //Check removing
      sqlTaskRepository.remove(newTask, taskSpecification);
      sqlTasks = sqlTaskRepository.getList(taskSpecification);

      final boolean CONTAINER_CONTAINS_REMOVED_TASK = sqlTasks.contains(newTask);
      final int SIZE_AFTER_MANIPULATIONS = sqlTasks.size();

      Assert.assertFalse(CONTAINER_CONTAINS_REMOVED_TASK);
      Assert.assertEquals(SIZE_BEFORE_MANIPULATIONS, SIZE_AFTER_MANIPULATIONS);
    }
  }

  @Test
  public void singleTaskShouldBeUpdated() {
    for (var boxType : BoxType.values()) {
      final String newStatement = "New statement";

      final var taskRelations = new TaskRelations(boxType);
      final var taskSpecification = new TaskSpecification(taskRelations);

      //Add new task
      Task newTask = new Task(exampleTasks.get(boxType));
      sqlTaskRepository.add(newTask, taskSpecification);

      //Save tasks before update
      List<Task> beforeUpdate = sqlTaskRepository.getList(taskSpecification);
      Task taskBeforeUpdate = beforeUpdate.get(beforeUpdate.indexOf(newTask));

      //Update task
      newTask.setStatement(newStatement);
      sqlTaskRepository.update(newTask, taskSpecification);

      //Save tasks after update
      List<Task> afterUpdate = sqlTaskRepository.getList(taskSpecification);
      Task taskAfterUpdate = afterUpdate.get(afterUpdate.indexOf(newTask));

      //Tasks equals but not statements
      final boolean SIZE_BEFORE_AND_AFTER_UPDATE_EQUAL = beforeUpdate.size() == afterUpdate.size();
      final boolean TASK_BEFORE_AND_AFTER_UPDATE_THE_SAME = taskBeforeUpdate.equals(taskAfterUpdate);
      final boolean TASK_INFOT_BEFORE_AND_AFTER_UPDATE_EQUAL =
          taskBeforeUpdate.getStatement().equals(taskAfterUpdate.getStatement());

      Assert.assertTrue(SIZE_BEFORE_AND_AFTER_UPDATE_EQUAL);
      Assert.assertTrue(TASK_BEFORE_AND_AFTER_UPDATE_THE_SAME);
      Assert.assertFalse(TASK_INFOT_BEFORE_AND_AFTER_UPDATE_EQUAL);

      sqlTaskRepository.remove(newTask, taskSpecification);
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
      final int SIZE_BEFORE_ADDING = sqlTaskRepository.getList(taskSpecification).size();

      sqlTaskRepository.add(createdTasks, taskSpecification);

      var sqlTasks = sqlTaskRepository.getList(taskSpecification);

      final int SIZE_AFTER_ADDING = sqlTasks.size();

      final boolean SIZE_WAS_INCREASED_CORRECT = (SIZE_AFTER_ADDING - SIZE_BEFORE_ADDING) == AMOUNT_OF_CREATED_TASKS;

      boolean EVERY_TASK_WAS_ADDED = true;
      for (var task : createdTasks) {
        EVERY_TASK_WAS_ADDED &= sqlTasks.contains(task);
      }

      Assert.assertTrue(SIZE_WAS_INCREASED_CORRECT);
      Assert.assertTrue(EVERY_TASK_WAS_ADDED);

      //Create queue to removing
      Queue<Task> removingTasks = new LinkedList<>(createdTasks);

      //Remove tasks
      sqlTaskRepository.remove(removingTasks);

      sqlTasks = sqlTaskRepository.getList(taskSpecification);
      boolean EVERY_TASK_WAS_REMOVED = true;
      for (var task : createdTasks) {
        EVERY_TASK_WAS_ADDED &= !sqlTasks.contains(task);
      }

      final int SIZE_AFTER_REMOVING = sqlTaskRepository.getList(taskSpecification).size();


      Assert.assertEquals(SIZE_BEFORE_ADDING, SIZE_AFTER_REMOVING);
      Assert.assertTrue(EVERY_TASK_WAS_REMOVED);
    }
  }
}