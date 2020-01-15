package edu.ponomarev.step.dao;

import edu.ponomarev.step.MVC.model.worker.TaskWorker;
import edu.ponomarev.step.MVC.model.worker.taskWorker.online.JDBCWorker;
import edu.ponomarev.step.MVC.model.worker.taskWorker.offile.Serializator;

import jdk.jfr.Description;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DataBaseManager {
  private boolean ONLINE;

  private Connection connection;

  @Description("Sets worker automatically. Depends of connection status. ")
  public TaskWorker getOnlineWorker() {
    ApplicationContext context = new ClassPathXmlApplicationContext("classpath:edu/ponomarev/step/dao/daoContext.xml");

    try {
      Class.forName("com.mysql.jdbc.Driver");
      this.connection = DriverManager.getConnection(context.getBean("daoUrl", String.class),
          context.getBean("daoProperties", Properties.class));
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
