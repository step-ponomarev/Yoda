package edu.ponomarev.step.component.task;

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

  class DateCompare implements Comparator<Task> {
    @Override
    public int compare(Task o1, Task o2) {
      return o1.getTimeOfCreation().compareTo(o2.getTimeOfCreation());
    }
  }

  class StatmentCompare implements Comparator<Task> {
    @Override
    public int compare(Task o1, Task o2) {
      return o1.getStatement().compareTo(o2.getStatement());
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

  public void setList(List<Task> list) {
    taskList.addAll(list);
  }

  public List getList() { return taskList; }

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
