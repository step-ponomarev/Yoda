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
  public void push(DataHandler.BoxType type, Task task) throws SQLException {
    String boxType;
    switch (type) {
      case DAY:
        boxType = "TODAY";
        break;

      case WEEK:
        boxType = "WEEK";
        break;

      case LATE:
        boxType = "LATE";
        break;

      default:
        boxType = "INBOX";
        break;
    }

    final String selectRequest = "SELECT * FROM box where type = ?";
    PreparedStatement statement = connection.prepareStatement(selectRequest);
    statement.setString(1, boxType);

    int id = 1;
    ResultSet resultSet = statement.executeQuery();
    if (resultSet.last()) {
      id = resultSet.getInt("id") + 1;
    }
    resultSet.close();
    task.setId(id);

    final String insertRequest = "INSERT INTO box (id, date_of_creation, statement, type) VALUES  (?, ?, ?, ?);";
    statement = connection.prepareStatement(insertRequest);

    String taskStatement = task.getStatement();
    DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD");
    String date = dateFormat.format(task.getDate());

    statement.setInt(1, task.getId());
    statement.setString(2, date);
    statement.setString(3, taskStatement);
    statement.setString(4, boxType);

    statement.execute();
    statement.close();
  }

  @Override
  public List pullAll(DataHandler.BoxType type) throws SQLException, ParseException {
    final String sqlRequest = "SELECT * FROM box where type = ?";

    PreparedStatement statement = connection.prepareStatement(sqlRequest);

    String boxType;
    switch (type) {
      case DAY:
        boxType = "TODAY";
        break;

      case WEEK:
        boxType = "WEEK";
        break;

      case LATE:
        boxType = "LATE";
        break;

      default:
        boxType = "INBOX";
        break;
    }
    statement.setString(1, boxType);

    ArrayList<Task> list = new ArrayList<Task>();

    ResultSet rs = statement.executeQuery();
    while (rs.next()) {
      list.add(new Task(
          rs.getString("statement"),
          rs.getDate("date_of_creation"),
          rs.getInt("id"))
      );
    }
    rs.close();
    statement.close();

    return list;
  }

  @Override
  public void pushAll(List<Task> list, DataHandler.BoxType type) throws Exception {
    String sqlRequest = "SELECT * FROM box where type = ?";

    PreparedStatement statement = connection.prepareStatement(sqlRequest);

    String boxType;
    switch (type) {
      case DAY:
        boxType = "TODAY";
        break;

      case WEEK:
        boxType = "WEEK";
        break;

      case LATE:
        boxType = "LATE";
        break;

      default:
        boxType = "INBOX";
        break;
    }
    statement.setString(1, boxType);

    ArrayList<Task> BDlist = new ArrayList<Task>();

    ResultSet rs = statement.executeQuery();
    while (rs.next()) {
      BDlist.add(new Task(
          rs.getString("statement"),
          rs.getDate("date_of_creation"),
          rs.getInt("id"))
      );
    }
    rs.close();

    sqlRequest = "INSERT INTO box (date_of_creation, statement, type) VALUES  (?, ?, ?);";
    statement = connection.prepareStatement(sqlRequest);

    statement.setString(3, boxType);

    DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD");
    for (Task task : list) {
      if (!BDlist.contains(task)) {
        statement.setString(1, dateFormat.format(task.getDate()));
        statement.setString(2, task.getStatement());
        statement.execute();
      }
    }

    statement.close();
  }
}
