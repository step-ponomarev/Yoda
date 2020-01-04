package edu.ponomarev.step.data.data_base;

import edu.ponomarev.step.data.DataWorker;
import edu.ponomarev.step.manager.DataHandler;
import edu.ponomarev.step.component.task.Task;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class JDBSWorker implements DataWorker {
  private Connection connection;

  public JDBSWorker(Connection connection) throws SQLException {
    this.connection = connection;
  }

  @Override
  public void put(DataHandler.BoxType type, Task task) throws SQLException {
    String sqlRequest = "INSERT INTO box (date_of_creation, statement, type)";
    String taskStatement = task.getStatement();

    DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD");
    String date = dateFormat.format(task.getDate());

    sqlRequest += " VALUES ('"  + date + "', '" + taskStatement + "',";

    switch (type) {
      case DAY:
        sqlRequest += " 'TODAY');";
        break;

      case WEEK:
        sqlRequest += " 'WEEK');";
        break;

      case LATE:
        sqlRequest += " 'LATE');";
        break;

      default:
        sqlRequest += " 'INBOX');";
        break;
    }

    PreparedStatement statement = connection.prepareStatement(sqlRequest);

    statement.execute();
  }

  @Override
  public void putAll(DataHandler.BoxType type, List<Task> task) throws Exception {
    //TODO Пушить все таски, которых нет в БД
    System.out.println("Это не работает еще!!!");
    return;
  }

  @Override
  public List pull(DataHandler.BoxType type) throws SQLException, ParseException {
    String sqlRequest = "SELECT * FROM box where type =  ";

    switch (type) {
      case DAY:
        sqlRequest += "'TODAY'";
        break;

      case WEEK:
        sqlRequest += "'WEEK'";
        break;

      case LATE:
        sqlRequest += "'LATE'";
        break;

      default:
        sqlRequest += "'INBOX'";
        break;
    }

    PreparedStatement statement = connection.prepareStatement(sqlRequest);
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
