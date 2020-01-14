package edu.ponomarev.step.worker;

import edu.ponomarev.step.manager.DataHandler;
import edu.ponomarev.step.component.task.Task;

import java.sql.SQLException;
import java.util.List;
import java.util.Queue;

public interface DataWorker {
  void push(Task task, DataHandler.BoxType type) throws Exception;
  void pushAll(List<Task> task, DataHandler.BoxType type) throws Exception;
  void remove(Task task) throws SQLException;
  void removeAll(Queue<Task> tasks) throws SQLException;
  List getAll(DataHandler.BoxType type) throws Exception;
}
