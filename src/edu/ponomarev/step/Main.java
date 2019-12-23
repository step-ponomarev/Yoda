package edu.ponomarev.step;

import edu.ponomarev.step.manager.TaskHandler;
import edu.ponomarev.step.task.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Main {
  public static void main(String [] args) {
    TaskHandler handler = new TaskHandler();
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    Integer command;
    try {
      while (true) {
        printOptions();
        command = Integer.parseInt(reader.readLine());

        if (command == 1) {
          System.out.println("Задачи на сегодня:");
          handler.show();
        } else if (command == 2) {
          handler.show();
          System.out.print("Новая задача: ");
          handler.addTask(new Task(reader.readLine()));
        } else if (command == 3) {
          handler.show();
          handler.removeTask(Integer.parseInt(reader.readLine()));
        } else {
          System.out.println("Invalid option.");
        }
      }
    } catch (IOException e) {
      System.err.println(e.getMessage());
    } catch (ArrayIndexOutOfBoundsException e) {
      System.err.println(e.getMessage());
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


