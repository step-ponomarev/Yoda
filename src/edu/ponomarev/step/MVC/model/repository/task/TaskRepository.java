package edu.ponomarev.step.MVC.model.repository.task;

import edu.ponomarev.step.component.task.InformatedTask;
import edu.ponomarev.step.component.taskContainer.TaskContainer;
import edu.ponomarev.step.component.taskContainer.TermTaskContainer;

import java.util.Queue;

public interface TaskRepository {
  void add(InformatedTask task);
  void add(TermTaskContainer container);
  void remove(InformatedTask task);
  void remove(Queue<InformatedTask> tasks);
  void update(InformatedTask task);
  void update(TermTaskContainer tasks);

  TaskContainer getContainer(TermTaskContainer.ContainerType containerTask);
}
