package edu.ponomarev.step.manager;

import edu.ponomarev.step.dao.DataBaseManager;
import edu.ponomarev.step.data.DataWorker;
import edu.ponomarev.step.component.task.Task;
import edu.ponomarev.step.component.task.TaskContainer;

import java.util.HashMap;
import java.util.Map;

public class DataHandler {
  public static class BoxRequestWrap {
    public DataHandler.BoxType type;
    public String boxName;

    public BoxRequestWrap(DataHandler.BoxType type, String boxName) {
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

  final public static BoxRequestWrap[] BOX_VARIABLES = new DataHandler.BoxRequestWrap[] {
      new BoxRequestWrap(DataHandler.BoxType.INBOX, "Inbox"),
      new BoxRequestWrap(DataHandler.BoxType.DAY, "Today"),
      new BoxRequestWrap(DataHandler.BoxType.WEEK, "Week"),
      new BoxRequestWrap(DataHandler.BoxType.LATE, "Late")
  };

  private DataBaseManager DBmanager;
  private DataWorker dataWorker;

  private HashMap<DataHandler.BoxType, TaskContainer> taskBox;

  public static String getBoxName(BoxType boxType) {
    String boxName =  new String();
    for (DataHandler.BoxRequestWrap item : DataHandler.BOX_VARIABLES) {
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

    this.DBmanager = new DataBaseManager();
    this.dataWorker = DBmanager.getOfflineWorker();
    pullData();
  }

  public void addTask(BoxType type, Task task) {
    taskBox.get(type).add(task);

    try {
      //Offline pushing;
      this.dataWorker = DBmanager.getOfflineWorker();
      this.dataWorker.push(task, type);


      //Try to push to DB
      this.dataWorker = DBmanager.getWorker();
      if (DBmanager.isONLINE()) {
        dataWorker.push(task, type);
      }
    } catch (Exception exception) {
      System.err.println(exception.getMessage());
    }
  }

  public void pushDate() {
    try {
      for (Map.Entry<DataHandler.BoxType, TaskContainer> box : taskBox.entrySet()) {
        this.dataWorker.pushAll(box.getValue().getList(), box.getKey());
      }
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public void pullData() {
    try {
      for (Map.Entry<DataHandler.BoxType, TaskContainer> box : taskBox.entrySet()) {
        box.getValue().setList(dataWorker.getAll(box.getKey()));
      }
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public HashMap<DataHandler.BoxType, TaskContainer> getBox() { return taskBox; }

  public DataBaseManager getDBmanager() { return DBmanager; }

  public DataWorker getDataWorker() {
    return dataWorker;
  }

  public void setDataWorkerAuto() { this.dataWorker = this.DBmanager.getWorker(); }

  public void setOfflineDataWorker() {
    this.dataWorker = this.DBmanager.getOfflineWorker();
  }
};
