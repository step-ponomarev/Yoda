package edu.ponomarev.step.component.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import java.io.Serializable;

public class Task implements Comparable<Task>, Serializable {
  private String statement;
  private LocalDate date_of_creation;
  private LocalDateTime time_of_last_change;

  public Task(String statement) {
    this.statement = statement;
    this.date_of_creation = LocalDate.now();
    this.time_of_last_change = LocalDateTime.now();
  }

  public Task(String statement, LocalDate date_of_creation, LocalDateTime time_of_last_change) {
    this.statement = statement;
    this.date_of_creation = date_of_creation;
    this.time_of_last_change = time_of_last_change;
  }

  public void setStatement(String statement) { this.statement = statement; }

  public String getStatement() {
    return this.statement;
  }

  public LocalDate getDateOfCreation() { return this.date_of_creation; }

  public LocalDateTime getTimeOfLastChange() { return this.time_of_last_change; }

  public void isChanged() { this.time_of_last_change = LocalDateTime.now(); }

  @Override
  public String toString() { return this.statement; }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Task)) return false;
    Task task = (Task) o;
    return ( this.statement.equals(task.statement) && this.date_of_creation.equals(task.date_of_creation) );
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.statement, this.date_of_creation);
  }

  @Override
  public int compareTo(Task o) {
    return this.date_of_creation.compareTo(o.date_of_creation);
  }
}
