package edu.ponomarev.step;

import edu.ponomarev.step.controller.Controller;
import edu.ponomarev.step.view.main.Window;
import edu.ponomarev.step.manager.DataHandler;

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
