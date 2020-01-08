package edu.ponomarev.step.manager;

import edu.ponomarev.step.data.DataWorker;
import edu.ponomarev.step.data.data_base.JDBSWorker;
import edu.ponomarev.step.data.offile.Serializator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseManager {
  private final String userName = "root";
  private final String password = "sql12345";
  private final String connectionUrl = "jdbc:mysql://localhost:3306/mysql?useUnicode=true&serverTimezone=Europe/Moscow";

  private Connection connection;
  //private DataWorker worker;

  public DataWorker getWorker() {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      connection = DriverManager.getConnection(connectionUrl, userName, password);

      return (new JDBSWorker(connection));
    } catch (Exception e) {
      System.err.println(e.getMessage());

      return (new Serializator());
    }
  }
}
