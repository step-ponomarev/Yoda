package edu.ponomarev.step.worker;

import edu.ponomarev.step.manager.DataHandler;
import edu.ponomarev.step.component.task.Task;

import java.util.List;

public interface DataWorker {
  void push(Task task, DataHandler.BoxType type) throws Exception;
  void pushAll(List<Task> task, DataHandler.BoxType type) throws Exception;
  List getAll(DataHandler.BoxType type) throws Exception;
}
