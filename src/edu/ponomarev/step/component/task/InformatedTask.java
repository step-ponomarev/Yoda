package edu.ponomarev.step.component.task;

import edu.ponomarev.step.component.project.Project;
import edu.ponomarev.step.component.BoxType;

public class InformatedTask extends Task {
  private BoxType boxType;
  private Project project;

  public InformatedTask(Task task, BoxType boxType) {
    super(task);
    this.boxType = boxType;
    this.project = null;
  }

  public InformatedTask(Task task, BoxType boxType, Project projectOwner) {
    super(task);
    this.boxType = boxType;
    this.project = projectOwner;
  }

  public BoxType getBoxType() {
    return boxType;
  }

  public Project getProject() {
    return project;
  }
}
