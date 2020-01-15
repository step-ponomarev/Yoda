package edu.ponomarev.step.component.task;

import edu.ponomarev.step.MVC.model.DataHandler;
import edu.ponomarev.step.component.project.Project;

import java.time.LocalDateTime;

public class InformatedTask extends TaskDecorator {
  private Task task;
  private DataHandler.BoxType boxType;
  private Project project;

  public InformatedTask(Task task, DataHandler.BoxType boxType) {
    super(task);
    this.task = task;
    this.boxType = boxType;
  }

  public InformatedTask(Task task, DataHandler.BoxType boxType, Project project) {
    super(task);
    this.task = task;
    this.boxType = boxType;
    this.project = project;
  }

  public InformatedTask(String statement) {
    super(statement);
  }

  public InformatedTask(String statement, LocalDateTime timeOfCreation, LocalDateTime timeOfLastChange) {
    super(statement, timeOfCreation, timeOfLastChange);
  }

  public Task getTask() {
    return task;
  }

  public void setTask(Task task) {
    this.task = task;
  }

  public DataHandler.BoxType getBoxType() {
    return boxType;
  }

  public void setBoxType(DataHandler.BoxType boxType) {
    this.boxType = boxType;
  }

  public Project getProject() {
    return project;
  }

  public void setProject(Project project) {
    this.project = project;
  }
}
