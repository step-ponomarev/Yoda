package edu.ponomarev.step;


import edu.ponomarev.step.MVC.controller.Controller;
import edu.ponomarev.step.system.ApplicationConfigure;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.awt.*;

public class Main {
  public static AnnotationConfigApplicationContext context;

  public static void main(String[] args) {
    context = new AnnotationConfigApplicationContext(ApplicationConfigure.class);

    var controller = context.getBean("controller", Controller.class);
    controller.start();
  }
}
