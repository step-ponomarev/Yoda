package edu.ponomarev.step.manager;

import edu.ponomarev.step.task.Task;
import edu.ponomarev.step.task.TaskContainer;

// TODO: Нарисовать ЮМЛ обслуживателя задач

public class TaskHandler {
  private TaskContainer tasks;

  /*enum BoxType {
    DAY,
    WEEK,
    LATE
  }*/

  public TaskHandler() {
    this.tasks = new TaskContainer();
  }

  public void addTask(Task task) {
    tasks.add(task);
  }

  public void removeTask(final int index) {
    tasks.remove(tasks.get(index));
  }

  /*public void move(TaskHandler.BoxType from, TaskHandler.BoxType to, Task task) {

  }*/

  public void show() {
    tasks.show();
  }
};
