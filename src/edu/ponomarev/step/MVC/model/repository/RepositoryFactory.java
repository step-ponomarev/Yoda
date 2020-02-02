package edu.ponomarev.step.MVC.model.repository;

import edu.ponomarev.step.MVC.model.repository.project.ProjectSerializator;
import edu.ponomarev.step.MVC.model.repository.project.ProjectSqlRepository;
import edu.ponomarev.step.MVC.model.repository.task.TaskSerializator;
import edu.ponomarev.step.MVC.model.repository.task.TaskSqlRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.sql.Connection;
import java.sql.DriverManager;

@Service
public class RepositoryFactory {
  public enum RepositoryType {
    TASK_OFFLINE,
    TASK_SQL,
    PROJECT_OFFLINE,
    PROJECT_SQL;
  }

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

  public Repository getRepository(RepositoryType repositoryType) {
    switch (repositoryType) {
      case TASK_OFFLINE:
        return ( new TaskSerializator() );

      case TASK_SQL:
        return ( new TaskSqlRepository(connection) );

      case PROJECT_SQL:
        return ( new ProjectSqlRepository(connection) );

      case PROJECT_OFFLINE:
        return ( new ProjectSerializator() );

      default:
        //TODO Сделать строгую ошибку
        throw new RuntimeException("Invalid Type");
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
