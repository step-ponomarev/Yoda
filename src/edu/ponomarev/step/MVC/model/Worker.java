package edu.ponomarev.step.MVC.model;

import edu.ponomarev.step.MVC.model.repository.task.TaskSerializator;
import edu.ponomarev.step.MVC.model.repository.task.BoxTypeSpecification;
import edu.ponomarev.step.MVC.model.repository.task.TaskSqlRepository;
import edu.ponomarev.step.component.project.Project;
import edu.ponomarev.step.component.task.InformatedTask;
import edu.ponomarev.step.MVC.model.repository.RepositoryFactory;
import edu.ponomarev.step.component.task.Task;
import edu.ponomarev.step.component.BoxType;
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
    taskSerializator = (TaskSerializator) repositoryFactory.getTaskSerializator();
    taskSqlRepository = (TaskSqlRepository) repositoryFactory.getSqlTaskRepository();
  }

  public void addTask(InformatedTask informatedTask) {
    final var boxType = informatedTask.getBoxType();
    final var boxTypeSpecification = new BoxTypeSpecification(boxType);

    boxes.get(boxType).add(informatedTask);

    taskSerializator.add(informatedTask, boxTypeSpecification);

    if (repositoryFactory.isOnline()) {
      taskSqlRepository.add(informatedTask, boxTypeSpecification);
    } else {
      System.err.println("No connection");
      return;
      //TODO пытаемся восстановить коннекшен
    }
  }

  public void removeTask(InformatedTask informatedTask) {
    final var boxType = informatedTask.getBoxType();
    final var boxTypeSpecification = new BoxTypeSpecification(boxType);

    removeQueueOfTasks.add(informatedTask);
    boxes.get(boxType).remove(informatedTask);

    taskSerializator.remove(informatedTask, boxTypeSpecification);

    if (repositoryFactory.isOnline()) {
      taskSqlRepository.remove(informatedTask, boxTypeSpecification);
    } else {
      System.err.println("No connection");
      return;
      // TODO Пытаемся восстановить коннекшен
    }
  }

  public void utilizeQueueToRemove() {
    if (repositoryFactory.isOnline()) {
      taskSqlRepository.remove(removeQueueOfTasks);
    } else {
      System.err.println("No connection");
      return;
      // TODO пытаесчя восстановить коннекшен
    }

    removeQueueOfTasks.clear();
  }

  public void pushAll() {
    for (var box : boxes.entrySet()) {
      final var boxTypeSpecification = new BoxTypeSpecification(box.getKey());
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

  public void updateTask(InformatedTask task) {
    final var boxType = task.getBoxType();
    taskSerializator.update(task, new BoxTypeSpecification(boxType));

    if (repositoryFactory.isOnline()) {
      taskSqlRepository.update(task, new BoxTypeSpecification(boxType));
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
