package edu.ponomarev.step.data;

import edu.ponomarev.step.manager.TaskHandler;
import edu.ponomarev.step.task.Task;

import java.util.List;

public interface DataWorker {
  void put(TaskHandler.BoxType type, Task task) throws Exception;
  void putAll(TaskHandler.BoxType type, List<Task> task) throws Exception;
  List pull(TaskHandler.BoxType type) throws Exception;
}
