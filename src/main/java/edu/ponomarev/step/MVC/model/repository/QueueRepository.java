package edu.ponomarev.step.MVC.model.repository;

import java.util.List;

public interface QueueRepository<T> {
  void push(List<T> queue, Specification specification) throws Exception;
  List<T> get(Specification specification) throws Exception;
}
