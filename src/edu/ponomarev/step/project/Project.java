package edu.ponomarev.step.project;

import edu.ponomarev.step.observer.Observer;
import edu.ponomarev.step.observer.Subject;
import edu.ponomarev.step.task.Task;
import edu.ponomarev.step.task.TaskContainer;
import edu.ponomarev.step.boxes.TaskStorage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class Project implements Observer, TaskStorage {
  private String name;
  private Calendar date_of_creation;
  private TaskContainer tasks;
  private ProjectStatus status;

  //TODO: Нужно сделать отписку от сабджекта с удалением его из сабджектов и удалением этого объекта из рассылки.

  private ArrayList<Subject> subjects;

  public Project(String name) {
    this.name = name;
    this.date_of_creation = Calendar.getInstance();
    this.tasks = new TaskContainer();
    this.status = ProjectStatus.PLANNING;
    this.subjects = new ArrayList<Subject>();
  }

  public void set_status(ProjectStatus status) {
    this.status = status;
  }

  public ProjectStatus get_status() {
    return this.status;
  }

  @Override
  public void add(Task task) {
    this.tasks.add(task);
    this.subjects.add(task);
  }

  @Override
  public void remove(Task task) {
    this.tasks.remove(task);
    this.subjects.remove(task);
  }

  @Override
  public void uptate(Subject obj) {
    if (obj instanceof Task) {
      Task task = (Task) obj;
      remove(task);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Project)) return false;
    Project project = (Project) o;
    return Objects.equals(name, project.name) &&
        Objects.equals(date_of_creation, project.date_of_creation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, date_of_creation);
  }
}
