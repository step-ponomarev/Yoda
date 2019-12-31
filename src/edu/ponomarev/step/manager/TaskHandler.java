package edu.ponomarev.step.manager;

import edu.ponomarev.step.task.Task;
import edu.ponomarev.step.task.TaskContainer;

// TODO: Нарисовать ЮМЛ обслуживателя задач

public class TaskHandler {
  private TaskContainer inbox;
  private TaskContainer todayBox;
  private TaskContainer weekBox;
  private  TaskContainer lateBox;

  public enum BoxType {
    DAY,
    WEEK,
    LATE
  }

  public TaskHandler() {
    this.inbox = new TaskContainer();
    this.todayBox = new TaskContainer();
    this.weekBox = new TaskContainer();
    this.lateBox = new TaskContainer();
  }

  public void addTask(BoxType type, Task task) {
    switch (type) {
      case DAY:
        todayBox.add(task);
        return;
      case WEEK:
        weekBox.add(task);
        return;
      case LATE:
        lateBox.add(task);
        return;
      default:
        inbox.add(task);
        return;
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
