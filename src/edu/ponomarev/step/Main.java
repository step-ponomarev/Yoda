package edu.ponomarev.step;

import edu.ponomarev.step.data.offile.Serializator;
import edu.ponomarev.step.graphics.Main.Window;
import edu.ponomarev.step.data.DataWorker;
import edu.ponomarev.step.manager.TaskHandler;
import edu.ponomarev.step.data.data_base.JDBSWorker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
  private static final String userName = "root";
  private static final String password = "sql12345";
  private static final String connectionUrl = "jdbc:mysql://localhost:3306/mysql?useUnicode=true&serverTimezone=UTC";

  public static void main(String[] args) {
    DataWorker dataWorker;

    try {
      Class.forName("com.mysql.jdbc.Driver");
      Connection connection = DriverManager.getConnection(connectionUrl, userName, password);

      dataWorker = new JDBSWorker(connection);
    } catch (SQLException e) {
      System.err.println(e.getMessage());
      dataWorker = new Serializator();
    } catch (Exception e) {
      System.err.println(e.getMessage());
      dataWorker = new Serializator();
    }

    TaskHandler handler1 = new TaskHandler(dataWorker);
    Window window = new Window(handler1);
    window.run();
  }
}


