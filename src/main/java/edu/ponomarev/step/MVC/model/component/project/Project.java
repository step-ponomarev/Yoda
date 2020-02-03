package edu.ponomarev.step.MVC.model.component.project;

import edu.ponomarev.step.MVC.model.component.BoxType;
import edu.ponomarev.step.MVC.model.component.task.Task;
import edu.ponomarev.step.system.TimeManager;

import java.io.Serializable;
import java.time.LocalDateTime;

import java.util.*;

public class Project implements Serializable {
  private String uuid;
  private String name;
  private LocalDateTime timeOfCreation;
  private LocalDateTime timeOfLastChange;

  private HashMap<BoxType, List<Task>> boxes;

  public Project(String name) {
    this.name = name;
    timeOfCreation = TimeManager.getLocalDateTimeOf(LocalDateTime.now());
    timeOfLastChange = timeOfCreation;
    boxes = new HashMap<>();

    setUpBoxes();
    generateUUID();
  }

  public Project(String uuid, String name, LocalDateTime timeOfCreation, LocalDateTime timeOfLastChange) {
    this.uuid = uuid;
    this.name = name;
    this.timeOfCreation = TimeManager.getLocalDateTimeOf(timeOfCreation);
    this.timeOfLastChange = TimeManager.getLocalDateTimeOf(timeOfLastChange);
    boxes = new HashMap<>();

    setUpBoxes();
  }

  public void addTask(Task task, BoxType boxType) {
    var box = boxes.get(boxType);
    box.add(task);
  }

  public void removeTask(Task task) {
    for (var taskBox : boxes.entrySet()) {
      var box = taskBox.getValue();
      box.remove(task);
    }
  }

  public List<Task> getTaskBox(BoxType boxType) {
    var box = boxes.get(boxType);

    return box;
  }

  public String getUuid() {
    return uuid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDateTime getTimeOfCreation() {
    return timeOfCreation;
  }

  public LocalDateTime getTimeOfLastChange() {
    return timeOfLastChange;
  }

  public void setTimeOfLastChange(LocalDateTime timeOfLastChange) {
    this.timeOfLastChange = timeOfLastChange;
  }

  public void updateTimeOfLastChange() {
    this.timeOfLastChange = TimeManager.getLocalDateTimeOf(LocalDateTime.now());
  }

  private void setUpBoxes() {
    boxes.put(BoxType.DAY, new ArrayList<>());
    boxes.put(BoxType.WEEK, new ArrayList<>());
    boxes.put(BoxType.LATE, new ArrayList<>());
  }

  private void generateUUID() {
    UUID uuid = UUID.randomUUID();
    this.uuid = uuid.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Project)) return false;
    Project project = (Project) o;

    return timeOfCreation.equals(project.timeOfCreation)
        && uuid.equals(project.uuid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timeOfCreation, uuid);
  }
}
