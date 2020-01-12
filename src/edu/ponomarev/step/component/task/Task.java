package edu.ponomarev.step.component.task;

import edu.ponomarev.step.manager.TimeManager;

import java.time.LocalDateTime;
import java.util.Objects;

import java.io.Serializable;

public class Task implements Comparable<Task>, Serializable {
  private String statement;
  private LocalDateTime time_of_creation;
  private LocalDateTime time_of_last_change;

  public Task(String statement) {
    this.statement = statement;

    this.time_of_creation = TimeManager.getLocalDateTimeOf(LocalDateTime.now());
    this.time_of_last_change = this.time_of_creation;
  }

  public Task(String statement, LocalDateTime time_of_creation, LocalDateTime time_of_last_change) {
    this.statement = statement;

    this.time_of_creation = TimeManager.getLocalDateTimeOf(time_of_creation);
    this.time_of_last_change = TimeManager.getLocalDateTimeOf(time_of_last_change);
  }

  public void setStatement(String statement) {
    this.statement = statement;
  }

  public String getStatement() {
    return this.statement;
  }

  public LocalDateTime getTimeOfCreation() {
    return this.time_of_creation;
  }

  public LocalDateTime getTimeOfLastChange() {
    return this.time_of_last_change;
  }

  public void setTTimeOfLastChange(LocalDateTime time_of_last_change) {
    this.time_of_last_change = TimeManager.getLocalDateTimeOf(time_of_last_change);
  }

  public void isChanged() {
    this.time_of_last_change = TimeManager.getLocalDateTimeOf(LocalDateTime.now());
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

    //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");

    return (this.time_of_creation.equals(task.time_of_creation));
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.time_of_creation);
  }

  @Override
  public int compareTo(Task o) {
    return this.time_of_creation.compareTo(o.time_of_creation);
  }
}
