package edu.ponomarev.step.data_base;

import edu.ponomarev.step.manager.TaskHandler;
import edu.ponomarev.step.task.Task;

import java.util.List;

public interface DBWorker {
  void insertTask(TaskHandler.BoxType type, Task task) throws Exception;
  List selectTask(TaskHandler.BoxType type) throws Exception;
}
