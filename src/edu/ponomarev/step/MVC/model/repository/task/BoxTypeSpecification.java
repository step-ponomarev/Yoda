package edu.ponomarev.step.MVC.model.repository.task;

import edu.ponomarev.step.MVC.model.repository.Specification;
import edu.ponomarev.step.component.BoxType;

public class BoxTypeSpecification implements Specification<BoxType> {
  private BoxType type;

  public BoxTypeSpecification(BoxType type) {
    this.type = type;
  }

  @Override
  public String getSqlSpecification() {
    return type.toString();
  }

  @Override
  public BoxType getSerialisationSpecification() {
    return type;
  }
}
