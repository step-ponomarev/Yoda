package edu.ponomarev.step.task;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TaskContainer {
  private ArrayList<Task> taskList;
  private Comparator<Task> sortStrategy;

  enum Strategy {
    DATE,
    STATEMENT
  }

  class DateCompare implements Comparator<Task>, Serializable {
    @Override
    public int compare(Task o1, Task o2) {
      return o1.date_of_creation.compareTo(o2.date_of_creation);
    }
  }

  class StatmentCompare implements Comparator<Task> {
    @Override
    public int compare(Task o1, Task o2) {
      return o1.statement.compareTo(o2.statement);
    }
  }

  public TaskContainer() {
    taskList = new ArrayList<Task>();
    sortStrategy = new DateCompare();
  }

  public Task get(final int index) throws ArrayIndexOutOfBoundsException {
    if (index >= taskList.size()) {
      throw new ArrayIndexOutOfBoundsException("Invalid task index");
    }
    return taskList.get(index);
  }

  public void setTaskList(List<Task> list) {
    taskList.addAll(list);
  }

  public void add(Task task) {
    taskList.add(task);
  }

  public void remove(Task task) {
    taskList.remove(task);
  }

  public void sort() {
    Collections.sort(taskList, sortStrategy);
  }

  public void show() {
    taskList.forEach(System.out::println);
  }

  public void setSortStrategy(Strategy strategy) {
    switch (strategy) {
      case DATE:
        sortStrategy = new DateCompare();
        break;
      case STATEMENT:
        sortStrategy = new StatmentCompare();
        break;
    }
  }

  public Object[] toArray() {
    return taskList.toArray();
  }

  @Override
  public String toString() {
    if (taskList.isEmpty()) {
      return "Empty";
    } else {
      return taskList.get(0).toString();
    }
  }
}
