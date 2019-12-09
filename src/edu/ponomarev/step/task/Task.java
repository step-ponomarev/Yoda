package edu.ponomarev.step.task;

import edu.ponomarev.step.observer.Observer;
import edu.ponomarev.step.observer.Subject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class Task implements Comparable<Task>, Subject {
  String statement;
  Calendar date_of_creation;

  private ArrayList<Observer> owners;

  public Task(String statement) {
    this.statement = statement;
    this.date_of_creation = Calendar.getInstance();
    this.owners = new ArrayList<Observer>();
  }

  @Override
  public void addObserver(Observer obs) {
    owners.add(obs);
  }

  @Override
  public void removeObserver(Observer obs) {
    owners.remove(obs);
  }

  @Override
  public void noticeAll() {
    for (Observer obs : owners) {
      obs.uptate(this);
    }
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
