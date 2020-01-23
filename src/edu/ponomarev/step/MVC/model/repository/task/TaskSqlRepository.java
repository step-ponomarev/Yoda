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

public class TaskSqlRepository implements Repository<Task> {
  private static final String SELECT = "SELECT * FROM task_box where type = ?";
  private static final String INSERT = "INSERT INTO task_box (id, projectID, type, statement, time_of_creation, " +
      "time_of_last_change) VALUES (?, ?, ?, ?, ?, ?)";
  private static final String DELETE = "DELETE FROM task_box WHERE ( id = ? ) AND ( time_of_creation = ? )";
  private static final String UPDATE = "UPDATE task_box SET statement = ?, time_of_last_change = ? WHERE ( id = ? ) AND ( time_of_creation = ? ) AND ( time_of_last_change <= ? )";

  private Connection connection;

  public TaskSqlRepository(Connection connection) {
    this.connection = connection;
  }

  @Override
  public void add(Task task, Specification specification) throws Exception {
    final var taskRelations = (TaskRelations) specification.getSpecification();
    final String projectID = taskRelations.getProjectID();
    final var boxType = taskRelations.getBoxType().toString();

    PreparedStatement statement = connection.prepareStatement(INSERT);

    statement.setObject(1, task.getUUID());
    statement.setString(2, projectID);
    statement.setString(3, boxType);
    statement.setString(4, task.getStatement());
    statement.setObject(5, task.getTimeOfCreation());
    statement.setObject(6, task.getTimeOfLastChange());

    statement.execute();

    statement.close();
  }

  @Override
  public void add(List<Task> tasks, Specification specification) throws Exception {
    final var taskRelations = (TaskRelations) specification.getSpecification();
    final String projectID = taskRelations.getProjectID();
    final var boxType = taskRelations.getBoxType().toString();
    PreparedStatement statement = connection.prepareStatement(SELECT);

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

    statement = connection.prepareStatement(INSERT);
// TODO решить, как записывать проект.
    List<Task> tasksToUpdate = new ArrayList<>();
    for (Task clientTask : tasks) {
      final boolean TASK_NOT_EXISTS_IN_DATABASE = !sqlTasks.contains(clientTask);
      if (TASK_NOT_EXISTS_IN_DATABASE) {
        statement.setString(1, clientTask.getUUID());
        statement.setString(2, projectID);
        statement.setString(3, boxType);
        statement.setString(4, clientTask.getStatement());
        statement.setObject(5, clientTask.getTimeOfCreation());
        statement.setObject(6, clientTask.getTimeOfLastChange());

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
    PreparedStatement statement = connection.prepareStatement(DELETE);

    statement.setString(1, task.getUUID());
    statement.setObject(2, task.getTimeOfCreation());

    statement.execute();

    statement.close();
  }

  @Override
  public void remove(List<Task> tasks) throws Exception {
    if (tasks.isEmpty()) {
      return;
    }

    PreparedStatement statement = connection.prepareStatement(DELETE);

    for (Task task : tasks) {
      statement.setString(1, task.getUUID());
      statement.setObject(2, task.getTimeOfCreation());

      statement.execute();
    }

    statement.close();
  }

  @Override
  public void update(Task updatedTask, Specification specification) throws Exception {
    PreparedStatement statement = connection.prepareStatement(UPDATE);

    statement.setString(1, updatedTask.getStatement());
    statement.setObject(2, updatedTask.getTimeOfLastChange());
    statement.setString(3, updatedTask.getUUID());
    statement.setObject(4, updatedTask.getTimeOfCreation());
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
    final var taskRelations = (TaskRelations) specification.getSpecification();

    final var boxType = taskRelations.getBoxType().toString();

    List<Task> tasks = new ArrayList<>();

    PreparedStatement statement = connection.prepareStatement(SELECT);

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
