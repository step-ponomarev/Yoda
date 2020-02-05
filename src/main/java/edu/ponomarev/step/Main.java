package edu.ponomarev.step;


import edu.ponomarev.step.MVC.controller.Controller;
import edu.ponomarev.step.MVC.view.WindowFactory;
import edu.ponomarev.step.system.ApplicationConfigure;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.awt.*;

public class Main {


  private static Scene scene;

  public static AnnotationConfigApplicationContext context;

  public static void main(String[] args) {
    /*context = new AnnotationConfigApplicationContext(ApplicationConfigure.class);

    var controller = context.getBean("controller", Controller.class);
    controller.start();*/

    try {
      WindowFactory factory = new WindowFactory();
      factory.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
