package edu.ponomarev.step.MVC.model;

import edu.ponomarev.step.MVC.model.repository.task.TaskSerializator;
import edu.ponomarev.step.MVC.model.repository.task.sqlTaskRepository;
import edu.ponomarev.step.component.task.InformatedTask;
import edu.ponomarev.step.MVC.model.repository.RepositoryFactory;
import edu.ponomarev.step.component.taskContainer.termContainer.TermTaskContainer;
import edu.ponomarev.step.component.taskContainer.termContainer.ContainerVariable.ContainerType;

import java.util.*;

import static edu.ponomarev.step.Main.context;

public class TaskWorker {
  private RepositoryFactory repositoryFactory;

  private TaskSerializator taskSerializator;
  private sqlTaskRepository taskSqlRepository;

  private HashMap<ContainerType, TermTaskContainer> taskContainers;

  private Queue<InformatedTask> removeQueueOfTasks;

  public TaskWorker() {
    taskContainers = new HashMap<>() {{
      put(ContainerType.INBOX, new TermTaskContainer(ContainerType.INBOX));
      put(ContainerType.DAY, new TermTaskContainer(ContainerType.DAY));
      put(ContainerType.WEEK, new TermTaskContainer(ContainerType.WEEK));
      put(ContainerType.LATE, new TermTaskContainer(ContainerType.LATE));
    }};

    removeQueueOfTasks = new LinkedList<>();

    repositoryFactory = context.getBean("repositoryFactory", RepositoryFactory.class);

    taskSerializator = (TaskSerializator) repositoryFactory.getTaskSerializator();
    taskSqlRepository = (sqlTaskRepository) repositoryFactory.getSqlTaskRepository();
  }

  public void addTask(InformatedTask informatedTask) {
    taskContainers.get(informatedTask.getContainerType()).add(informatedTask);

    taskSerializator.add(informatedTask);

    if (repositoryFactory.isOnline()) {
      taskSqlRepository.add(informatedTask);
    } else {
      System.err.println("No connection");
      return;
      //TODO пытаемся восстановить коннекшен
    }
  }

  public void removeTask(InformatedTask informatedTask) {
    removeQueueOfTasks.add(informatedTask);
    taskContainers.get(informatedTask.getContainerType()).remove(informatedTask);

    taskSerializator.remove(informatedTask);

    if (repositoryFactory.isOnline()) {
      taskSqlRepository.remove(informatedTask);
    } else {
      System.err.println("No connection");
      return;
      // TODO Пытаемся восстановить коннекшен
    }
  }

  public void cleanQueueToDelete() {
    taskSerializator.remove(removeQueueOfTasks);

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
    for (var box : taskContainers.entrySet()) {
      TermTaskContainer container = box.getValue();

      taskSerializator.add(container);
      if (repositoryFactory.isOnline()) {
        taskSqlRepository.add(container);
      } else {
        System.err.println("No connection");
        return;
        // TODO Востсанавливаем коннекшен в другом потоке....
      }
    }
  }

  public void updateTask(InformatedTask task) {
    taskSerializator.update(task);

    if (repositoryFactory.isOnline()) {
      taskSqlRepository.update(task);
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

  public HashMap<ContainerType, TermTaskContainer> getContainer() { return taskContainers; }

  public TermTaskContainer getContainer(ContainerType containerType) { return taskContainers.get(containerType); }
}
