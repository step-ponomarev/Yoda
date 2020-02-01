package edu.ponomarev.step.MVC.model.repository.task;

import edu.ponomarev.step.MVC.model.repository.Repository;
import edu.ponomarev.step.MVC.model.repository.Specification;
import edu.ponomarev.step.MVC.model.component.task.Task;
import edu.ponomarev.step.MVC.model.component.BoxType;
import edu.ponomarev.step.MVC.model.component.task.TaskRelations;

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
    createFileIfNotExists(file);

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
  public void remove(List<Task> tasksToRemove) throws Exception {
    if (tasksToRemove.isEmpty()) {
      return;
    }

    for (var boxType : BoxType.values()) {
      addFileNameToPath(boxType);

      List<Task> tasks = deseriadeserializeAndGetList();
      if (!tasks.isEmpty()) {
        for (var task : tasksToRemove) {
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

    if (!tasks.contains(updatedTask)) {
      tasks.add(updatedTask);
    } else {
      updateTaskInListIfOutdated(updatedTask, tasks);
    }

    serializeAndSaveList(tasks);

    resetPathAndCreateDirIfNotExists();
  }

  @Override
  public void update(List<Task> updatedTasks, Specification specification) throws Exception {
    defineTaskRelationsAndSetUpPath(specification);

    List<Task> tasks = deseriadeserializeAndGetList();

    for (var updatedTask : updatedTasks) {
      if (!tasks.contains(updatedTask)) {
        tasks.add(updatedTask);
      } else {
        updateTaskInListIfOutdated(updatedTask, tasks);
      }
    }

    serializeAndSaveList(tasks);

    resetPathAndCreateDirIfNotExists();
  }

  @Override
  public List<Task> getList(Specification specification) throws Exception {
    defineTaskRelationsAndSetUpPath(specification);

    List<Task> tasks = new ArrayList<>();

    File file = new File(directory);
    createFileIfNotExists(file);

    if (file.length() != 0) {
      FileInputStream is = new FileInputStream(file);
      ObjectInputStream isObj = new ObjectInputStream(is);

      tasks = (List<Task>) isObj.readObject();

      isObj.close();
      is.close();
    }

    resetPathAndCreateDirIfNotExists();

    return tasks;
  }

  private void updateTaskInListIfOutdated(Task updatedTask, List<Task> listToUpdate) {
    Task taskToUpdate = listToUpdate.get(listToUpdate.indexOf(updatedTask));

    final boolean TASK_WAS_UPDATED = !updatedTask.getTimeOfLastChange().isAfter(taskToUpdate.getTimeOfLastChange());

    if (TASK_WAS_UPDATED) {
      taskToUpdate.setStatement(updatedTask.getStatement());
      taskToUpdate.setTTimeOfLastChange(updatedTask.getTimeOfLastChange());
    }
  }

  private List<Task> deseriadeserializeAndGetList() throws Exception {
    List<Task> tasks = new ArrayList<>();

    File file = new File(directory);
    createFileIfNotExists(file);

    if (file.length() != 0) {
      FileInputStream is = new FileInputStream(file);
      ObjectInputStream isObj = new ObjectInputStream(is);

      tasks = (List<Task>) isObj.readObject();
      isObj.close();
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

  private void createFileIfNotExists(File file) throws Exception {
    if (!file.exists()) {
      file.createNewFile();
    }
  }

  private void resetPathAndCreateDirIfNotExists() {
    directory = Paths.get("tasks").toAbsolutePath().toString();

    File file = new File(directory);
    if (!file.exists()) {
      file.mkdir();
    }
  }

  private void addFileNameToPath(BoxType boxType) {
    switch (boxType) {
      case DAY:
        directory += File.separator + "box_today.ser";
        break;

      case WEEK:
        directory +=  File.separator + "box_week.ser";
        break;

      case LATE:
        directory += File.separator + "box_late.ser";
        break;

      default:
        directory += File.separator + "box_inbox.ser";
        break;
    }
  }
}
