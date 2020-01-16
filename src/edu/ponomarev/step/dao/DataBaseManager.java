package edu.ponomarev.step.dao;

import edu.ponomarev.step.MVC.model.worker.TaskWorker;
import edu.ponomarev.step.MVC.model.worker.taskWorker.online.JDBCWorker;
import edu.ponomarev.step.MVC.model.worker.taskWorker.offile.Serializator;

import jdk.jfr.Description;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.sql.Connection;
import java.sql.DriverManager;

@Component
public class DataBaseManager {
  @Value("${url}")
  private String url;

  @Value("${sql_user}")
  private String user;

  @Value("${password}")
  private String password;

  private boolean ONLINE;
  private Connection connection;

  @Description("Sets worker automatically. Depends of connection status. ")
  public TaskWorker getOnlineWorker() {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      this.connection = DriverManager.getConnection(url, user, password);
      this.ONLINE = true;

      return (new JDBCWorker(connection));
    } catch (Exception e) {
      System.err.println(e.getMessage());
      ONLINE = false;
      return (new Serializator());
    }
  }

  @Description("Creates certain offline worker if you need work with data offline.")
  public TaskWorker getOfflineWorker() {
    this.ONLINE = false;
    return (new Serializator());
  }

  public boolean isONLINE() {
    return this.ONLINE;
  }
}
