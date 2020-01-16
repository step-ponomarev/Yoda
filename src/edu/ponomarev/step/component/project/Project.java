package edu.ponomarev.step.component.project;

import edu.ponomarev.step.component.taskContainer.TermTaskContainer;

import java.util.Calendar;
import java.util.Objects;

public class Project {
  private String name;
  private Calendar date_of_creation;
  private TermTaskContainer tasks;
  private ProjectStatus status;

  public Project(String name) {
    this.name = name;
    this.date_of_creation = Calendar.getInstance();
    //this.tasks = new TermTaskContainer();
    this.status = ProjectStatus.PLANNING;
  }

  public void setStatus(ProjectStatus status) {
    this.status = status;
  }

  public ProjectStatus getStatus() {
    return this.status;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Project)) return false;
    Project project = (Project) o;
    return Objects.equals(name, project.name) &&
        Objects.equals(date_of_creation, project.date_of_creation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, date_of_creation);
  }
}
