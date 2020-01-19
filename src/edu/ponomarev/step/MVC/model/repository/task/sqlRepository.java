package edu.ponomarev.step.MVC.model.repository.task;

import edu.ponomarev.step.MVC.model.repository.Repository;
import edu.ponomarev.step.component.task.InformatedTask;
import edu.ponomarev.step.component.task.Task;
import edu.ponomarev.step.component.taskContainer.termContainer.ContainerVariable;
import edu.ponomarev.step.component.taskContainer.termContainer.TermTaskContainer;
import edu.ponomarev.step.component.taskContainer.termContainer.ContainerVariable.ContainerType;
import edu.ponomarev.step.system.TimeManager;

import java.sql.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class sqlRepository implements Repository {
  private Connection connection;

  public sqlRepository(Connection connection) {
    this.connection = connection;
  }

  @Override
  public void add(InformatedTask task) {
    final String insertRequest = "INSERT INTO task_box (time_of_creation, time_of_last_change, statement, type) " +
        "VALUES (?, ?, ?, ?);";
    String boxType = ContainerVariable.getBoxName(task.getContainerType());

    try {
      PreparedStatement statement = connection.prepareStatement(insertRequest);

      statement.setObject(1, task.getTimeOfCreation());
      statement.setObject(2, task.getTimeOfLastChange());
      statement.setString(3, task.getStatement());
      statement.setString(4, boxType);

      statement.execute();
      statement.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void add(TermTaskContainer container) {
    final String selectRequest = "SELECT * FROM task_box where type = ?";
    final String insertRequest = "INSERT INTO task_box (time_of_creation, time_of_last_change, statement, type) " +
        "VALUES (?, ?, ?, ?);";

    try {
      PreparedStatement statement = connection.prepareStatement(selectRequest);

      String boxType = ContainerVariable.getBoxName(container.getContainerType());

      statement.setString(1, boxType);

      ArrayList<Task> BDlist = new ArrayList();
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        BDlist.add(new Task(
            resultSet.getString("statement"),
            TimeManager.convertToLocalTimeZone(resultSet.getObject("time_of_creation", LocalDateTime.class)),
            TimeManager.convertToLocalTimeZone(resultSet.getObject("time_of_last_change", LocalDateTime.class))
        ));
      }
      resultSet.close();

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
        update(updateContainer);
      }

      statement.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void remove(InformatedTask tasks) {
    final String removeRequest = "DELETE FROM task_box WHERE time_of_creation = ?";

    try {
      PreparedStatement statement = connection.prepareStatement(removeRequest);

      statement.setObject(1, tasks.getTimeOfCreation());

      statement.execute();

      statement.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void remove(Queue<InformatedTask> tasks) {
    if (tasks.isEmpty()) {
      return;
    }

    final String removeRequest = "DELETE FROM task_box WHERE time_of_creation = ?";

    try {
      PreparedStatement statement = connection.prepareStatement(removeRequest);

      for (Task task : tasks) {
        statement.setObject(1, task.getTimeOfCreation());
        statement.execute();
      }

      statement.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void update(InformatedTask informatedTask) {
    final String updateRequest = "UPDATE task_box SET statement = ?, time_of_last_change = ? WHERE time_of_creation " +
        "= ?";

    try {
      PreparedStatement statement = connection.prepareStatement(updateRequest);

      statement.setString(1, informatedTask.getStatement());
      statement.setObject(2, informatedTask.getTimeOfLastChange());
      statement.setObject(3, informatedTask.getTimeOfCreation());

      statement.execute();

      statement.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void update(TermTaskContainer container) {
    final String updateRequest = "UPDATE task_box SET statement = ?, time_of_last_change = ? WHERE time_of_creation " +
        "= ?";

    try {
      PreparedStatement statement = connection.prepareStatement(updateRequest);


      for (Task updatedTask : container.getList()) {
        statement.setString(1, updatedTask.getStatement());
        statement.setObject(2, updatedTask.getTimeOfLastChange());
        statement.setObject(3, updatedTask.getTimeOfCreation());
        statement.execute();
      }

      statement.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public List<Task> getList(ContainerType containerType) {
    final String sqlRequest = "SELECT * FROM task_box where type = ?";

    ArrayList<Task> tasks = new ArrayList<>();

    try {
      PreparedStatement statement = connection.prepareStatement(sqlRequest);

      String boxType = ContainerVariable.getBoxName(containerType);

      statement.setString(1, boxType);

      ResultSet rs = statement.executeQuery();
      while (rs.next()) {
        tasks.add(new Task(
            rs.getString("statement"),
            TimeManager.convertToLocalTimeZone(rs.getObject("time_of_creation", LocalDateTime.class)),
            TimeManager.convertToLocalTimeZone(rs.getObject("time_of_last_change", LocalDateTime.class))
        ));
      }
      rs.close();
      statement.close();
    } catch (Exception e) {
      e.printStackTrace();
    }


    return tasks;
  }
}
