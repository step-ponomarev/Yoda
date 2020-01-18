package edu.ponomarev.step.component.task;

import edu.ponomarev.step.component.project.Project;
import edu.ponomarev.step.component.taskContainer.termContainer.ContainerVariable.ContainerType;

public class InformatedTask extends TaskDecorator {
  private ContainerType containerType;
  private Project project;

  public InformatedTask(Task task, ContainerType containerType) {
    super(task);
    this.containerType = containerType;
  }

  public ContainerType getContainerType() {
    return containerType;
  }

  public void setContainerType(ContainerType containerType) {
    this.containerType = containerType;
  }

  public Project getProject() {
    return project;
  }

  public void setProject(Project project) {
    this.project = project;
  }
}
