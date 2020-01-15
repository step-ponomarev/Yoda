package edu.ponomarev.step.MVC.model.worker;

import edu.ponomarev.step.MVC.model.DataHandler;
import edu.ponomarev.step.component.task.InformatedTask;
import edu.ponomarev.step.component.task.Task;

import java.sql.SQLException;
import java.util.List;
import java.util.Queue;

public interface TaskWorker {
  void push(Task task, DataHandler.BoxType type) throws Exception;
  void pushAll(List<Task> task, DataHandler.BoxType type) throws Exception;
  void remove(InformatedTask task) throws SQLException;
  void removeAll(Queue<InformatedTask> tasks) throws SQLException;
  List getAll(DataHandler.BoxType type) throws Exception;
}
