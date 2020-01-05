package edu.ponomarev.step.data;

import edu.ponomarev.step.manager.DataHandler;
import edu.ponomarev.step.component.task.Task;

import java.util.List;

public interface DataWorker {
  void push(DataHandler.BoxType type, Task task) throws Exception;
  void pushAll(List<Task> task, DataHandler.BoxType type) throws Exception;
  List pullAll(DataHandler.BoxType type) throws Exception;
}
