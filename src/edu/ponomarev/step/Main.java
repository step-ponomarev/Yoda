package edu.ponomarev.step;

import edu.ponomarev.step.graphics.Main.Window;
import edu.ponomarev.step.data_base.DBWorker;
import edu.ponomarev.step.manager.TaskHandler;
import edu.ponomarev.step.data_base.JDBSWorker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
  private static final String userName = "root";
  private static final String password = "sql12345";
  private static final String connectionUrl = "jdbc:mysql://localhost:3306/mysql?useUnicode=true&serverTimezone=UTC";

  public static void main(String[] args) {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      Connection connection = DriverManager.getConnection(connectionUrl, userName, password);

      DBWorker dbWorker = new JDBSWorker(connection);

      TaskHandler handler1 = new TaskHandler(dbWorker);
      Window window = new Window(handler1);

      window.run();
    } catch (SQLException e) {
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


