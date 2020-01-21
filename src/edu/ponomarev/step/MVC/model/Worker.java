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
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class Worker {
  @Autowired
  @Qualifier("repositoryFactory")
  private RepositoryFactory repositoryFactory;

  private TaskSerializator taskSerializator;
  private TaskSqlRepository taskSqlRepository;

  private HashMap<BoxType, List<Task>> boxes;
  private ArrayList<Project> projectList;

  private Queue<Task> removeQueueOfTasks;

  public Worker() {
    boxes = new HashMap<>() {{
      put(BoxType.INBOX, new ArrayList<>());
      put(BoxType.DAY, new ArrayList<>());
      put(BoxType.WEEK, new ArrayList<>());
      put(BoxType.LATE, new ArrayList<>());
    }};

    projectList = new ArrayList<>();
    removeQueueOfTasks = new LinkedList<>();
  }

  @PostConstruct
  public void postConstruct() {
    taskSerializator = (TaskSerializator) repositoryFactory.getRepository(RepositoryType.TASK_OFFLINE);
    taskSqlRepository = (TaskSqlRepository) repositoryFactory.getRepository(RepositoryType.TASK_SQL);
  }

  public void addTask(Task task, TaskRelations taskRelations) {
    final var boxType = taskRelations.getBoxOwnerType();
    final var taskSpecification = new TaskSpecification(taskRelations);

    boxes.get(boxType).add(task);

    taskSerializator.add(task, taskSpecification);

    if (repositoryFactory.isOnline()) {
      taskSqlRepository.add(task, taskSpecification);
    } else {
      System.err.println("No connection");
      return;
      //TODO пытаемся восстановить коннекшен
    }
  }

  public void removeTask(Task task, TaskRelations taskRelations) {
    final var boxType = taskRelations.getBoxOwnerType();
    final var taskSpecification = new TaskSpecification(taskRelations);

    removeQueueOfTasks.add(task);
    boxes.get(boxType).remove(task);

    taskSerializator.remove(task, taskSpecification);

    if (repositoryFactory.isOnline()) {
      taskSqlRepository.remove(task, taskSpecification);
    } else {
      System.err.println("No connection");
      return;
      // TODO Пытаемся восстановить коннекшен
    }
  }

  public void utilizeQueueToRemove() {
    if (repositoryFactory.isOnline()) {
      taskSqlRepository.remove(removeQueueOfTasks);
      removeQueueOfTasks.clear();
    } else {
      System.err.println("No connection");
      return;
      // TODO пытаесчя восстановить коннекшен
    }
  }

  public void pushAll() {
    for (var box : boxes.entrySet()) {
      final var taskReletions = new TaskRelations(box.getKey());
      final var boxTypeSpecification = new TaskSpecification(taskReletions);
      List<Task> tasks = box.getValue();

      taskSerializator.add(tasks, boxTypeSpecification);
      if (repositoryFactory.isOnline()) {
        taskSqlRepository.add(tasks, boxTypeSpecification);
      } else {
        System.err.println("No connection");
        return;
        // TODO Востсанавливаем коннекшен в другом потоке....
      }
    }
  }

  public void updateTask(Task task, TaskRelations taskRelations) {
    final var taskSpecification = new TaskSpecification(taskRelations);

    taskSerializator.update(task, taskSpecification);

    if (repositoryFactory.isOnline()) {
      taskSqlRepository.update(task, taskSpecification);
    } else {
      System.err.println("No connection");
      return;
      // TODO Востсанавливаем коннекшен в другом потоке....
    }
  }

  // TODO Добавить синхронизацию задач
  public void synchronizeTasks() {
    return;
  }

  public HashMap<BoxType, List<Task>> getTaskBoxes() { return boxes; }

  public List<Task> getTaskBox(BoxType boxType) { return boxes.get(boxType); }

  public ArrayList<Project> getProjectList() {
    return projectList;
  }
}
