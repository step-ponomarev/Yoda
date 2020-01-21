package edu.ponomarev.step.MVC.model.repository;

import java.util.List;
import java.util.Queue;

public interface Repository<T> {
  void add(T obj, Specification specification) throws Exception;
  void add(List<T> objs, Specification specification) throws Exception;
  void remove(T task, Specification specification) throws Exception;
  void remove(Queue<T> objs) throws Exception;
  void update(T obj, Specification specification) throws Exception;
  void update(List<T> objs, Specification specification) throws Exception;

  List<T> getList(Specification specification) throws Exception;
}
