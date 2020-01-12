package edu.ponomarev.step.manager;

import edu.ponomarev.step.data.DataWorker;
import edu.ponomarev.step.data.data_base.JDBSWorker;
import edu.ponomarev.step.data.offile.Serializator;
import jdk.jfr.Description;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataBaseManager {
  private final String userName = "root";
  private final String password = "sql12345";
  private final String connectionUrl = "jdbc:mysql://localhost:3306/mysql?useUnicode=true&serverTimezone=Europe/Moscow";

  private boolean ONLINE;

  private Connection connection;
  //private DataWorker worker;

  @Description("Sets worker automatically. Depends of connection status. ")
  public DataWorker getWorker() {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      this.connection = DriverManager.getConnection(connectionUrl, userName, password);
      this.ONLINE = true;

      return (new JDBSWorker(connection));
    } catch (Exception e) {
      System.err.println(e.getMessage());
      ONLINE = false;
      return (new Serializator());
    }
  }

  @Description("Creates certain offline worker if you need work with data offline.")
  public DataWorker getOfflineWorker() {
    this.ONLINE = false;
    return (new Serializator());
  }

  public boolean isONLINE() {
    return this.ONLINE;
  }
}
