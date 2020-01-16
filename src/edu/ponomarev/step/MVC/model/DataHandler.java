package edu.ponomarev.step.MVC.model;

import edu.ponomarev.step.component.task.InformatedTask;
import edu.ponomarev.step.dao.DataBaseManager;
import edu.ponomarev.step.MVC.model.worker.TaskWorker;
import edu.ponomarev.step.component.task.Task;
import edu.ponomarev.step.component.taskContainer.TermTaskContainer;

import java.util.*;

import static edu.ponomarev.step.Main.context;

public class DataHandler {
  private DataBaseManager dataBaseManager;
  private TaskWorker taskWorker;

  private HashMap<TermTaskContainer.ContainerType, TermTaskContainer> taskContainers;

  private Queue<InformatedTask> tasksToRemoveQueue;

  public DataHandler() {
    taskContainers = new HashMap<>() {{
      put(TermTaskContainer.ContainerType.INBOX, new TermTaskContainer(TermTaskContainer.ContainerType.INBOX));
      put(TermTaskContainer.ContainerType.DAY, new TermTaskContainer(TermTaskContainer.ContainerType.DAY));
      put(TermTaskContainer.ContainerType.WEEK, new TermTaskContainer(TermTaskContainer.ContainerType.WEEK));
      put(TermTaskContainer.ContainerType.LATE, new TermTaskContainer(TermTaskContainer.ContainerType.LATE));
    }};

    this.tasksToRemoveQueue = new LinkedList<>();
    this.dataBaseManager = context.getBean("dataBaseManager", DataBaseManager.class);
    this.taskWorker = dataBaseManager.getOfflineWorker();
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

    try {
      //Offline pushing;
      this.taskWorker = dataBaseManager.getOfflineWorker();
      this.taskWorker.push(informatedTask);


      //Try to push to DB
      this.taskWorker = dataBaseManager.getOnlineWorker();
      if (dataBaseManager.isONLINE()) {
        taskWorker.push(informatedTask);
      }
    } catch (Exception exception) {
      System.err.println(exception.getMessage());
    }
  }

  public void removeTask(InformatedTask informatedTask) {
    tasksToRemoveQueue.add(informatedTask);

    taskContainers.get(informatedTask.getContainerType()).remove(informatedTask);
  }

  public void removeAll() {
    try {
      taskWorker.remove(tasksToRemoveQueue);
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public void pushAll() {
    try {
      for (Map.Entry<TermTaskContainer.ContainerType, TermTaskContainer> box : taskContainers.entrySet()) {
        this.taskWorker.push(box.getValue());
      }
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public void pullAll() {
    try {
      for (Map.Entry<TermTaskContainer.ContainerType, TermTaskContainer> box : taskContainers.entrySet()) {
        taskContainers.replace(box.getKey(), (TermTaskContainer) taskWorker.getContainer(box.getKey()));
      }
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public void updateAll() {
    try {
      for (Map.Entry<TermTaskContainer.ContainerType, TermTaskContainer> box : taskContainers.entrySet()) {
        TermTaskContainer.ContainerType containerTask = box.getKey();
        synchTasks((TermTaskContainer) taskWorker.getContainer(containerTask));
      }
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public HashMap<TermTaskContainer.ContainerType, TermTaskContainer> getContainer() { return taskContainers; }

  public TermTaskContainer getContainer(TermTaskContainer.ContainerType containerType) {
    return taskContainers.get(containerType);
  }


  public DataBaseManager getDataBaseManager() { return dataBaseManager; }

  public TaskWorker getTaskWorker() {
    return taskWorker;
  }

  public HashMap<TermTaskContainer.ContainerType, TermTaskContainer> getTaskContainers() {
    return taskContainers;
  }

  public void setOnlineWorker() { this.taskWorker = this.dataBaseManager.getOnlineWorker(); }

  public void setOfflineWorker() { this.taskWorker = this.dataBaseManager.getOfflineWorker(); }

  private void synchTasks(TermTaskContainer updatedTasks) {
    List<Task> tasksToUpdate = this.taskContainers.get(updatedTasks.getContainerType()).getList();

    for (Task currentTask : updatedTasks.getList()) {
      if (tasksToUpdate.contains(currentTask)) {
        Task taskToUpdate = tasksToUpdate.get(tasksToUpdate.indexOf(currentTask));
        if (currentTask.getTimeOfLastChange().isAfter(taskToUpdate.getTimeOfLastChange())) {
          taskToUpdate.setStatement(currentTask.getStatement());
          taskToUpdate.setTTimeOfLastChange(currentTask.getTimeOfLastChange());
        }
      }
    }

    tasksToUpdate.removeAll(updatedTasks.getList());
    tasksToUpdate.addAll(updatedTasks.getList());
  }
}
