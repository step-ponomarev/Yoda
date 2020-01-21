package edu.ponomarev.step;

import edu.ponomarev.step.MVC.controller.Controller;
import edu.ponomarev.step.system.ApplicationConfigure;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
  public static ApplicationContext context;

  public static void main(String[] args) {
    try {
      context = new AnnotationConfigApplicationContext(ApplicationConfigure.class);

      var controller = context.getBean("controller", Controller.class);

    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
}
