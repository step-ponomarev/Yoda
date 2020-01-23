package edu.ponomarev.step.MVC.model.repository.project;

import edu.ponomarev.step.MVC.model.repository.Repository;
import edu.ponomarev.step.MVC.model.repository.Specification;
import edu.ponomarev.step.component.project.Project;
import edu.ponomarev.step.system.TimeManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// TODO Написать тесты для класса

public class ProjectSqlRepository implements Repository<Project> {
  private static final String INSERT = "INSERT INTO project_list (id, name, time_of_creation, time_of_last_change) " +
      "VALUES (?, ?, ?, ?)";
  private static final String SELECT = "SELECT * FROM project_list";
  private static final String DELETE = "DELETE FROM project_list where ( id = ? ) AND ( time_of_creation = ? )";
  private static final String UPDATE = "UPDATE project_list SET name = ?, time_of_last_change = ? WHERE ( id" +
      " = ? ) AND ( time_of_creation = ? ) AND ( time_of_last_change <= ? )";

  private Connection connection;

  public ProjectSqlRepository(Connection connection) {
    this.connection = connection;
  }

  @Override
  public void add(Project project, Specification specification) throws Exception {
    final var preparedStatement = connection.prepareStatement(INSERT);

    preparedStatement.setString(1, project.getUuid());
    preparedStatement.setString(2, project.getName());
    preparedStatement.setObject(3, project.getTimeOfCreation());
    preparedStatement.setObject(4, project.getTimeOfLastChange());

    preparedStatement.execute();

    preparedStatement.close();
  }

  @Override
  public void add(List<Project> projects, Specification specification) throws Exception {
    final var preparedStatement = connection.prepareStatement(INSERT);

    for (var project : projects) {
      preparedStatement.setString(1, project.getUuid());
      preparedStatement.setString(2, project.getName());
      preparedStatement.setObject(3, project.getTimeOfCreation());
      preparedStatement.setObject(4, project.getTimeOfLastChange());

      preparedStatement.execute();
    }

    preparedStatement.close();
  }

  @Override
  public void remove(Project project, Specification specification) throws Exception {
    final var preparedStatement = connection.prepareStatement(DELETE);

    preparedStatement.setString(1, project.getUuid());
    preparedStatement.setObject(2, project.getTimeOfCreation());

    preparedStatement.execute();

    preparedStatement.close();
  }

  @Override
  public void remove(List<Project> projects) throws Exception {
    if (projects.isEmpty()) {
      return;
    }

    final var preparedStatement = connection.prepareStatement(DELETE);

    for (var project : projects) {
      preparedStatement.setString(1, project.getUuid());
      preparedStatement.setObject(2, project.getTimeOfCreation());

      preparedStatement.execute();
    }

    preparedStatement.close();
  }

  @Override
  public void update(Project project, Specification specification) throws Exception {
    final var preparedStatement = connection.prepareStatement(UPDATE);

    preparedStatement.setString(1, project.getName());
    preparedStatement.setObject(2, project.getTimeOfLastChange());
    preparedStatement.setString(3, project.getUuid());
    preparedStatement.setObject(4, project.getTimeOfCreation());
    preparedStatement.setObject(5, project.getTimeOfLastChange());

    preparedStatement.execute();

    preparedStatement.close();
  }

  @Override
  public void update(List<Project> projects, Specification specification) throws Exception {
    final var preparedStatement = connection.prepareStatement(UPDATE);

    for (var project : projects) {
      preparedStatement.setString(1, project.getName());
      preparedStatement.setObject(2, project.getTimeOfLastChange());
      preparedStatement.setString(3, project.getUuid());
      preparedStatement.setObject(4, project.getTimeOfCreation());
      preparedStatement.setObject(5, project.getTimeOfLastChange());

      preparedStatement.execute();
    }

    preparedStatement.close();
  }

  @Override
  public List<Project> getList(Specification specification) throws Exception {
    final var preparedStatement = connection.prepareStatement(SELECT);
    final List<Project> projects = new ArrayList<>();

    ResultSet resultSet = preparedStatement.executeQuery();
    while (resultSet.next()) {
      Project project = new Project(
          resultSet.getString("id"),
          resultSet.getString("name"),
          TimeManager.convertToLocalTimeZone(resultSet.getObject("time_of_creation", LocalDateTime.class)),
          TimeManager.convertToLocalTimeZone(resultSet.getObject("time_of_last_change", LocalDateTime.class))
      );

      projects.add(project);
    }

    resultSet.close();
    preparedStatement.close();

    return projects;
  }
}
