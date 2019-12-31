package edu.ponomarev.step;

import edu.ponomarev.step.graphics.Main.Window;
import edu.ponomarev.step.manager.DBWorker;
import edu.ponomarev.step.manager.GraphicsWorker;
import edu.ponomarev.step.manager.TaskHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
  public static void main(String [] args) {
    TaskHandler handler = new TaskHandler();
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    Integer command;
    try {
      TaskHandler handler1 = new TaskHandler();
      Window window = new Window(handler1);

      Thread taskHandleThread = new Thread(new DBWorker(handler));
      Thread graphicsThread = new Thread(new GraphicsWorker(window));

      graphicsThread.run();
      taskHandleThread.run();
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public static void printOptions() {
    System.out.println("1) Список задач ");
    System.out.println("2) Добавить задачи ");
    System.out.println("3) Удалить задачи ");
  }
}


