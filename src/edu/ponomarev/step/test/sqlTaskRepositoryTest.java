package edu.ponomarev.step.test;

import edu.ponomarev.step.MVC.model.repository.RepositoryFactory;
import edu.ponomarev.step.MVC.model.repository.task.sqlTaskRepository;
import edu.ponomarev.step.component.task.InformatedTask;
import edu.ponomarev.step.component.task.Task;
import edu.ponomarev.step.component.taskContainer.termContainer.ContainerVariable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.List;

public class sqlTaskRepositoryTest {
  private static RepositoryFactory repositoryFactory;

  private sqlTaskRepository sqlTaskRepository;

  private HashMap<ContainerVariable.ContainerType, InformatedTask> exampleTasks;

  @BeforeClass
  public static void setConnection() {
    var context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

    repositoryFactory = context.getBean("repositoryFactory", RepositoryFactory.class);
  }

  @Before
  public void setState() {
    exampleTasks = new HashMap<>();

    sqlTaskRepository = (sqlTaskRepository) repositoryFactory.getSqlTaskRepository();

    for (var taskType : ContainerVariable.BOX_VARIABLES) {
      String statement = taskType.name;
      ContainerVariable.ContainerType boxType = taskType.type;

      exampleTasks.put(boxType, new InformatedTask(new Task(statement), boxType));
    }
  }

  @Test
  public void taskShouldBeAddedAndRemovedCorrect() {
    for (var taskType : ContainerVariable.BOX_VARIABLES) {
      //push task to bd.
      InformatedTask newTask = new InformatedTask(exampleTasks.get(taskType.type), taskType.type);
      sqlTaskRepository.add(newTask);

      //Check existence
      List<Task> containerFromBd = sqlTaskRepository.getList(taskType.type);
      Assert.assertTrue(containerFromBd.contains(newTask));

      //Check removing
      sqlTaskRepository.remove(newTask);
      containerFromBd = sqlTaskRepository.getList(taskType.type);
      Assert.assertFalse(containerFromBd.contains(newTask));
    }
  }

  @Test
  public void singleTaskShouldBeUpdated() {
    for (var taskType : ContainerVariable.BOX_VARIABLES) {
      final String newStatement = "New statement";
      //Add new task
      InformatedTask newTask = new InformatedTask(exampleTasks.get(taskType.type), taskType.type);
      sqlTaskRepository.add(newTask);

      //Save tasks before update
      List<Task> beforeUpdate = sqlTaskRepository.getList(taskType.type);
      Task taskBeforeUpdate = beforeUpdate.get(beforeUpdate.indexOf(newTask));

      //Update task
      newTask.setStatement(newStatement);
      sqlTaskRepository.update(newTask);

      //Save tasks after update
      List<Task> afterUpdate = sqlTaskRepository.getList(taskType.type);
      Task taskAfterUpdate = afterUpdate.get(afterUpdate.indexOf(newTask));

      //The same tasks with different statement
      Assert.assertNotEquals(taskBeforeUpdate.getStatement(), taskAfterUpdate.getStatement());
      Assert.assertEquals(taskBeforeUpdate, taskAfterUpdate);

      sqlTaskRepository.remove(newTask);
    }
  }
}
