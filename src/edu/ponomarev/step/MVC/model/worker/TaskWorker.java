package edu.ponomarev.step.MVC.model.worker;

import edu.ponomarev.step.component.task.InformatedTask;
import edu.ponomarev.step.component.taskContainer.TaskContainer;
import edu.ponomarev.step.component.taskContainer.TermTaskContainer;

import java.sql.SQLException;
import java.util.Queue;

public interface TaskWorker {
  void push(InformatedTask task) throws Exception;
  void push(TermTaskContainer container) throws Exception;
  void remove(InformatedTask task) throws SQLException;
  void remove(Queue<InformatedTask> tasks) throws SQLException;
  TaskContainer getContainer(TermTaskContainer.ContainerType containerTask) throws Exception;
}
