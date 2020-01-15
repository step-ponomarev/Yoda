package edu.ponomarev.step;

import edu.ponomarev.step.MVC.controller.Controller;
import edu.ponomarev.step.MVC.view.main.Window;
import edu.ponomarev.step.MVC.model.DataHandler;

public class Main {
  public static void main(String[] args) {
    try {
      DataHandler handler1 = new DataHandler();
      Window window = new Window("Yoda");

      Controller controller = new Controller(window, handler1);
      controller.initView();
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
}
