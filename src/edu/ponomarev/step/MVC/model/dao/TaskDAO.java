package edu.ponomarev.step.MVC.model.dao;

import edu.ponomarev.step.component.task.InformatedTask;
import edu.ponomarev.step.component.taskContainer.TaskContainer;
import edu.ponomarev.step.component.taskContainer.TermTaskContainer;

import java.util.Queue;

public interface TaskDAO {
  void push(InformatedTask task);
  void push(TermTaskContainer container);
  void remove(InformatedTask task);
  void remove(Queue<InformatedTask> tasks);
  TaskContainer getContainer(TermTaskContainer.ContainerType containerTask);
}
