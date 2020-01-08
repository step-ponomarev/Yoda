package edu.ponomarev.step.data.data_base;

import edu.ponomarev.step.data.DataWorker;
import edu.ponomarev.step.manager.DataHandler;
import edu.ponomarev.step.component.task.Task;

import java.sql.*;
import java.text.ParseException;

import java.time.LocalDate;

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

    final String insertRequest = "INSERT INTO task_box (date_of_creation, statement, type) VALUES  (?, ?, ?);";
    PreparedStatement statement = connection.prepareStatement(insertRequest);

    statement.setObject(1, task.getDateOfCreation());
    statement.setString(2, task.getStatement());
    statement.setString(3, boxType);

    statement.execute();
    statement.close();
  }

  @Override
  public List pullAll(DataHandler.BoxType type) throws SQLException, ParseException {
    final String sqlRequest = "SELECT * FROM task_box where type = ?";

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
          rs.getObject("date_of_creation", LocalDate.class)
      ));
    }
    rs.close();
    statement.close();

    return list;
  }

  @Override
  public void pushAll(List<Task> list, DataHandler.BoxType type) throws Exception {
    final String selectRequest = "SELECT * FROM task_box where type = ?";

    PreparedStatement statement = connection.prepareStatement(selectRequest);

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
          rs.getObject("date_of_creation", LocalDate.class)
      ));
    }
    rs.close();

    final String insertRequest = "INSERT INTO task_box (date_of_creation, statement, type) VALUES  (?, ?, ?);";
    statement = connection.prepareStatement(insertRequest);

    statement.setString(3, boxType);

    for (Task task : list) {
      if (!BDlist.contains(task)) {
        statement.setObject(1, task.getDateOfCreation());
        statement.setString(2, task.getStatement());
        statement.execute();
      }
    }

    statement.close();
  }
}
