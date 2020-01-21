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
  public void add(Task task, Specification specification) throws Exception {
    final String insertRequest = "INSERT INTO task_box (id, type, statement, time_of_creation, time_of_last_change) VALUES (?, ?, ?, ?, ?);";

    final var taskRelations = (TaskRelations) specification.getSpecification();

    final var boxType = taskRelations.getBoxOwnerType().toString();

    PreparedStatement statement = connection.prepareStatement(insertRequest);

    statement.setObject(1, task.getUUID());
    statement.setString(2, boxType);
    statement.setString(3, task.getStatement());
    statement.setObject(4, task.getTimeOfCreation());
    statement.setObject(5, task.getTimeOfLastChange());

    statement.execute();
    statement.close();
  }

  @Override
  public void add(List<Task> tasks, Specification specification) throws Exception {
    final String selectRequest = "SELECT * FROM task_box where type = ?";
    final String insertRequest = "INSERT INTO task_box (id, type, statement, time_of_creation, time_of_last_change)" +
        " " +
        "VALUES (?, ?, ?, ?, ?);";

    final var taskRelations = (TaskRelations) specification.getSpecification();

    final var boxType = taskRelations.getBoxOwnerType().toString();
    PreparedStatement statement = connection.prepareStatement(selectRequest);

    statement.setString(1, boxType);

    List<Task> sqlTasks = new ArrayList();

    ResultSet resultSet = statement.executeQuery();
    while (resultSet.next()) {
      sqlTasks.add(new Task(
          resultSet.getString("id"),
          resultSet.getString("statement"),
          TimeManager.convertToLocalTimeZone(resultSet.getObject("time_of_creation", LocalDateTime.class)),
          TimeManager.convertToLocalTimeZone(resultSet.getObject("time_of_last_change", LocalDateTime.class))
      ));
    }
    resultSet.close();

    statement = connection.prepareStatement(insertRequest);

    List<Task> tasksToUpdate = new ArrayList<Task>();
    for (Task clientTask : tasks) {
      final boolean TASK_NOT_EXISTS_IN_DATABASE = !sqlTasks.contains(clientTask);
      if (TASK_NOT_EXISTS_IN_DATABASE) {
        statement.setString(1, clientTask.getUUID());
        statement.setString(2, boxType);
        statement.setString(3, clientTask.getStatement());
        statement.setObject(4, clientTask.getTimeOfCreation());
        statement.setObject(5, clientTask.getTimeOfLastChange());
        statement.execute();
      } else {
        Task sqlTask = sqlTasks.get(sqlTasks.indexOf(clientTask));
        final boolean TASK_WAS_CHANGED_OFFLINE =
            clientTask.getTimeOfLastChange().isAfter(sqlTask.getTimeOfLastChange());

        if (TASK_WAS_CHANGED_OFFLINE) {
          tasksToUpdate.add(clientTask);
        }
      }
    }

    update(tasksToUpdate, specification);

    statement.close();
  }

  @Override
  public void remove(Task task, Specification specification) throws Exception {
    final String removeRequest = "DELETE FROM task_box WHERE ( time_of_creation = ? ) AND ( id = ? )";

    PreparedStatement statement = connection.prepareStatement(removeRequest);

    statement.setObject(1, task.getTimeOfCreation());
    statement.setString(2, task.getUUID());
    statement.execute();

    statement.close();
  }

  @Override
  public void remove(List<Task> tasks) throws Exception {
    if (tasks.isEmpty()) {
      return;
    }

    final String removeRequest = "DELETE FROM task_box WHERE ( id = ? ) AND ( time_of_creation = ? )";

    PreparedStatement statement = connection.prepareStatement(removeRequest);

    for (Task task : tasks) {
      statement.setObject(1, task.getTimeOfCreation());
      statement.setString(2, task.getUUID());
      statement.execute();
    }

    statement.close();
  }

  @Override
  public void update(Task updatedTask, Specification specification) throws Exception {
    final String updateRequest =
        "UPDATE task_box SET statement = ?, time_of_last_change = ? WHERE ( time_of_creation = ? ) AND ( id = ? ) AND" +
            " ( time_of_last_change <= ? )";

    PreparedStatement statement = connection.prepareStatement(updateRequest);

    statement.setString(1, updatedTask.getStatement());
    statement.setObject(2, updatedTask.getTimeOfLastChange());
    statement.setObject(3, updatedTask.getTimeOfCreation());
    statement.setString(4, updatedTask.getUUID());
    statement.setObject(5, updatedTask.getTimeOfLastChange());

    statement.execute();

    statement.close();
  }

  @Override
  public void update(List<Task> tasks, Specification specification) throws Exception {
    for (Task task : tasks) {
      update(task, specification);
    }
  }

  @Override
  public List<Task> getList(Specification specification) throws Exception {
    final String sqlRequest = "SELECT * FROM task_box where type = ?";

    final var taskRelations = (TaskRelations) specification.getSpecification();

    final var boxType = taskRelations.getBoxOwnerType().toString();

    List<Task> tasks = new ArrayList<>();

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


    return tasks;
  }
}
