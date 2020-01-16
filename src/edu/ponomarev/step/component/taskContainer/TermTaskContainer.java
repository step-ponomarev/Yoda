package edu.ponomarev.step.component.taskContainer;
import edu.ponomarev.step.component.task.Task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TermTaskContainer implements TaskContainer, Serializable {
  public static class ContainerVariable implements Serializable {
    public ContainerType type;
    public String name;

    public ContainerVariable(ContainerType type, String name) {
      this.type = type;
      this.name = name;
    }

    @Override
    public String toString() {
      return name;
    }
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

  public enum ContainerType {
    INBOX(0),
    DAY(1),
    WEEK(2),
    LATE(3);

    private int value;

    ContainerType(int i) {
      this.value = i;
    }

    public int toInteger() {
      return value;
    }
  }

  enum Strategy {
    DATE,
    STATEMENT
  }

  final public static ContainerVariable[] BOX_VARIABLES = new ContainerVariable[] {
      new ContainerVariable(ContainerType.INBOX, "Inbox"),
      new ContainerVariable(ContainerType.DAY, "Today"),
      new ContainerVariable(ContainerType.WEEK, "Week"),
      new ContainerVariable(ContainerType.LATE, "Late")
  };

  private ContainerType containerType;
  private ArrayList<Task> taskList;
  private Comparator<Task> sortStrategy;

  public TermTaskContainer(ContainerType containerType) {
    this.taskList = new ArrayList<Task>();
    this.sortStrategy = new DateCompare();
    this.containerType = containerType;
  }

  public void setList(List<Task> list) {
    taskList.addAll(list);
  }

  public List<Task> getList() { return taskList; }

  public void setContainerType(ContainerType containerType) {
    this.containerType = containerType;
  }

  public ContainerType getContainerType() {
    return containerType;
  }

  public void sort() {
    Collections.sort(taskList, sortStrategy);
  }

  public Object[] toArray() {
    return taskList.toArray();
  }

  @Override
  public boolean isEmpty() {
    return taskList.isEmpty();
  }

  @Override
  public void add(Task task) {
    taskList.add(task);
  }

  @Override
  public void remove(Task task) {
    taskList.remove(task);
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
