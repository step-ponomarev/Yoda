package edu.ponomarev.step.MVC.model.repository.task;

import edu.ponomarev.step.component.task.InformatedTask;
import edu.ponomarev.step.component.task.Task;
import edu.ponomarev.step.component.taskContainer.TaskContainer;
import edu.ponomarev.step.component.taskContainer.TermTaskContainer;

import java.io.*;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class TaskSerializator implements TaskRepository {
  private String directory;

  public TaskSerializator() {
    this.directory = Paths.get("data").toAbsolutePath().toString();
  }

  @Override
  public void add(InformatedTask task) {
    setUpPath(task.getContainerType());

    ArrayList<Task> tasks = digUpTaskList();
    tasks.add(task);

    digInTaskList(tasks);
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
  }

  @Override
  public void remove(InformatedTask task) {
    setUpPath(task.getContainerType());

    ArrayList<Task> tasks = digUpTaskList();
    tasks.remove(task);

    digInTaskList(tasks);
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
  }

  @Override
  public void update(InformatedTask updatedTask) {
    setUpPath(updatedTask.getContainerType());

    ArrayList<Task> tasks = digUpTaskList();

    putTaskIn(updatedTask, tasks);

    digInTaskList(tasks);
  }

  @Override
  public void update(TermTaskContainer updatedTasks) {
    setUpPath(updatedTasks.getContainerType());

    ArrayList<Task> tasks = digUpTaskList();

    for (var updatedTask : updatedTasks.getList()) {
      putTaskIn(updatedTask, tasks);
    }

    digInTaskList(tasks);
  }

  @Override
  public TaskContainer getContainer(TermTaskContainer.ContainerType containerType) {
    setUpPath(containerType);

    File file = new File(directory);
    if (!file.exists()) {
      return (new TermTaskContainer(containerType));
    }

    TermTaskContainer tasks = new TermTaskContainer(containerType);
    try {
      FileInputStream is = new FileInputStream(file);
      ObjectInputStream isObj = new ObjectInputStream(is);

      tasks = (TermTaskContainer) isObj.readObject();

      isObj.close();
      is.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

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

  private void setUpPath(TermTaskContainer.ContainerType containerType) {
    File file = new File(directory);
    if (!file.exists()) {
      file.mkdir();
    }

    addFileNameToPath(containerType);
  }

  private void addFileNameToPath(TermTaskContainer.ContainerType containerType) {
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