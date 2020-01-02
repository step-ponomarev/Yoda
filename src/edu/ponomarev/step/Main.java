package edu.ponomarev.step;

import edu.ponomarev.step.graphics.Main.Window;
import edu.ponomarev.step.manager.DataBaseManager;
import edu.ponomarev.step.manager.TaskHandler;

public class Main {
  public static void main(String[] args) {
    try {
      DataBaseManager dataBaseManager = new DataBaseManager();
      TaskHandler handler1 = new TaskHandler(dataBaseManager.getWorker());

      Window window = new Window(handler1, dataBaseManager);
      window.run();
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
}


