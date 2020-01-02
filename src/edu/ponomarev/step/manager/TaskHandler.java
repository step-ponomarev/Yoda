package edu.ponomarev.step.manager;

import edu.ponomarev.step.data.DataWorker;
import edu.ponomarev.step.task.Task;
import edu.ponomarev.step.task.TaskContainer;

// TODO: Нарисовать ЮМЛ обслуживателя задач

public class TaskHandler {
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

  public TaskHandler(DataWorker db) {
    this.dataWorker = db;

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
      this.inbox.setTaskList(dataWorker.pull(BoxType.INBOX));
      this.todayBox.setTaskList(dataWorker.pull(BoxType.DAY));
      this.weekBox.setTaskList(dataWorker.pull(BoxType.WEEK));
      this.lateBox.setTaskList(dataWorker.pull(BoxType.LATE));
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public TaskContainer getInbox() {
    return inbox;
  }

  public TaskContainer getTodayBox() {
    return todayBox;
  }

  public TaskContainer getWeekBox() {
    return weekBox;
  }

  public TaskContainer getLateBox() {
    return lateBox;
  }

  public void setDataWorker(DataWorker dataWorker) {
    this.dataWorker = dataWorker;
  }
};
