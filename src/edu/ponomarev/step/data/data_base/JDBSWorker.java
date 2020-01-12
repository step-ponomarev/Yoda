package edu.ponomarev.step.data.data_base;

import edu.ponomarev.step.data.DataWorker;
import edu.ponomarev.step.manager.DataHandler;
import edu.ponomarev.step.component.task.Task;

import java.sql.*;
import java.text.ParseException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JDBSWorker implements DataWorker {
  private Connection connection;

  public JDBSWorker(Connection connection) throws SQLException {
    this.connection = connection;
  }

  @Override
  public void push(Task task, DataHandler.BoxType type) throws SQLException {
    String boxType = new String();
    for (DataHandler.BoxRequestWrap item : DataHandler.BOX_VARIABLES) {
      if (type.equals(item.type)) {
        boxType = item.boxName;
        break;
      }
    }

    final String insertRequest = "INSERT INTO task_box (time_of_creation, time_of_last_change, statement, type) " +
        "VALUES (?, ?, ?, ?);";
    PreparedStatement statement = connection.prepareStatement(insertRequest);

    statement.setObject(1, task.getTimeOfCreation());
    statement.setObject(2, task.getTimeOfLastChange());
    statement.setString(3, task.getStatement());
    statement.setString(4, boxType);

    statement.execute();
    statement.close();
  }

  @Override
  public List getAll(DataHandler.BoxType type) throws SQLException, ParseException {
    final String sqlRequest = "SELECT * FROM task_box where type = ?";

    PreparedStatement statement = connection.prepareStatement(sqlRequest);

    String boxType = new String();
    for (DataHandler.BoxRequestWrap item : DataHandler.BOX_VARIABLES) {
      if (type.equals(item.type)) {
        boxType = item.boxName;
        break;
      }
    }
    statement.setString(1, boxType);

    ArrayList<Task> list = new ArrayList<Task>();

    ResultSet rs = statement.executeQuery();
    while (rs.next()) {
      list.add(new Task(
          rs.getString("statement"),
          rs.getObject("date_of_creation", LocalDateTime.class),
          rs.getObject("time_of_last_change", LocalDateTime.class)
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

    String boxType = new String();
    for (DataHandler.BoxRequestWrap item : DataHandler.BOX_VARIABLES) {
      if (type.equals(item.type)) {
        boxType = item.boxName;
        break;
      }
    }

    statement.setString(1, boxType);

    ArrayList<Task> BDlist = new ArrayList<Task>();
    ResultSet resultSet = statement.executeQuery();
    while (resultSet.next()) {
      BDlist.add(new Task(
          resultSet.getString("statement"),
          resultSet.getObject("time_of_creation", LocalDateTime.class),
          resultSet.getObject("time_of_last_change", LocalDateTime.class)
      ));
    }
    resultSet.close();

    final String insertRequest = "INSERT INTO task_box (time_of_creation, time_of_last_change, statement, type) " +
        "VALUES (?, ?, ?, ?);";

    statement = connection.prepareStatement(insertRequest);

    ArrayList<Task> updateList = new ArrayList<Task>();

    statement.setString(4, boxType);
    for (Task clientTask : list) {
      if (!BDlist.contains(clientTask)) {
        statement.setObject(1, clientTask.getTimeOfCreation());
        statement.setObject(2, clientTask.getTimeOfLastChange());
        statement.setString(3, clientTask.getStatement());
        statement.execute();
      } else {
        Task taskOnBD = BDlist.get(BDlist.indexOf(clientTask));
        final boolean TASK_WAS_CHANGED_OFFLINE = clientTask.getTimeOfLastChange().isAfter(taskOnBD.getTimeOfLastChange());

        if (TASK_WAS_CHANGED_OFFLINE) {
          updateList.add(clientTask);
        }
      }
    }

    updateAll(updateList, type);

    statement.close();
  }

  private void updateAll(List<Task> updateList, DataHandler.BoxType type) throws Exception {
    final String updateRequest = "UPDATE task_box SET statement = ?, time_of_last_change = ? where time_of_creation " +
        "= ?";

    PreparedStatement statement = connection.prepareStatement(updateRequest);

    /*String boxType = new String();
    for (DataHandler.BoxRequestWrap item : DataHandler.BOX_VARIABLES) {
      if (type.equals(item.type)) {
        boxType = item.boxName;
        break;
      }
    }

    statement.setString(4, boxType);*/

    for (Task updatedTask : updateList) {
      statement.setString(1, updatedTask.getStatement());
      statement.setObject(2, updatedTask.getTimeOfLastChange());
      statement.setObject(3, updatedTask.getTimeOfCreation());
    }

    statement.close();
  }
}
