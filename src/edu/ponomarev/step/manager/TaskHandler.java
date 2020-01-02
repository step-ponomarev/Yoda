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

    try {
      dbWorker.selectTask(BoxType.INBOX, inbox);
      dbWorker.selectTask(BoxType.DAY, todayBox);
      dbWorker.selectTask(BoxType.WEEK, weekBox);
      dbWorker.selectTask(BoxType.LATE, lateBox);
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
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
      dbWorker.insertTask(type, task);
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

  /*public void move(TaskHandler.BoxType from, TaskHandler.BoxType to, Task task) {

  }*/
};
