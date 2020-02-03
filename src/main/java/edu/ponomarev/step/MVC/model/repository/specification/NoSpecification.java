package edu.ponomarev.step.MVC.model.repository.specification;

import edu.ponomarev.step.MVC.model.repository.Specification;

public class NoSpecification implements Specification<Object> {
  @Override
  public Object getSpecification() {
    return null;
  }
}
