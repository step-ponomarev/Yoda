package edu.ponomarev.step.component.task;

import edu.ponomarev.step.component.BoxType;
import edu.ponomarev.step.component.project.Project;

public class TaskRelations {
  private BoxType boxOwnerType;
  private String projectID;

  public TaskRelations(BoxType boxType, Project project) {
    this.boxOwnerType = boxType;
    this.projectID = project.getUuid();
  }

  public TaskRelations(BoxType boxType) {
    this.boxOwnerType = boxType;
    this.projectID = null;
  }

  public String getProjectID() {
    return projectID;
  }

  public BoxType getBoxType() {
    return boxOwnerType;
  }
}

