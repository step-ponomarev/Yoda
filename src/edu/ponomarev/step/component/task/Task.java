package edu.ponomarev.step.component.task;

import java.time.LocalDate;
import java.util.Objects;

import java.io.Serializable;

public class Task implements Comparable<Task>, Serializable {
  private String statement;
  private LocalDate date_of_creation;

  public Task(String statement) {
    this.statement = statement;
    this.date_of_creation = LocalDate.now();
  }

  public Task(String statement, LocalDate date_of_creation) {
    this.statement = statement;
    this.date_of_creation = date_of_creation;
  }

  public void setStatement(String statement) { this.statement = statement; }

  public String getStatement() {
    return this.statement;
  }

  public LocalDate getDateOfCreation() { return this.date_of_creation; }

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
