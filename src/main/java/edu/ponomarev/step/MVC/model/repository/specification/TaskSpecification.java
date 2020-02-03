package edu.ponomarev.step.MVC.model.repository.specification;

import edu.ponomarev.step.MVC.model.repository.Specification;
import edu.ponomarev.step.MVC.model.component.task.TaskRelations;

public class TaskSpecification implements Specification<TaskRelations> {
  private TaskRelations taskRelations;

  public TaskSpecification(TaskRelations taskRelations) {
    this.taskRelations = taskRelations;
  }

  @Override
  public TaskRelations getSpecification() {
    return taskRelations;
  }
}
