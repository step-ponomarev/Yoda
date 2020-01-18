package edu.ponomarev.step.MVC.model.repository.task;

import edu.ponomarev.step.component.task.InformatedTask;
import edu.ponomarev.step.component.task.Task;
import edu.ponomarev.step.component.taskContainer.TaskContainer;
import edu.ponomarev.step.component.taskContainer.termContainer.TermTaskContainer;
import edu.ponomarev.step.component.taskContainer.termContainer.ContainerVariable.ContainerType;

import java.io.*;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class TaskSerializator implements TaskRepository {
  private String directory;

  public TaskSerializator() {
    resetPath();
  }

  @Override
  public void add(InformatedTask task) {
    setUpPath(task.getContainerType());

    ArrayList<Task> tasks = digUpTaskList();
    tasks.add(task);

    digInTaskList(tasks);

    resetPath();
  }

  @Override
  public void add(TermTaskContainer container) {
    setUpPath(container.getContainerType());

    try {
      File file = new File(directory);
      if (!file.exists()) {
        file.createNewFile();
      }

      digInTaskList(container.getList());
    } catch (Exception e) {
      e.printStackTrace();
    }

    resetPath();
  }

  @Override
  public void remove(InformatedTask task) {
    setUpPath(task.getContainerType());

    ArrayList<Task> tasks = digUpTaskList();
    tasks.remove(task);

    digInTaskList(tasks);

    resetPath();
  }

  @Override
  public void remove(Queue<InformatedTask> removedTasks) {
    if (removedTasks.isEmpty()) {
      return;
    }

    setUpPath(removedTasks.peek().getContainerType());

    ArrayList<Task> tasks = digUpTaskList();
    for (var removedTask : removedTasks) {
      tasks.remove(removedTask);
    }

    digInTaskList(tasks);

    resetPath();
  }

  @Override
  public void update(InformatedTask updatedTask) {
    setUpPath(updatedTask.getContainerType());

    ArrayList<Task> tasks = digUpTaskList();

    putTaskIn(updatedTask, tasks);

    digInTaskList(tasks);

    resetPath();
  }

  @Override
  public void update(TermTaskContainer updatedTasks) {
    setUpPath(updatedTasks.getContainerType());

    ArrayList<Task> tasks = digUpTaskList();

    for (var updatedTask : updatedTasks.getList()) {
      putTaskIn(updatedTask, tasks);
    }

    digInTaskList(tasks);

    resetPath();
  }

  @Override
  public TaskContainer getContainer(ContainerType containerType) {
    setUpPath(containerType);

    TermTaskContainer container = new TermTaskContainer(containerType);

    File file = new File(directory);
    if (!file.exists()) {
      return container;
    }

    ArrayList<Task> tasks = new ArrayList<>();
    try {
      FileInputStream is = new FileInputStream(file);
      ObjectInputStream isObj = new ObjectInputStream(is);

      tasks = (ArrayList<Task>) isObj.readObject();

      container.setList(tasks);

      isObj.close();
      is.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    resetPath();

    return container;
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

  private ArrayList<Task> digUpTaskList() {
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

  private void digInTaskList(List<Task> tasks) {
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
