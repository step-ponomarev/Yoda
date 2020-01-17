package edu.ponomarev.step.MVC.model.repository;

import edu.ponomarev.step.MVC.model.repository.task.TaskRepository;
import edu.ponomarev.step.MVC.model.repository.task.TaskSerializator;
import edu.ponomarev.step.MVC.model.repository.task.sqlTaskRepository;
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

  public TaskRepository getSqlTaskRepository() {
    try {
      if (!connection.isClosed()) {
        return (new sqlTaskRepository(connection));
      } else {
        setConnection();

        return (new sqlTaskRepository(connection));
      }
    } catch (Exception e) {
      System.err.println(e.getMessage());
      return null;
    }
  }

  public TaskRepository getTaskSerializator() {
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

  @PostConstruct
  private void setConnection() {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      connection = DriverManager.getConnection(url, user, password);
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
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
