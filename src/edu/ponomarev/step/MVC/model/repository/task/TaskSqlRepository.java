package edu.ponomarev.step.MVC.model.repository.task;

import edu.ponomarev.step.MVC.model.repository.Repository;
import edu.ponomarev.step.MVC.model.repository.Specification;
import edu.ponomarev.step.component.task.Task;
import edu.ponomarev.step.component.task.TaskRelations;
import edu.ponomarev.step.system.TimeManager;

import java.sql.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class TaskSqlRepository implements Repository<Task> {
  private Connection connection;

  public TaskSqlRepository(Connection connection) {
    this.connection = connection;
  }

  @Override
  public void add(Task task, Specification specification) {
    final String insertRequest = "INSERT INTO task_box (id, type, statement, time_of_creation, time_of_last_change) VALUES (?, ?, ?, ?, ?);";

    final var taskRelations = (TaskRelations) specification.getSpecification();

    final var boxType = taskRelations.getBoxOwnerType().toString();

    try {
      PreparedStatement statement = connection.prepareStatement(insertRequest);

      statement.setObject(1, task.getUUID());
      statement.setString(2, boxType);
      statement.setString(3, task.getStatement());
      statement.setObject(4, task.getTimeOfCreation());
      statement.setObject(5, task.getTimeOfLastChange());

      statement.execute();
      statement.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void add(List<Task> tasks, Specification specification) {
    final String selectRequest = "SELECT * FROM task_box where type = ?";
    final String insertRequest = "INSERT INTO task_box (id, type, statement, time_of_creation, time_of_last_change)" +
        " " +
        "VALUES (?, ?, ?, ?, ?);";

    final var taskRelations = (TaskRelations) specification.getSpecification();

    final var boxType = taskRelations.getBoxOwnerType().toString();
    try {
      PreparedStatement statement = connection.prepareStatement(selectRequest);

      statement.setString(1, boxType);

      ArrayList<Task> BDlist = new ArrayList();
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        BDlist.add(new Task(
            resultSet.getString("id"),
            resultSet.getString("statement"),
            TimeManager.convertToLocalTimeZone(resultSet.getObject("time_of_creation", LocalDateTime.class)),
            TimeManager.convertToLocalTimeZone(resultSet.getObject("time_of_last_change", LocalDateTime.class))
        ));
      }
      resultSet.close();

      statement = connection.prepareStatement(insertRequest);

      List<Task> updateContainer = new ArrayList<Task>();

      statement.setString(2, boxType);
      for (Task clientTask : tasks) {
        if (!BDlist.contains(clientTask)) {
          statement.setString(1, clientTask.getUUID());
          statement.setString(3, clientTask.getStatement());
          statement.setObject(4, clientTask.getTimeOfCreation());
          statement.setObject(5, clientTask.getTimeOfLastChange());
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

      update(updateContainer, specification);

      statement.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void remove(Task task, Specification specification) {
    final String removeRequest = "DELETE FROM task_box WHERE ( time_of_creation = ? ) AND ( id = ? )";

    try {
      PreparedStatement statement = connection.prepareStatement(removeRequest);

      statement.setObject(1, task.getTimeOfCreation());
      statement.setString(2, task.getUUID());
      statement.execute();

      statement.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void remove(Queue<Task> tasks) {
    if (tasks.isEmpty()) {
      return;
    }

    final String removeRequest = "DELETE FROM task_box WHERE ( time_of_creation = ? ) AND ( id = ? )";

    try {
      PreparedStatement statement = connection.prepareStatement(removeRequest);

      for (Task task : tasks) {
        statement.setObject(1, task.getTimeOfCreation());
        statement.setString(2, task.getUUID());
        statement.execute();
      }

      statement.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void update(Task updatedTask, Specification specification) {
    final String updateRequest =
        "UPDATE task_box SET statement = ?, time_of_last_change = ? WHERE ( time_of_creation = ? ) AND ( id = ? )";

    try {
      PreparedStatement statement = connection.prepareStatement(updateRequest);

      statement.setString(1, updatedTask.getStatement());
      statement.setObject(2, updatedTask.getTimeOfLastChange());
      statement.setObject(3, updatedTask.getTimeOfCreation());
      statement.setString(4, updatedTask.getUUID());

      statement.execute();

      statement.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void update(List<Task> tasks, Specification specification) {
    for (Task task : tasks) {
      update(task, specification);
    }
  }

  @Override
  public List<Task> getList(Specification specification) {
    final String sqlRequest = "SELECT * FROM task_box where type = ?";

    final var taskRelations = (TaskRelations) specification.getSpecification();

    final var boxType = taskRelations.getBoxOwnerType().toString();

    ArrayList<Task> tasks = new ArrayList<>();

    try {
      PreparedStatement statement = connection.prepareStatement(sqlRequest);

      statement.setString(1, boxType);

      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        tasks.add(new Task(
            resultSet.getString("id"),
            resultSet.getString("statement"),
            TimeManager.convertToLocalTimeZone(resultSet.getObject("time_of_creation", LocalDateTime.class)),
            TimeManager.convertToLocalTimeZone(resultSet.getObject("time_of_last_change", LocalDateTime.class))
        ));
      }
      resultSet.close();
      statement.close();
    } catch (Exception e) {
      e.printStackTrace();
    }


    return tasks;
  }
}
