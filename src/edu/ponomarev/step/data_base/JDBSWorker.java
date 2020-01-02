package edu.ponomarev.step.data_base;

import edu.ponomarev.step.manager.TaskHandler;
import edu.ponomarev.step.task.Task;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class JDBSWorker implements DBWorker {
  private Connection connection;
  private Statement statement;;

  public JDBSWorker(Connection connection) throws SQLException {
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

  public List<Task> selectTask(TaskHandler.BoxType type) throws SQLException, ParseException {
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

    ArrayList<Task> list = new ArrayList<Task>();
    DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD");
    while (rs.next()) {
      list.add(new Task(
          rs.getString("statement"),
          rs.getDate("date_of_creation"))
      );
    }

    rs.close();

    return list;
  }
}