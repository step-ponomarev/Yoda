package edu.ponomarev.step.component.task;

import java.time.LocalDateTime;

public class TaskDecorator extends Task {

  public TaskDecorator(Task task) {
    super(task);
  }

  public TaskDecorator(String statement) {
    super(statement);
  }

  public TaskDecorator(String statement, LocalDateTime timeOfCreation, LocalDateTime timeOfLastChange) {
    super(statement, timeOfCreation, timeOfLastChange);
  }
}
