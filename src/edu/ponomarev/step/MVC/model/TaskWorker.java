package edu.ponomarev.step.MVC.model;

import edu.ponomarev.step.component.task.InformatedTask;
import edu.ponomarev.step.db.DataBaseManager;
import edu.ponomarev.step.MVC.model.dao.TaskDAO;
import edu.ponomarev.step.component.task.Task;
import edu.ponomarev.step.component.taskContainer.TermTaskContainer;

import java.util.*;

import static edu.ponomarev.step.Main.context;

public class TaskWorker {
  private DataBaseManager dataBaseManager;
  private TaskDAO taskDAO;

  private HashMap<TermTaskContainer.ContainerType, TermTaskContainer> taskContainers;

  private Queue<InformatedTask> removeQueueOfTasks;

  public TaskWorker() {
    taskContainers = new HashMap<>() {{
      put(TermTaskContainer.ContainerType.INBOX, new TermTaskContainer(TermTaskContainer.ContainerType.INBOX));
      put(TermTaskContainer.ContainerType.DAY, new TermTaskContainer(TermTaskContainer.ContainerType.DAY));
      put(TermTaskContainer.ContainerType.WEEK, new TermTaskContainer(TermTaskContainer.ContainerType.WEEK));
      put(TermTaskContainer.ContainerType.LATE, new TermTaskContainer(TermTaskContainer.ContainerType.LATE));
    }};

    this.removeQueueOfTasks = new LinkedList<>();
    this.dataBaseManager = context.getBean("dataBaseManager", DataBaseManager.class);
    this.taskDAO = dataBaseManager.getOfflineWorker();
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

    //Offline pushing;
    this.taskDAO = dataBaseManager.getOfflineWorker();
    this.taskDAO.push(informatedTask);


    //Try to push to DB
    this.taskDAO = dataBaseManager.getOnlineWorker();
    if (dataBaseManager.isONLINE()) {
      taskDAO.push(informatedTask);
    }
  }

  public void removeTask(InformatedTask informatedTask) {
    removeQueueOfTasks.add(informatedTask);

    taskContainers.get(informatedTask.getContainerType()).remove(informatedTask);
  }

  public void removeAll() {
    taskDAO.remove(removeQueueOfTasks);
  }

  public void pushAll() {
    for (Map.Entry<TermTaskContainer.ContainerType, TermTaskContainer> box : taskContainers.entrySet()) {
      this.taskDAO.push(box.getValue());
    }
  }

  public void pullAll() {
    for (Map.Entry<TermTaskContainer.ContainerType, TermTaskContainer> box : taskContainers.entrySet()) {
      taskContainers.replace(box.getKey(), (TermTaskContainer) taskDAO.getContainer(box.getKey()));
    }
  }

  public void updateAll() {
    for (Map.Entry<TermTaskContainer.ContainerType, TermTaskContainer> box : taskContainers.entrySet()) {
      TermTaskContainer.ContainerType containerTask = box.getKey();
      synchTasks((TermTaskContainer) taskDAO.getContainer(containerTask));
    }
  }

  public HashMap<TermTaskContainer.ContainerType, TermTaskContainer> getContainer() {
    return taskContainers;
  }

  public TermTaskContainer getContainer(TermTaskContainer.ContainerType containerType) {
    return taskContainers.get(containerType);
  }

  public DataBaseManager getDataBaseManager() {
    return dataBaseManager;
  }

  public TaskDAO getTaskDAO() {
    return taskDAO;
  }


  public void setOnlineWorker() {
    this.taskDAO = this.dataBaseManager.getOnlineWorker();
  }

  public void setOfflineWorker() {
    this.taskDAO = this.dataBaseManager.getOfflineWorker();
  }

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
