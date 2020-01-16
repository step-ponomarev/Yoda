package edu.ponomarev.step.MVC.model.worker.taskWorker.online;

import edu.ponomarev.step.MVC.model.worker.TaskWorker;
import edu.ponomarev.step.MVC.model.DataHandler;
import edu.ponomarev.step.component.task.InformatedTask;
import edu.ponomarev.step.component.task.Task;
import edu.ponomarev.step.component.taskContainer.TermTaskContainer;
import edu.ponomarev.step.system.TimeManager;

import java.sql.*;
import java.text.ParseException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Queue;

public class JDBCWorker implements TaskWorker {
  private Connection connection;

  public JDBCWorker(Connection connection) throws SQLException {
    this.connection = connection;
  }

  @Override
  public void push(InformatedTask task) throws SQLException {
    String boxType = DataHandler.getBoxName(task.getContainerType());

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
  public void push(TermTaskContainer container) throws Exception {
    final String selectRequest = "SELECT * FROM task_box where type = ?";

    PreparedStatement statement = connection.prepareStatement(selectRequest);

    String boxType = DataHandler.getBoxName(container.getContainerType());

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

    TermTaskContainer updateContainer = new TermTaskContainer(container.getContainerType());

    statement.setString(4, boxType);
    for (Task clientTask : container.getList()) {
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
          updateContainer.add(clientTask);
        }
      }
    }

    if (!updateContainer.isEmpty()) {
      updateAll(updateContainer);
    }

    statement.close();
  }

  @Override
  public void remove(InformatedTask tasks) throws SQLException{
    final String removeRequest = "DELETE FROM task_box WHERE time_of_creation = ?";

    PreparedStatement statement = connection.prepareStatement(removeRequest);

    statement.setObject(1, tasks.getTimeOfCreation());

    statement.execute();

    statement.close();
  }

  @Override
  public void remove(Queue<InformatedTask> tasks) throws SQLException {
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
  public TermTaskContainer getContainer(TermTaskContainer.ContainerType containerTask) throws SQLException, ParseException {
    final String sqlRequest = "SELECT * FROM task_box where type = ?";

    PreparedStatement statement = connection.prepareStatement(sqlRequest);

    String boxType = DataHandler.getBoxName(containerTask);

    statement.setString(1, boxType);

    TermTaskContainer container = new TermTaskContainer(containerTask);

    ResultSet rs = statement.executeQuery();
    while (rs.next()) {
      container.add(new Task(
          rs.getString("statement"),
          TimeManager.convertToLocalTimeZone(rs.getObject("time_of_creation", LocalDateTime.class)),
          TimeManager.convertToLocalTimeZone(rs.getObject("time_of_last_change", LocalDateTime.class))
      ));
    }
    rs.close();
    statement.close();

    return container;
  }

  private void updateAll(TermTaskContainer container) throws Exception {
    final String updateRequest = "UPDATE task_box SET statement = ?, time_of_last_change = ? WHERE time_of_creation " +
        "= ?";

    PreparedStatement statement = connection.prepareStatement(updateRequest);


    for (Task updatedTask : container.getList()) {
      statement.setString(1, updatedTask.getStatement());
      statement.setObject(2, updatedTask.getTimeOfLastChange());
      statement.setObject(3, updatedTask.getTimeOfCreation());
      statement.execute();
    }

    statement.close();
  }
}
