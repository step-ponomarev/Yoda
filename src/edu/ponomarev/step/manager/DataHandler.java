package edu.ponomarev.step.manager;

import edu.ponomarev.step.data.DataWorker;
import edu.ponomarev.step.component.task.Task;
import edu.ponomarev.step.component.task.TaskContainer;

// TODO: Нарисовать ЮМЛ обслуживателя задач

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
      dataWorker.put(type, task);
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public void pushDate() {
    try {
      dataWorker.putAll(BoxType.INBOX, inbox.getList());
      dataWorker.putAll(BoxType.DAY, todayBox.getList());
      dataWorker.putAll(BoxType.WEEK, weekBox.getList());
      dataWorker.putAll(BoxType.LATE, lateBox.getList());
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public void pullData() {
    try {
      this.inbox.setList(dataWorker.pull(BoxType.INBOX));
      this.todayBox.setList(dataWorker.pull(BoxType.DAY));
      this.weekBox.setList(dataWorker.pull(BoxType.WEEK));
      this.lateBox.setList(dataWorker.pull(BoxType.LATE));
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public TaskContainer getBox(String name) {
    TaskContainer returned = new TaskContainer();

    if (name.equals("Inbox")) {
      returned = this.inbox;
    } else if (name.equals("Today")) {
      returned = this.todayBox;
    } else if (name.equals("Week")) {
      returned = this.weekBox;
    } else if (name.equals("Late")) {
      returned = this.lateBox;
    }

    return returned;
  }

  public TaskContainer getInbox() {
    return this.inbox;
  }

  public TaskContainer getTodayBox() {
    return this.todayBox;
  }

  public TaskContainer getWeekBox() {
    return this.weekBox;
  }

  public TaskContainer getLateBox() {
    return this.lateBox;
  }

  public void setDataWorker() {
    this.dataWorker = this.DBmanager.getWorker();
  }
};
