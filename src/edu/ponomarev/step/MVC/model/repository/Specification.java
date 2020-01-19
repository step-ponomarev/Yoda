package edu.ponomarev.step.MVC.model.repository;

public interface Specification<T> {
  String getSqlSpecification();
  T getSerialisationSpecification();
}
