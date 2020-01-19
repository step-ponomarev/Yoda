package edu.ponomarev.step.MVC.model.repository;

import java.util.List;

public interface Repository<T> {
  void add(T task, Specification specification);
  void add(List<T> objs, Specification specification);
  void remove(T task, Specification specification);
  void remove(List<T> objs, Specification specification);
  void update(T obj, Specification specification);
  void update(List<T> objs, Specification specification);

  List<T> getList(Specification specification);
}
