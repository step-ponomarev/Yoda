package edu.ponomarev.step.MVC.model.repository.task;

import edu.ponomarev.step.component.task.InformatedTask;
import edu.ponomarev.step.component.task.Task;
import edu.ponomarev.step.component.taskContainer.termContainer.TermTaskContainer;
import edu.ponomarev.step.component.taskContainer.termContainer.ContainerVariable.ContainerType;

import java.util.List;
import java.util.Queue;

public interface TaskRepository {
  void add(InformatedTask task);
  void add(TermTaskContainer container);
  void remove(InformatedTask task);
  void remove(Queue<InformatedTask> tasks);
  void update(InformatedTask task);
  void update(TermTaskContainer tasks);

  List<Task> getList(ContainerType containerTask);
}
