package edu.ponomarev.step.manager;

import edu.ponomarev.step.task.Task;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DBWorker {
  private Connection connection;
  private Statement statement;

  public DBWorker(Connection connection) throws SQLException {
    this.connection = connection;
    this.statement = connection.createStatement();
  }

  public void insertTask(TaskHandler.BoxType type, Task task) throws SQLException {
    String sqlRequest = "INSERT INTO";
    String taskStatement = task.getStatement();

    DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD");
    String date = dateFormat.format(task.getDate());

    switch (type) {
      case DAY:
        sqlRequest += " box_today (date_of_creation, statement)";
        break;

      case WEEK:
        sqlRequest += " box_week (date_of_creation, statement)";
        break;

      case LATE:
        sqlRequest += " box_late (date_of_creation, statement)";
        break;

      default:
        sqlRequest += " box_inbox (date_of_creation, statement)";
        break;
    }

    sqlRequest += " VALUES ('"  + date + "', '" + taskStatement + "');";

    statement.executeUpdate(sqlRequest);
  }


}
