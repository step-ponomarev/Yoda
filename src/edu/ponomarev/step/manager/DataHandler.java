package edu.ponomarev.step.manager;

import edu.ponomarev.step.data.DataWorker;
import edu.ponomarev.step.component.task.Task;
import edu.ponomarev.step.component.task.TaskContainer;

import java.util.HashMap;
import java.util.Map;

public class DataHandler {
  private DataBaseManager DBmanager;
  private DataWorker dataWorker;

  private HashMap<DataHandler.BoxType, TaskContainer> taskBox;

  public enum BoxType {
    INBOX,
    DAY,
    WEEK,
    LATE
  }

  public DataHandler() {
    this.DBmanager = new DataBaseManager();

    this.dataWorker = this.DBmanager.getWorker();

    taskBox = new HashMap<DataHandler.BoxType, TaskContainer>() {{
      put(BoxType.INBOX, new TaskContainer());
      put(BoxType.DAY, new TaskContainer());
      put(BoxType.WEEK, new TaskContainer());
      put(BoxType.LATE, new TaskContainer());
    }};

    pullData();
  }

  public void addTask(BoxType type, Task task) {
    taskBox.get(type).add(task);

    try {
      dataWorker.push(type, task);
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public void pushDate() {
    try {
      for (Map.Entry<DataHandler.BoxType, TaskContainer> box : taskBox.entrySet()) {
        dataWorker.pushAll(box.getValue().getList(), box.getKey());
      }
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public void pullData() {
    try {
      for (Map.Entry<DataHandler.BoxType, TaskContainer> box : taskBox.entrySet()) {
        box.getValue().setList(dataWorker.pullAll(box.getKey()));
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
