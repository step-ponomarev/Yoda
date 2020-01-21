package edu.ponomarev.step.component.task;

import edu.ponomarev.step.component.BoxType;
import edu.ponomarev.step.component.project.Project;

public class TaskRelations {
  private BoxType boxOwnerType;
  private Project projectOwner;

  public TaskRelations(BoxType boxType, Project project) {
    this.boxOwnerType = boxType;
    this.projectOwner = project;
  }

  public TaskRelations(BoxType boxType) {
    this.boxOwnerType = boxType;
    this.projectOwner = null;
  }

  public Project getProjectOwner() {
    return projectOwner;
  }

  public BoxType getBoxOwnerType() {
    return boxOwnerType;
  }
}

