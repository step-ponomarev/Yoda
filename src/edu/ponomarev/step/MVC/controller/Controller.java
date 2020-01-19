package edu.ponomarev.step.MVC.controller;

import edu.ponomarev.step.MVC.model.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
// TODO Переписать помойку

@Component
public class Controller {
  @Autowired
  @Qualifier("worker")
  private Worker worker;

  @Value("null")
  private Object window;

  @PostConstruct
  public void start() {
    System.out.println("Application is started");
  }
}
