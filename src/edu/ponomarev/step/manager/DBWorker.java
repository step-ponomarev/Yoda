package edu.ponomarev.step.manager;

import edu.ponomarev.step.task.Task;

import java.sql.*;

public class DBWorker {
  private Connection connection;
  private Statement statement;

  public DBWorker(Connection connection) throws SQLException {
    this.connection = connection;
    this.statement = connection.createStatement();
  }

  public void insertTask(TaskHandler.BoxType type, Task task) throws SQLException {
    String sqlRequest;
    String taskStatement = task.getStatement();
    String date = task.getDate().toString();

    switch (type) {
      case DAY:
        sqlRequest = "INSERT INTO box_today (statement) VALUES (\'" + taskStatement + "\');";
        statement.executeUpdate(sqlRequest);
        return;
      case WEEK:
        sqlRequest = "INSERT INTO box_week (statement) VALUES ('" + taskStatement + "');";
        statement.executeUpdate(sqlRequest);
        return;
      case LATE:
        sqlRequest = "INSERT INTO box_late (statement) VALUES ('" + taskStatement + "');";
        statement.executeUpdate(sqlRequest);
        return;
      default:
        sqlRequest = "INSERT INTO box_inbox (statement) VALUES ('" + taskStatement + "');";
        statement.executeUpdate(sqlRequest);
        return;
    }
  }


}
