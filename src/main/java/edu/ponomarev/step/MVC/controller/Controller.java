package edu.ponomarev.step.MVC.controller;

import edu.ponomarev.step.MVC.model.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;

public class Controller {
  @Autowired
  @Qualifier("worker")
  private Worker worker;

  @PostConstruct
  public void start() {
    return;
  }
}
