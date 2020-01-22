package edu.ponomarev.step.MVC.model;

import edu.ponomarev.step.MVC.model.repository.task.TaskSerializator;
import edu.ponomarev.step.MVC.model.repository.task.TaskSpecification;
import edu.ponomarev.step.MVC.model.repository.task.TaskSqlRepository;
import edu.ponomarev.step.component.project.Project;
import edu.ponomarev.step.MVC.model.repository.RepositoryFactory;
import edu.ponomarev.step.MVC.model.repository.RepositoryFactory.RepositoryType;
import edu.ponomarev.step.component.task.Task;
import edu.ponomarev.step.component.BoxType;
import edu.ponomarev.step.component.task.TaskRelations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import java.sql.SQLException;
import java.util.*;

public class Worker {
  @Autowired
  @Qualifier("repositoryFactory")
  private RepositoryFactory repositoryFactory;

  private TaskSerializator taskSerializator;
  private TaskSqlRepository taskSqlRepository;

  private HashMap<BoxType, List<Task>> boxes;
  private List<Project> projects;

  private List<Task> tasksToRemove;
  private HashMap<Task, TaskRelations> tasksToAdd;
  private HashMap<Task, TaskRelations> tasksToUpdate;

  public Worker() {
    boxes = new HashMap<>() {{
      put(BoxType.INBOX, new ArrayList<>());
      put(BoxType.DAY, new ArrayList<>());
      put(BoxType.WEEK, new ArrayList<>());
      put(BoxType.LATE, new ArrayList<>());
    }};

    projects = new ArrayList<>();

    tasksToRemove = new ArrayList<>();
    tasksToAdd = new HashMap<>();
    tasksToUpdate = new HashMap<>();
  }

  @PostConstruct
  public void postConstruct() {
    taskSerializator = (TaskSerializator) repositoryFactory.getRepository(RepositoryType.TASK_OFFLINE);
    taskSqlRepository = (TaskSqlRepository) repositoryFactory.getRepository(RepositoryType.TASK_SQL);
  }

  public void addTask(Task task, TaskRelations taskRelations) {
    final var boxType = taskRelations.getBoxType();
    final var taskSpecification = new TaskSpecification(taskRelations);

    try {
      boxes.get(boxType).add(task);

      taskSerializator.add(task, taskSpecification);

      taskSqlRepository.add(task, taskSpecification);
    } catch (SQLException e) {
      // TODO восстанавливаем коннекшен, задачу в список на добавление сохраняем???
      tasksToAdd.put(task, taskRelations);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void removeTask(Task task, TaskRelations taskRelations) {
    final var boxType = taskRelations.getBoxType();
    final var taskSpecification = new TaskSpecification(taskRelations);

    try {
      boxes.get(boxType).remove(task);

      taskSerializator.remove(task, taskSpecification);

      taskSqlRepository.remove(task, taskSpecification);
    } catch (SQLException e) {
      // TODO восстанавливаем коннекшен, задачу в список на удаление сохраняем???
      tasksToRemove.add(task);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void updateTask(Task task, TaskRelations taskRelations) {
    final var taskSpecification = new TaskSpecification(taskRelations);

    try {
      taskSerializator.update(task, taskSpecification);
      taskSqlRepository.update(task, taskSpecification);
    } catch (SQLException e) {
      e.printStackTrace();
      //TODO Добавить в лист на обновление.
      tasksToUpdate.put(task, taskRelations);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //TODO Написать тесты для методов очистки очередей

  public void clearAddList() {
    try {
      for (var taskEntry : tasksToAdd.entrySet()) {
        final var task = taskEntry.getKey();
        final var taskSpecification = new TaskSpecification(taskEntry.getValue());
        taskSqlRepository.remove(task, taskSpecification);
      }
      tasksToAdd.clear();
    } catch (SQLException e) {
      e.printStackTrace();
      // TODO Ничо не делать ((((?
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void clearRemoveList() {
    try {
      taskSqlRepository.remove(tasksToRemove);
      tasksToRemove.clear();
    } catch (SQLException e) {
      e.printStackTrace();
      // TODO Ничо не делать ((((?
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void clearUpdateList() {
    try {
      for (var taskEntry : tasksToUpdate.entrySet()) {
        final var task = taskEntry.getKey();
        final var taskSpecification = new TaskSpecification(taskEntry.getValue());
        taskSqlRepository.update(task, taskSpecification);
      }
      tasksToUpdate.clear();
    } catch (SQLException e) {
      e.printStackTrace();
      // TODO Ничо не делать ((((?
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void pushAll() {
    // TODO возможно этот метод не нужен.
    try {
      // Add tasks offline
      for (var box : boxes.entrySet()) {
        final var taskReletions = new TaskRelations(box.getKey());
        final var boxTypeSpecification = new TaskSpecification(taskReletions);
        List<Task> tasks = box.getValue();

        taskSerializator.add(tasks, boxTypeSpecification);
      }

      for (var box : boxes.entrySet()) {
        final var taskReletions = new TaskRelations(box.getKey());
        final var boxTypeSpecification = new TaskSpecification(taskReletions);
        List<Task> tasks = box.getValue();

        taskSqlRepository.add(tasks, boxTypeSpecification);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void addProject(Project project) {
    projects.add(project);
  }

  public void removeProject(Project project) {
    projects.remove(project);
  }

  // TODO Добавить синхронизацию задач
  public void synchronizeTasks() { return; }

  public List<Task> getTaskBox(BoxType boxType) { return boxes.get(boxType); }

  public List<Project> getProjects() {
    return projects;
  }
}
