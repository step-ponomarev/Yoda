package edu.ponomarev.step.component.project;

import edu.ponomarev.step.system.TimeManager;

import java.time.LocalDateTime;
import java.util.Objects;

public class Project {
  private String name;
  private LocalDateTime timeOfCreation;
  private LocalDateTime timeOfLastChange;
  private ProjectStatus status;

  public enum ProjectStatus {
    PLANNING,
    PAUSED,
    RUNNING
  }

  public Project(String name) {
    name = name;
    timeOfCreation = TimeManager.getLocalDateTimeOf(LocalDateTime.now());
    timeOfLastChange = timeOfCreation;
    //this.tasks = new TermTaskContainer();
    status = ProjectStatus.PLANNING;
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
    return Objects.equals(timeOfCreation, project.timeOfCreation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timeOfCreation);
  }
}
