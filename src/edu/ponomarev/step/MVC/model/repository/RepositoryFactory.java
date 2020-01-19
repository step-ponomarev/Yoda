package edu.ponomarev.step.MVC.model.repository;

import edu.ponomarev.step.MVC.model.repository.task.TaskSerializator;
import edu.ponomarev.step.MVC.model.repository.task.sqlRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.sql.Connection;
import java.sql.DriverManager;

@Component
@Scope("singleton")
public class RepositoryFactory {
  @Value("${url}")
  private String url;

  @Value("${sql_user}")
  private String user;

  @Value("${password}")
  private String password;

  private Connection connection;

  @PostConstruct
  private void setConnection() {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      connection = DriverManager.getConnection(url, user, password);
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public Repository getSqlTaskRepository() {
    try {
      if (!connection.isClosed()) {
        return (new sqlRepository(connection));
      } else {
        setConnection();

        return (new sqlRepository(connection));
      }
    } catch (Exception e) {
      System.err.println(e.getMessage());
      return null;
    }
  }

  public Repository getTaskSerializator() {
    return (new TaskSerializator());
  }

  public boolean isOnline() {
    boolean status = false;
    try {
      status = !connection.isClosed();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return status;
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
