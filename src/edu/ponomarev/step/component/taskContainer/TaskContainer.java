package edu.ponomarev.step.component.taskContainer;

import edu.ponomarev.step.component.task.Task;

public interface TaskContainer {
  void add(Task task);
  void remove(Task task);
  boolean isEmpty();
}
