package edu.ponomarev.step.boxes;

import edu.ponomarev.step.observer.Observer;
import edu.ponomarev.step.observer.Subject;
import edu.ponomarev.step.task.Task;
import edu.ponomarev.step.task.TaskContainer;

import java.util.ArrayList;

public class Box implements TaskStorage, Observer {
  private TaskContainer tasks;
  private ArrayList<Subject> subjects;

  public Box() {
    this.tasks = new TaskContainer();
    this.subjects = new ArrayList<Subject>();
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
}
