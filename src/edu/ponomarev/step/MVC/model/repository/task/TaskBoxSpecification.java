package edu.ponomarev.step.MVC.model.repository.task;

import edu.ponomarev.step.MVC.model.repository.Specification;
import edu.ponomarev.step.component.taskContainer.termContainer.ContainerType;

public class TaskBoxSpecification implements Specification<ContainerType> {
  private ContainerType type;

  public TaskBoxSpecification(ContainerType type) {
    this.type = type;
  }

  @Override
  public String getSqlSpecification() {
    return type.toString();
  }

  @Override
  public ContainerType getSerialisationSpecification() {
    return type;
  }
}
