package edu.ponomarev.step.component.project;

import edu.ponomarev.step.system.TimeManager;

import java.time.LocalDateTime;

import java.util.Objects;
import java.util.UUID;

public class Project {
  private String uuid;
  private String name;
  private LocalDateTime timeOfCreation;
  private LocalDateTime timeOfLastChange;

  public Project(String name) {
    this.name = name;
    timeOfCreation = TimeManager.getLocalDateTimeOf(LocalDateTime.now());
    timeOfLastChange = timeOfCreation;

    generateUUID();
  }

  public Project(String uuid, String name, LocalDateTime timeOfCreation, LocalDateTime timeOfLastChange) {
    this.uuid = uuid;
    this.name = name;
    this.timeOfLastChange = timeOfCreation;
    this.timeOfCreation = TimeManager.getLocalDateTimeOf(LocalDateTime.now());
  }

  public String getUuid() {
    return uuid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDateTime getTimeOfCreation() {
    return timeOfCreation;
  }

  public LocalDateTime getTimeOfLastChange() {
    return timeOfLastChange;
  }

  public void setTimeOfLastChange(LocalDateTime timeOfLastChange) {
    this.timeOfLastChange = timeOfLastChange;
  }

  public void updateTimeOfLastChange() {
    this.timeOfLastChange = TimeManager.getLocalDateTimeOf(LocalDateTime.now());
  }

  private void generateUUID() {
    UUID uuid = UUID.randomUUID();
    this.uuid = uuid.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Project)) return false;
    Project project = (Project) o;

    return timeOfCreation.equals(project.timeOfCreation)
        && uuid.equals(project.uuid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timeOfCreation, uuid);
  }
}
