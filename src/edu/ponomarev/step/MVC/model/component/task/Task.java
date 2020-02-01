package edu.ponomarev.step.MVC.model.component.task;

import edu.ponomarev.step.system.TimeManager;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import java.io.Serializable;

public class Task implements Comparable<Task>, Serializable {
  private String uuid;
  private String statement;
  private LocalDateTime timeOfCreation;
  private LocalDateTime timeOfLastChange;

  public Task(Task task) {
    this.statement = task.getStatement();
    this.timeOfCreation = TimeManager.getLocalDateTimeOf(task.getTimeOfCreation());
    this.timeOfLastChange =  TimeManager.getLocalDateTimeOf(task.getTimeOfLastChange());
    this.uuid = task.getUUID();
  }

  public Task(String statement) {
    this.statement = statement;
    this.timeOfCreation = TimeManager.getLocalDateTimeOf(LocalDateTime.now());
    this.timeOfLastChange = this.timeOfCreation;

    generateUUID();
  }

  public Task(String uuid, String statement, LocalDateTime timeOfCreation, LocalDateTime timeOfLastChange) {
    this.uuid = uuid;
    this.statement = statement;
    this.timeOfCreation = TimeManager.getLocalDateTimeOf(timeOfCreation);
    this.timeOfLastChange = TimeManager.getLocalDateTimeOf(timeOfLastChange);
  }

  public void setStatement(String statement) {
    this.statement = statement;
  }

  public String getStatement() {
    return this.statement;
  }

  public LocalDateTime getTimeOfCreation() {
    return this.timeOfCreation;
  }

  public LocalDateTime getTimeOfLastChange() {
    return this.timeOfLastChange;
  }

  public void setTTimeOfLastChange(LocalDateTime time_of_last_change) {
    this.timeOfLastChange = TimeManager.getLocalDateTimeOf(time_of_last_change);
  }

  public String getUUID() {
    return uuid;
  }

  public void updateTimeOfLastChange() {
    this.timeOfLastChange = TimeManager.getLocalDateTimeOf(LocalDateTime.now());
  }

  private void generateUUID() {
    UUID uuid = UUID.randomUUID();
    this.uuid =  uuid.toString();
  }

  @Override
  public String toString() {
    return this.statement;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Task)) return false;
    Task task = (Task) o;

    return this.timeOfCreation.equals(task.timeOfCreation)
        && this.uuid.equals(task.uuid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.timeOfCreation, this.uuid);
  }

  @Override
  public int compareTo(Task o) {
    return this.timeOfCreation.compareTo(o.timeOfCreation);
  }
}
