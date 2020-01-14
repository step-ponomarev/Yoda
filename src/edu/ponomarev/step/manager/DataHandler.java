package edu.ponomarev.step.manager;

import edu.ponomarev.step.dao.DataBaseManager;
import edu.ponomarev.step.worker.DataWorker;
import edu.ponomarev.step.component.task.Task;
import edu.ponomarev.step.component.task.TaskContainer;

import java.util.*;

public class DataHandler {
  public static class BoxVariable {
    public DataHandler.BoxType type;
    public String boxName;

    public BoxVariable(DataHandler.BoxType type, String boxName) {
      this.type = type;
      this.boxName = boxName;
    }

    @Override
    public String toString() {
      return boxName;
    }
  }

  public enum BoxType {
    INBOX,
    DAY,
    WEEK,
    LATE
  }

  final public static BoxVariable[] BOX_VARIABLES = new BoxVariable[] {
      new BoxVariable(DataHandler.BoxType.INBOX, "Входящие"),
      new BoxVariable(DataHandler.BoxType.DAY, "Сегодня"),
      new BoxVariable(DataHandler.BoxType.WEEK, "На неделе"),
      new BoxVariable(DataHandler.BoxType.LATE, "Позже")
  };

  private DataBaseManager DBmanager;
  private DataWorker dataWorker;

  private HashMap<DataHandler.BoxType, TaskContainer> taskBox;

  private Queue<Task> tasksToRemoveQueue;

  public static String getBoxName(BoxType boxType) {
    String boxName =  new String();
    for (BoxVariable item : DataHandler.BOX_VARIABLES) {
      if (boxType.equals(item.type)) {
        boxName = item.boxName;
      }
    }

    return boxName;
  }

  public DataHandler() {
    taskBox = new HashMap<DataHandler.BoxType, TaskContainer>() {{
      put(BoxType.INBOX, new TaskContainer());
      put(BoxType.DAY, new TaskContainer());
      put(BoxType.WEEK, new TaskContainer());
      put(BoxType.LATE, new TaskContainer());
    }};
    this.tasksToRemoveQueue = new LinkedList<Task>();

    this.DBmanager = new DataBaseManager();
    this.dataWorker = DBmanager.getOfflineWorker();
    pullAll();
  }

  public void addTask(BoxType type, Task task) {
    taskBox.get(type).add(task);

    try {
      //Offline pushing;
      this.dataWorker = DBmanager.getOfflineWorker();
      this.dataWorker.push(task, type);


      //Try to push to DB
      this.dataWorker = DBmanager.getOnlineWorker();
      if (DBmanager.isONLINE()) {
        dataWorker.push(task, type);
      }
    } catch (Exception exception) {
      System.err.println(exception.getMessage());
    }
  }

  public void removeTask(Task task) {
    tasksToRemoveQueue.add(task);

    for (Task removingTask : tasksToRemoveQueue) {
      for (Map.Entry<BoxType, TaskContainer> box : taskBox.entrySet()) {
        box.getValue().getList().remove(removingTask);
      }
    }
  }

  public void removeAll() {
    try {
      dataWorker.removeAll(tasksToRemoveQueue);
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public void pushAll() {
    try {
      for (Map.Entry<DataHandler.BoxType, TaskContainer> box : taskBox.entrySet()) {
        this.dataWorker.pushAll(box.getValue().getList(), box.getKey());
      }
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public void pullAll() {
    try {
      for (Map.Entry<DataHandler.BoxType, TaskContainer> box : taskBox.entrySet()) {
        box.getValue().setList(dataWorker.getAll(box.getKey()));
      }
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public void updateAll() {
    try {
      for (Map.Entry<DataHandler.BoxType, TaskContainer> box : taskBox.entrySet()) {
        BoxType boxType = box.getKey();
        synchList(boxType, dataWorker.getAll(boxType));
      }
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  private void synchList(BoxType type, List<Task> updatedList) {
    List<Task> listToUpdate = this.taskBox.get(type).getList();

    for (Task task : updatedList) {
      if (!listToUpdate.contains(task)) {
        listToUpdate.add(task);
      } else {
        Task taskToUpdate = listToUpdate.get(listToUpdate.indexOf(task));

        if ( task.getTimeOfLastChange().isAfter(taskToUpdate.getTimeOfLastChange()) ) {
          taskToUpdate.setStatement(task.getStatement());
          taskToUpdate.setTTimeOfLastChange(task.getTimeOfLastChange());
        }
      }
    }
  }


  public HashMap<DataHandler.BoxType, TaskContainer> getBox() { return taskBox; }

  public DataBaseManager getDBmanager() { return DBmanager; }

  public DataWorker getDataWorker() {
    return dataWorker;
  }

  public HashMap<BoxType, TaskContainer> getTaskBox() {
    return taskBox;
  }

  public void setOnlineWorker() { this.dataWorker = this.DBmanager.getOnlineWorker(); }

  public void setOfflineWorker() { this.dataWorker = this.DBmanager.getOfflineWorker(); }
};
