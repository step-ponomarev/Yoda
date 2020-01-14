package edu.ponomarev.step.component.task;

import edu.ponomarev.step.system.TimeManager;

import java.time.LocalDateTime;
import java.util.Objects;

import java.io.Serializable;

public class Task implements Comparable<Task>, Serializable {
  private String statement;
  private LocalDateTime timeOfCreation;
  private LocalDateTime timeOfLastChange;

  public Task(String statement) {
    this.statement = statement;
    this.timeOfCreation = TimeManager.getLocalDateTimeOf(LocalDateTime.now());
    this.timeOfLastChange = this.timeOfCreation;
  }

  public Task(String statement, LocalDateTime timeOfCreation, LocalDateTime timeOfLastChange) {
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

  public void isChanged() {
    this.timeOfLastChange = TimeManager.getLocalDateTimeOf(LocalDateTime.now());
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
    return (this.timeOfCreation.equals(task.timeOfCreation));
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.timeOfCreation);
  }

  @Override
  public int compareTo(Task o) {
    return this.timeOfCreation.compareTo(o.timeOfCreation);
  }
}
