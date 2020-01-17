package edu.ponomarev.step.component.task;

import edu.ponomarev.step.component.project.Project;
import edu.ponomarev.step.component.taskContainer.termContainer.TermTaskContainer;
import edu.ponomarev.step.component.taskContainer.termContainer.ContainerVariable.ContainerType;

public class InformatedTask extends TaskDecorator {
  private Task task;
  private ContainerType containerType;
  private Project project;

  public InformatedTask(Task task, ContainerType containerType) {
    super(task);
    this.task = task;
    this.containerType = containerType;
  }

  public InformatedTask(Task task, ContainerType containerType, Project project) {
    super(task);
    this.task = task;
    this.containerType = containerType;
    this.project = project;
  }

  public Task getTask() {
    return task;
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
