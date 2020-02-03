package edu.ponomarev.step.MVC.model.repository.specification;

import edu.ponomarev.step.MVC.model.repository.Specification;

public class RemoveTaskSpecificatiom implements Specification<String> {
  @Override
  public String getSpecification() {
    return "tasks_to_remove.ser";
  }
}
