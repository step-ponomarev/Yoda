package edu.ponomarev.step.boxes;

import edu.ponomarev.step.task.Task;

public interface TaskStorage {
  void add(Task task);
  void remove(Task task);
}
