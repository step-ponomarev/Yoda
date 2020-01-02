package edu.ponomarev.step.manager;

import edu.ponomarev.step.Main;
import edu.ponomarev.step.task.Task;
import edu.ponomarev.step.task.TaskContainer;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
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

  public void selectTask(TaskHandler.BoxType type, TaskContainer taskList) throws SQLException, ParseException {
    String sqlRequest = "SELECT date_of_creation, statement FROM";

    switch (type) {
      case DAY:
        sqlRequest += " box_today";
        break;

      case WEEK:
        sqlRequest += " box_week";
        break;

      case LATE:
        sqlRequest += " box_late";
        break;

      default:
        sqlRequest += " box_inbox";
        break;
    }

    ResultSet rs = statement.executeQuery(sqlRequest);

    DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD");
    Date date;
    while (rs.next()) {
      date = rs.getDate("date_of_creation");

      taskList.add(new Task(rs.getString("statement"), date));
    }

    rs.close();
  }
}
