package edu.ponomarev.step.MVC.model.repository;

import java.util.List;

public interface Repository<T> {
  void add(T obj, Specification specification) throws Exception;
  void add(List<T> objs, Specification specification) throws Exception;
  void remove(T obj, Specification specification) throws Exception;
  void remove(List<T> objs) throws Exception;
  void update(T obj, Specification specification) throws Exception;
  void update(List<T> objs, Specification specification) throws Exception;

  List<T> getList(Specification specification) throws Exception;
}
