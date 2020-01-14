package edu.ponomarev.step.worker.taskWorker.online;

import edu.ponomarev.step.worker.DataWorker;
import edu.ponomarev.step.manager.DataHandler;
import edu.ponomarev.step.component.task.Task;
import edu.ponomarev.step.system.TimeManager;

import java.sql.*;
import java.text.ParseException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class JDBCWorker implements DataWorker {
  private Connection connection;

  public JDBCWorker(Connection connection) throws SQLException {
    this.connection = connection;
  }

  @Override
  public void push(Task task, DataHandler.BoxType type) throws SQLException {
    String boxType = DataHandler.getBoxName(type);

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
  public void pushAll(List<Task> taskListOnClient, DataHandler.BoxType type) throws Exception {
    final String selectRequest = "SELECT * FROM task_box where type = ?";

    PreparedStatement statement = connection.prepareStatement(selectRequest);

    String boxType = DataHandler.getBoxName(type);

    statement.setString(1, boxType);

    ArrayList<Task> BDlist = new ArrayList<Task>();
    ResultSet resultSet = statement.executeQuery();
    while (resultSet.next()) {
      BDlist.add(new Task(
          resultSet.getString("statement"),
          TimeManager.convertToLocalTimeZone(resultSet.getObject("time_of_creation", LocalDateTime.class)),
          TimeManager.convertToLocalTimeZone(resultSet.getObject("time_of_last_change", LocalDateTime.class))
      ));
    }
    resultSet.close();

    final String insertRequest = "INSERT INTO task_box (time_of_creation, time_of_last_change, statement, type) " +
        "VALUES (?, ?, ?, ?);";

    statement = connection.prepareStatement(insertRequest);

    ArrayList<Task> updateList = new ArrayList<Task>();

    statement.setString(4, boxType);
    for (Task clientTask : taskListOnClient) {
      if (!BDlist.contains(clientTask)) {
        statement.setObject(1, clientTask.getTimeOfCreation());
        statement.setObject(2, clientTask.getTimeOfLastChange());
        statement.setString(3, clientTask.getStatement());
        statement.execute();
      } else {
        Task taskOnBD = BDlist.get(BDlist.indexOf(clientTask)); // The elements equal only by dates_of_creation
        final boolean TASK_WAS_CHANGED_OFFLINE =
            clientTask.getTimeOfLastChange().isAfter(taskOnBD.getTimeOfLastChange());


        if (TASK_WAS_CHANGED_OFFLINE) {
          updateList.add(clientTask);
        }
      }
    }

    if (!updateList.isEmpty()) {
      updateAll(updateList, type);
    }

    statement.close();
  }

  @Override
  public void remove(Task tasks) throws SQLException{
    final String removeRequest = "DELETE FROM task_box WHERE time_of_creation = ?";

    PreparedStatement statement = connection.prepareStatement(removeRequest);

    statement.setObject(1, tasks.getTimeOfCreation());

    statement.execute();

    statement.close();
  }

  @Override
  public void removeAll(Queue<Task> tasks) throws SQLException {
    final String removeRequest = "DELETE FROM task_box WHERE time_of_creation = ?";

    PreparedStatement statement = connection.prepareStatement(removeRequest);

    for (Task task : tasks) {
      statement.setObject(1, task.getTimeOfCreation());
      statement.execute();
    }

    tasks.clear();

    statement.close();
  }

  @Override
  public List getAll(DataHandler.BoxType type) throws SQLException, ParseException {
    final String sqlRequest = "SELECT * FROM task_box where type = ?";

    PreparedStatement statement = connection.prepareStatement(sqlRequest);

    String boxType = DataHandler.getBoxName(type);

    statement.setString(1, boxType);

    ArrayList<Task> list = new ArrayList<Task>();

    ResultSet rs = statement.executeQuery();
    while (rs.next()) {
      list.add(new Task(
          rs.getString("statement"),
          TimeManager.convertToLocalTimeZone(rs.getObject("time_of_creation", LocalDateTime.class)),
          TimeManager.convertToLocalTimeZone(rs.getObject("time_of_last_change", LocalDateTime.class))
      ));
    }
    rs.close();
    statement.close();

    return list;
  }

  private void updateAll(List<Task> updateList, DataHandler.BoxType type) throws Exception {
    final String updateRequest = "UPDATE task_box SET statement = ?, time_of_last_change = ? WHERE time_of_creation " +
        "= ?";

    PreparedStatement statement = connection.prepareStatement(updateRequest);


    for (Task updatedTask : updateList) {
      statement.setString(1, updatedTask.getStatement());
      statement.setObject(2, updatedTask.getTimeOfLastChange());
      statement.setObject(3, updatedTask.getTimeOfCreation());
      statement.execute();
    }

    statement.close();
  }
}
