// TODO Переписать тесты под новую логику

/*package edu.ponomarev.step.test;

import edu.ponomarev.step.MVC.model.Worker;
import edu.ponomarev.step.Main;
import edu.ponomarev.step.component.task.InformatedTask;
import edu.ponomarev.step.component.task.Task;
import org.junit.*;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.List;

public class WorkerTest {
  private Worker worker;
  private HashMap<ContainerVariable.ContainerType, InformatedTask> exampleTasks;


  @BeforeClass
  public static void beforeTestClass() {
    Main.context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
  }

  @AfterClass
  public static void afterTestClass() {
    Main.context.close();
  }

  @Before
  public void beforeTest() {
    exampleTasks = new HashMap<>();
    worker = new Worker();

    for (var taskType : ContainerVariable.BOX_VARIABLES) {
      String statement = taskType.name;
      ContainerVariable.ContainerType boxType = taskType.type;

      exampleTasks.put(boxType, new InformatedTask(new Task(statement), boxType));
    }
  }

  @Test
  public void containerOfNewTaskMasterShouldBeEmpty() {
    for (var entry : worker.getTaskBoxes().entrySet()) {
      Assert.assertTrue(entry.getValue().isEmpty());
    }
  }

  @Test
  public void taskMustBeAddedAndRemovedCorrect() {
    for (var taskType : ContainerVariable.BOX_VARIABLES) {
      //Add new task
      InformatedTask currentTestTask = new InformatedTask(exampleTasks.get(taskType.type), taskType.type);
      worker.addTask(currentTestTask);

      List<Task> tasksInWorker = worker.getTaskBoxes(taskType.type).getList();

      //Check existing of added tasks
      Assert.assertTrue(tasksInWorker.contains(currentTestTask));

      //Check correctness of removing
      worker.removeTask(currentTestTask);

      Assert.assertFalse(tasksInWorker.contains(currentTestTask));
    }
  }

  @Test
  public void taskShouldBeUpdatedCorrect() {
    for (var taskType : ContainerVariable.BOX_VARIABLES) {
      //Add new task
      InformatedTask currentTestTask = new InformatedTask(exampleTasks.get(taskType.type), taskType.type);
      worker.addTask(currentTestTask);

      //Make copy of task
      Task copyTask = new Task(currentTestTask.getStatement());

      //Check existing of added tasks
      final String newStatement = "New Statement";
      currentTestTask.setStatement(newStatement);

      //Update task on Worker
      worker.updateTask(currentTestTask);

      //Get updated task
      List<Task> tasksInWorker = worker.getTaskBoxes(taskType.type).getList();
      Task updatedTask = tasksInWorker.get(tasksInWorker.indexOf(currentTestTask));

      //The same tasks, but statement updated
      Assert.assertEquals(updatedTask, currentTestTask);
      Assert.assertNotEquals(updatedTask.getStatement(), copyTask.getStatement());

      worker.removeTask(currentTestTask);
    }
  }
}
*/