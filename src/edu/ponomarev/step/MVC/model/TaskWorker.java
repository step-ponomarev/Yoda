package edu.ponomarev.step.MVC.model;

import edu.ponomarev.step.MVC.model.repository.task.TaskSerializator;
import edu.ponomarev.step.MVC.model.repository.task.sqlTaskRepository;
import edu.ponomarev.step.component.task.InformatedTask;
import edu.ponomarev.step.MVC.model.repository.RepositoryFactory;
import edu.ponomarev.step.component.taskContainer.TermTaskContainer;

import java.util.*;

import static edu.ponomarev.step.Main.context;

public class TaskWorker {
  private RepositoryFactory repositoryFactory;

  private TaskSerializator taskSerializator;
  private sqlTaskRepository taskSqlRepository;

  private HashMap<TermTaskContainer.ContainerType, TermTaskContainer> taskContainers;

  private Queue<InformatedTask> removeQueueOfTasks;

  public TaskWorker() {
    taskContainers = new HashMap<>() {{
      put(TermTaskContainer.ContainerType.INBOX, new TermTaskContainer(TermTaskContainer.ContainerType.INBOX));
      put(TermTaskContainer.ContainerType.DAY, new TermTaskContainer(TermTaskContainer.ContainerType.DAY));
      put(TermTaskContainer.ContainerType.WEEK, new TermTaskContainer(TermTaskContainer.ContainerType.WEEK));
      put(TermTaskContainer.ContainerType.LATE, new TermTaskContainer(TermTaskContainer.ContainerType.LATE));
    }};

    removeQueueOfTasks = new LinkedList<>();

    repositoryFactory = context.getBean("repositoryFactory", RepositoryFactory.class);

    taskSerializator = (TaskSerializator) repositoryFactory.getTaskSerializator();
    taskSqlRepository = (sqlTaskRepository) repositoryFactory.getSqlTaskRepository();
  }

  public static String getBoxName(TermTaskContainer.ContainerType containerType) {
    for (TermTaskContainer.ContainerVariable item : TermTaskContainer.BOX_VARIABLES) {
      if (containerType.equals(item.type)) {
        return item.name;
      }
    }

    return null;
  }

  public void addTask(InformatedTask informatedTask) {
    taskContainers.get(informatedTask.getContainerType()).add(informatedTask);

    taskSerializator.add(informatedTask);

    if (repositoryFactory.isOnline()) {
      taskSqlRepository.add(informatedTask);
    } else {
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
      return;
      // TODO Пытаемся восстановить коннекшен
    }
  }

  public void cleanQueueToDelete() {
    taskSerializator.remove(removeQueueOfTasks);

    if (repositoryFactory.isOnline()) {
      taskSqlRepository.remove(removeQueueOfTasks);
    } else {
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
        return;
        // TODO Востсанавливаем коннекшен в другом потоке....
      }
    }
  }

  // TODO Реализовать обновлеие задач/задачи

  public HashMap<TermTaskContainer.ContainerType, TermTaskContainer> getContainer() {
    return taskContainers;
  }

  public TermTaskContainer getContainer(TermTaskContainer.ContainerType containerType) {
    return taskContainers.get(containerType);
  }
}
