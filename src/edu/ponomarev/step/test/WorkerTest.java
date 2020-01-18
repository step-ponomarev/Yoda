package edu.ponomarev.step.test;

import edu.ponomarev.step.MVC.model.Worker;
import edu.ponomarev.step.Main;
import org.junit.*;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class WorkerTest {
  private Worker worker;

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
    worker = new Worker();
  }

  @Test
  public void containerOfNewTaskMasterShouldBeEmpty() {
    for (var entry : worker.getTaskContainer().entrySet()) {
      Assert.assertTrue(entry.getValue().isEmpty());
    }
  }
}
