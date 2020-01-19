package edu.ponomarev.step;

import edu.ponomarev.step.MVC.controller.Controller;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
  public static ClassPathXmlApplicationContext context;

  public static void main(String[] args) {
    try {
      context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

      var controller = context.getBean("controller", Controller.class);

    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
}
