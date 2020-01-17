package edu.ponomarev.step.db;

import edu.ponomarev.step.MVC.model.dao.TaskDAO;
import edu.ponomarev.step.MVC.model.dao.taskSaver.online.JDBC_DAO;
import edu.ponomarev.step.MVC.model.dao.taskSaver.offile.Serializator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


import javax.annotation.PreDestroy;
import java.sql.Connection;
import java.sql.DriverManager;

@Component
@Scope("singleton")
public class DataBaseManager {
  @Value("${url}")
  private String url;

  @Value("${sql_user}")
  private String user;

  @Value("${password}")
  private String password;

  private boolean ONLINE;
  private Connection connection;

  public TaskDAO getOnlineWorker() {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      this.connection = DriverManager.getConnection(url, user, password);
      this.ONLINE = true;

      return (new JDBC_DAO(connection));
    } catch (Exception e) {
      System.err.println(e.getMessage());
      ONLINE = false;
      return (new Serializator());
    }
  }

  public TaskDAO getOfflineWorker() {
    this.ONLINE = false;
    return (new Serializator());
  }

  public boolean isONLINE() {
    return this.ONLINE;
  }

  @PreDestroy
  private void preDestroy() {
    try {
      connection.close();
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
}
