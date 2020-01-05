package edu.ponomarev.step.manager;

import edu.ponomarev.step.data.DataWorker;
import edu.ponomarev.step.component.task.Task;
import edu.ponomarev.step.component.task.TaskContainer;

public class DataHandler {
  private DataBaseManager DBmanager;
  private DataWorker dataWorker;

  private TaskContainer inbox;
  private TaskContainer todayBox;
  private TaskContainer weekBox;
  private TaskContainer lateBox;

  public enum BoxType {
    INBOX,
    DAY,
    WEEK,
    LATE
  }

  public DataHandler() {
    this.DBmanager = new DataBaseManager();

    this.dataWorker = this.DBmanager.getWorker();

    this.inbox = new TaskContainer();
    this.todayBox = new TaskContainer();
    this.weekBox = new TaskContainer();
    this.lateBox = new TaskContainer();

    pullData();
  }

  public void addTask(BoxType type, Task task) {
    switch (type) {
      case DAY:
        todayBox.add(task);
        break;

      case WEEK:
        weekBox.add(task);
        break;

      case LATE:
        lateBox.add(task);
        break;

      default:
        inbox.add(task);
        break;
    }

    try {
      dataWorker.push(type, task);
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public void pushDate() {
    try {
      dataWorker.pushAll(inbox.getList(), BoxType.INBOX);
      dataWorker.pushAll(todayBox.getList(), BoxType.DAY);
      dataWorker.pushAll(weekBox.getList(), BoxType.WEEK);
      dataWorker.pushAll(lateBox.getList(), BoxType.LATE);
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public void pullData() {
    try {
      this.inbox.setList(dataWorker.pullAll(BoxType.INBOX));
      this.todayBox.setList(dataWorker.pullAll(BoxType.DAY));
      this.weekBox.setList(dataWorker.pullAll(BoxType.WEEK));
      this.lateBox.setList(dataWorker.pullAll(BoxType.LATE));
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public TaskContainer getBox(BoxType type) {
    switch (type) {
      case DAY:
        return this.todayBox;

      case WEEK:
        return this.weekBox;

      case LATE:
        return this.lateBox;

      default:
        return this.inbox;
    }
  }

  public void setDataWorker() {
    this.dataWorker = this.DBmanager.getWorker();
  }
};
