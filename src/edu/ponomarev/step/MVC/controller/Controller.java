package edu.ponomarev.step.MVC.controller;

import edu.ponomarev.step.MVC.model.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

public class Controller {
  @Autowired
  @Qualifier("worker")
  private Worker worker;

  @Value("null")
  private Object window;

  @PostConstruct
  public void start() {
    return;
  }
}
