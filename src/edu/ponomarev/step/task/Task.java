package edu.ponomarev.step.task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class Task implements Comparable<Task> {
  String statement;
  Calendar date_of_creation;

  public Task(String statement) {
    this.statement = statement;
    this.date_of_creation = Calendar.getInstance();
  }

  @Override
  public String toString() {
    DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    return (statement + " " + dateFormat.format(date_of_creation.getTime()));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Task)) return false;
    Task task = (Task) o;
    return statement.equals(task.statement) &&
        date_of_creation.equals(task.date_of_creation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(statement, date_of_creation);
  }

  @Override
  public int compareTo(Task o) {
    return this.date_of_creation.compareTo(o.date_of_creation);
  }
}
