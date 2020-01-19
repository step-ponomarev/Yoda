package edu.ponomarev.step.MVC.model.repository;

public class FreeSpecification implements Specification<Object> {
  @Override
  public String getSqlSpecification() { return null; }

  @Override
  public Object getSerialisationSpecification() { return null; }
}
