package edu.ponomarev.step;

import edu.ponomarev.step.MVC.controller.Controller;
import edu.ponomarev.step.MVC.model.TaskWorker;
import edu.ponomarev.step.MVC.view.Window;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
  public static ClassPathXmlApplicationContext context;

  public static void main(String[] args) {
    try {
      context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

      var window = context.getBean("window", Window.class);
      var taskWorker = new TaskWorker();

      var controller = new Controller(window, taskWorker);
      controller.initView();

    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
}
