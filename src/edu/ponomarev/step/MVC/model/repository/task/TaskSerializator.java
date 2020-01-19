package edu.ponomarev.step.MVC.model.repository.task;

import edu.ponomarev.step.MVC.model.repository.Repository;
import edu.ponomarev.step.MVC.model.repository.Specification;
import edu.ponomarev.step.component.task.Task;
import edu.ponomarev.step.component.taskContainer.termContainer.ContainerType;

import java.io.*;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TaskSerializator implements Repository<Task> {
  private String directory;

  public TaskSerializator() {
    resetPath();
  }

  @Override
  public void add(Task task, Specification specification) {
    setUpPath( (ContainerType) specification.getSerialisationSpecification());

    ArrayList<Task> tasks = deseriadeserializeAndGetList();
    tasks.add(task);

    serializeAndSaveList(tasks);

    resetPath();
  }

  @Override
  public void add(List<Task> tasks, Specification specification) {
    setUpPath( (ContainerType) specification.getSerialisationSpecification());

    try {
      File file = new File(directory);
      if (!file.exists()) {
        file.createNewFile();
      }

      serializeAndSaveList(tasks);
    } catch (Exception e) {
      e.printStackTrace();
    }

    resetPath();
  }

  @Override
  public void remove(Task task, Specification specification) {
    setUpPath( (ContainerType) specification.getSerialisationSpecification());

    ArrayList<Task> tasks = deseriadeserializeAndGetList();
    tasks.remove(task);

    serializeAndSaveList(tasks);

    resetPath();
  }

  @Override
  // Never used.
  public void remove(List<Task> removedTasks, Specification specification) {}

  @Override
  public void update(Task updatedTask, Specification specification) {
    setUpPath( (ContainerType) specification.getSerialisationSpecification());

    ArrayList<Task> tasks = deseriadeserializeAndGetList();

    putTaskIn(updatedTask, tasks);

    serializeAndSaveList(tasks);

    resetPath();
  }

  @Override
  public void update(List<Task> updatedTasks, Specification specification) {
    setUpPath( (ContainerType) specification.getSerialisationSpecification());

    ArrayList<Task> tasks = deseriadeserializeAndGetList();

    for (var updatedTask : updatedTasks) {
      putTaskIn(updatedTask, tasks);
    }

    serializeAndSaveList(tasks);

    resetPath();
  }

  @Override
  public List<Task> getList(Specification specification) {
    setUpPath( (ContainerType) specification.getSerialisationSpecification());

    ArrayList<Task> tasks = new ArrayList<>();

    File file = new File(directory);
    if (!file.exists()) {
      return tasks;
    }

    try {
      FileInputStream is = new FileInputStream(file);
      ObjectInputStream isObj = new ObjectInputStream(is);

      tasks = (ArrayList<Task>) isObj.readObject();

      isObj.close();
      is.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    resetPath();

    return tasks;
  }

  private void putTaskIn(Task updatedTask, ArrayList<Task> tasks) {
    if (!tasks.contains(updatedTask)) {
      tasks.add(updatedTask);
    } else {
      Task taskToUpdate = tasks.get(tasks.indexOf(updatedTask));

      final boolean TASK_WAS_UPDATED = updatedTask.getTimeOfLastChange().isAfter(taskToUpdate.getTimeOfLastChange());

      if (TASK_WAS_UPDATED) {
        taskToUpdate.setStatement(updatedTask.getStatement());
        taskToUpdate.setTTimeOfLastChange(updatedTask.getTimeOfLastChange());
      }
    }
  }

  private ArrayList<Task> deseriadeserializeAndGetList() {
    ArrayList<Task> tasks = new ArrayList<>();
    try {
      File file = new File(directory);
      if (!file.exists()) {
        file.createNewFile();
        return tasks;
      } else {
        FileInputStream is = new FileInputStream(file);
        ObjectInputStream isObj = new ObjectInputStream(is);

        tasks = (ArrayList<Task>) isObj.readObject();
        is.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return tasks;
  }

  private void serializeAndSaveList(List<Task> tasks) {
    File file = new File(directory);

    try {
      FileOutputStream fostream = new FileOutputStream(file);
      ObjectOutputStream objOstream = new ObjectOutputStream(fostream);

      objOstream.writeObject(tasks);

      objOstream.close();
      fostream.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void setUpPath(ContainerType containerType) {
    File file = new File(directory);
    if (!file.exists()) {
      file.mkdir();
    }

    addFileNameToPath(containerType);
  }

  private void resetPath() {
    directory = Paths.get("data").toAbsolutePath().toString();
  }

  private void addFileNameToPath(ContainerType containerType) {
    switch (containerType) {
      case DAY:
        directory = Paths.get(directory + "/box_today.ser").toAbsolutePath().toString();
        break;

      case WEEK:
        directory = Paths.get(directory + "/box_week.ser").toAbsolutePath().toString();
        break;

      case LATE:
        directory = Paths.get(directory + "/box_late.ser").toAbsolutePath().toString();
        break;

      default:
        directory = Paths.get(directory + "/box_inbox.ser").toAbsolutePath().toString();
        break;
    }
  }
}
