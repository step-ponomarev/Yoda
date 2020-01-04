package edu.ponomarev.step.data;

import edu.ponomarev.step.manager.DataHandler;
import edu.ponomarev.step.component.task.Task;

import java.util.List;

public interface DataWorker {
  void put(DataHandler.BoxType type, Task task) throws Exception;
  void putAll(DataHandler.BoxType type, List<Task> task) throws Exception;
  List pull(DataHandler.BoxType type) throws Exception;
}
