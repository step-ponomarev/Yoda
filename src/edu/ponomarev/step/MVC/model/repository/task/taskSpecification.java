package edu.ponomarev.step.MVC.model.repository.task;

import edu.ponomarev.step.component.task.InformatedTask;

public interface taskSpecification {
  boolean specified(InformatedTask task);
}
