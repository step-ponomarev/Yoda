package edu.ponomarev.step.component.taskContainer.termContainer;

import edu.ponomarev.step.component.task.Task;
import edu.ponomarev.step.component.taskContainer.TaskContainer;
import edu.ponomarev.step.component.taskContainer.termContainer.ContainerVariable.ContainerType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TermTaskContainer implements TaskContainer, Serializable {
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

  enum Strategy {
    DATE,
    STATEMENT
  }

  private ContainerVariable.ContainerType containerType;
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
