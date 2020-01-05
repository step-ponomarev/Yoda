package edu.ponomarev.step.component.task;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import java.io.Serializable;

public class Task implements Comparable<Task>, Serializable {
  private String statement;
  private Calendar date_of_creation;
  private Integer id;

  public Task(String statement) {
    this.statement = statement;
    this.date_of_creation = Calendar.getInstance();
    this.id = 0;
  }

  public Task(String statement, Date date, Integer id) {
    this.date_of_creation = Calendar.getInstance();
    date_of_creation.setTime(date);
    this.id = id;

    this.statement = statement;
  }

  public void setStatement(String statement) { this.statement = statement; }
  public String getStatement() {
    return statement;
  }

  public Integer getId() { return this.id; }
  public void setId(Integer id) { this.id = id; }

  public Date getDate() {
    return date_of_creation.getTime();
  }

  @Override
  public String toString() { return this.statement; }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Task)) return false;
    Task task = (Task) o;
    return ( this.statement.equals(task.statement) && (this.id.equals(task.getId())) );
  }

  @Override
  public int hashCode() {
    return Objects.hash(statement, id);
  }

  @Override
  public int compareTo(Task o) {
    return this.date_of_creation.compareTo(o.date_of_creation);
  }
}
