package edu.ponomarev.step.test;

import edu.ponomarev.step.MVC.model.repository.RepositoryFactory;
import edu.ponomarev.step.MVC.model.repository.task.BoxTypeSpecification;
import edu.ponomarev.step.MVC.model.repository.task.TaskSqlRepository;
import edu.ponomarev.step.component.BoxType;
import edu.ponomarev.step.component.task.Task;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.List;


// TODO Допилить остальные течты
public class taskSqlRepositoryTest {
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

    sqlTaskRepository = (TaskSqlRepository) repositoryFactory.getSqlTaskRepository();

    for (var boxType : BoxType.values()) {
      String statement = boxType.toString();

      exampleTasks.put(boxType, new Task(statement));
    }
  }

  @Test
  public void taskShouldBeAddedAndRemovedCorrect() {
    for (var boxType : BoxType.values()) {
      final var boxTypeSpecification = new BoxTypeSpecification(boxType);
      //push task to bd.
      Task newTask = new Task(boxType.toString());
      sqlTaskRepository.add(newTask, boxTypeSpecification);

      //Check existence
      List<Task> containerFromBd = sqlTaskRepository.getList(boxTypeSpecification);
      Assert.assertTrue(containerFromBd.contains(newTask));

      //Check removing
      sqlTaskRepository.remove(newTask, boxTypeSpecification);
      containerFromBd = sqlTaskRepository.getList(boxTypeSpecification);
      Assert.assertFalse(containerFromBd.contains(newTask));
    }
  }

  @Test
  public void singleTaskShouldBeUpdated() {
    for (var boxType : BoxType.values()) {
      final String newStatement = "New statement";
      final var boxTypeSpecification = new BoxTypeSpecification(boxType);
      //Add new task
      Task newTask = new Task(exampleTasks.get(boxType));
      sqlTaskRepository.add(newTask, boxTypeSpecification);

      //Save tasks before update
      List<Task> beforeUpdate = sqlTaskRepository.getList(boxTypeSpecification);
      Task taskBeforeUpdate = beforeUpdate.get(beforeUpdate.indexOf(newTask));

      //Update task
      newTask.setStatement(newStatement);
      sqlTaskRepository.update(newTask, boxTypeSpecification);

      //Save tasks after update
      List<Task> afterUpdate = sqlTaskRepository.getList(boxTypeSpecification);
      Task taskAfterUpdate = afterUpdate.get(afterUpdate.indexOf(newTask));

      //Tasks equals but not statements
      Assert.assertNotEquals(taskBeforeUpdate.getStatement(), taskAfterUpdate.getStatement());
      Assert.assertEquals(taskBeforeUpdate, taskAfterUpdate);

      sqlTaskRepository.remove(newTask, boxTypeSpecification);
    }
  }
}