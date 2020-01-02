package edu.ponomarev.step.manager;

import edu.ponomarev.step.task.Task;
import edu.ponomarev.step.task.TaskContainer;

import java.sql.SQLException;

// TODO: Нарисовать ЮМЛ обслуживателя задач

public class TaskHandler {
  private DBWorker dbWorker;

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

  public TaskHandler(DBWorker db) {
    this.dbWorker = db;

    this.inbox = new TaskContainer();
    this.todayBox = new TaskContainer();
    this.weekBox = new TaskContainer();
    this.lateBox = new TaskContainer();
  }

  public void addTask(BoxType type, Task task) throws SQLException {
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

    dbWorker.insertTask(type, task);
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

  /*public void move(TaskHandler.BoxType from, TaskHandler.BoxType to, Task task) {

  }*/
};
