package edu.ponomarev.step.test;

import edu.ponomarev.step.MVC.model.TaskWorker;
import edu.ponomarev.step.Main;
import org.junit.*;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TaskWorkerTest {
  private TaskWorker taskWorker;

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
    taskWorker = new TaskWorker();
  }

  @Test
  public void containerOfNewTaskMasterShouldBeEmpty() {
    for (var entry : taskWorker.getContainer().entrySet()) {
      Assert.assertTrue(entry.getValue().isEmpty());
    }
  }
}
