package edu.ponomarev.step;

import edu.ponomarev.step.view.Main.Window;
import edu.ponomarev.step.manager.DataBaseManager;
import edu.ponomarev.step.manager.DataHandler;

public class Main {
  public static void main(String[] args) {
    try {
      DataHandler handler1 = new DataHandler();

      Window window = new Window(handler1);
      window.run();
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
}


