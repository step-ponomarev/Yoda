package edu.ponomarev.step.MVC.model.repository.task;

import edu.ponomarev.step.MVC.model.repository.Repository;
import edu.ponomarev.step.MVC.model.repository.Specification;
import edu.ponomarev.step.component.task.Task;
import edu.ponomarev.step.component.BoxType;
import edu.ponomarev.step.component.task.TaskRelations;

import java.io.*;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TaskSerializator implements Repository<Task> {
  private String directory;

  public TaskSerializator() {
    resetPathAndCreateDirIfNotExists();
  }

  @Override
  public void add(Task task, Specification specification) throws Exception {
    defineTaskRelationsAndSetUpPath(specification);

    List<Task> tasks = deseriadeserializeAndGetList();
    tasks.add(task);

    serializeAndSaveList(tasks);

    resetPathAndCreateDirIfNotExists();
  }

  @Override
  public void add(List<Task> tasks, Specification specification) throws Exception {
    defineTaskRelationsAndSetUpPath(specification);

    File file = new File(directory);
    if (!file.exists()) {
      file.createNewFile();
    }

    serializeAndSaveList(tasks);

    resetPathAndCreateDirIfNotExists();
  }

  @Override
  public void remove(Task task, Specification specification) throws Exception {
    defineTaskRelationsAndSetUpPath(specification);

    List<Task> tasks = deseriadeserializeAndGetList();

    tasks.remove(task);

    serializeAndSaveList(tasks);

    resetPathAndCreateDirIfNotExists();
  }

  @Override
  public void remove(List<Task> removedTasks) throws Exception {
    if (removedTasks.isEmpty()) {
      return;
    }

    for (var boxType : BoxType.values()) {
      addFileNameToPath(boxType);

      List<Task> tasks = deseriadeserializeAndGetList();
      if (!tasks.isEmpty()) {
        for (var task : removedTasks) {
          tasks.remove(task);
        }
      }

      serializeAndSaveList(tasks);

      resetPathAndCreateDirIfNotExists();
    }
  }

  @Override
  public void update(Task updatedTask, Specification specification) throws Exception {
    defineTaskRelationsAndSetUpPath(specification);

    List<Task> tasks = deseriadeserializeAndGetList();

    putTaskIn(updatedTask, tasks);

    serializeAndSaveList(tasks);

    resetPathAndCreateDirIfNotExists();
  }

  @Override
  public void update(List<Task> updatedTasks, Specification specification) throws Exception {
    defineTaskRelationsAndSetUpPath(specification);

    List<Task> tasks = deseriadeserializeAndGetList();

    for (var updatedTask : updatedTasks) {
      putTaskIn(updatedTask, tasks);
    }

    serializeAndSaveList(tasks);

    resetPathAndCreateDirIfNotExists();
  }

  @Override
  public List<Task> getList(Specification specification) throws Exception {
    defineTaskRelationsAndSetUpPath(specification);

    List<Task> tasks = new ArrayList<>();

    File file = new File(directory);
    if (file.exists()) {
      FileInputStream is = new FileInputStream(file);
      ObjectInputStream isObj = new ObjectInputStream(is);

      tasks = (List<Task>) isObj.readObject();

      isObj.close();
      is.close();
    }

    resetPathAndCreateDirIfNotExists();

    return tasks;
  }

  private void putTaskIn(Task updatedTask, List<Task> tasks) {
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

  private List<Task> deseriadeserializeAndGetList() throws Exception {
    List<Task> tasks = new ArrayList<>();

    File file = new File(directory);
    if (!file.exists()) {
      file.createNewFile();
      return tasks;
    } else {
      FileInputStream is = new FileInputStream(file);
      ObjectInputStream isObj = new ObjectInputStream(is);

      tasks = (List<Task>) isObj.readObject();
      is.close();
    }

    return tasks;
  }

  private void serializeAndSaveList(List<Task> tasks) throws Exception {
    File file = new File(directory);

    FileOutputStream fostream = new FileOutputStream(file);
    ObjectOutputStream objOstream = new ObjectOutputStream(fostream);

    objOstream.writeObject(tasks);

    objOstream.close();
    fostream.close();
  }

  private void defineTaskRelationsAndSetUpPath(Specification specification) {
    final var taskRelations = (TaskRelations) specification.getSpecification();

    addFileNameToPath(taskRelations.getBoxType());
  }

  private void resetPathAndCreateDirIfNotExists() {
    directory = Paths.get("data").toAbsolutePath().toString();

    File file = new File(directory);
    if (!file.exists()) {
      file.mkdir();
    }
  }

  private void addFileNameToPath(BoxType boxType) {
    switch (boxType) {
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
