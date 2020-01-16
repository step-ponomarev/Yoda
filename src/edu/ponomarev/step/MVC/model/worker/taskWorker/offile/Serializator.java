package edu.ponomarev.step.MVC.model.worker.taskWorker.offile;

import edu.ponomarev.step.MVC.model.worker.TaskWorker;
import edu.ponomarev.step.component.task.InformatedTask;
import edu.ponomarev.step.component.task.Task;
import edu.ponomarev.step.component.taskContainer.TaskContainer;
import edu.ponomarev.step.component.taskContainer.TermTaskContainer;

import java.io.*;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Queue;

public class Serializator implements TaskWorker {
  private String directory;

  public Serializator() {
    this.directory = Paths.get("data").toAbsolutePath().toString();
  }

  @Override
  public void push(InformatedTask task) throws Exception {
    String dir = directory;

    File file = new File(directory);
    if (!file.exists()) {
      file.mkdir();
    }

    dir = addFileNameToPath(task.getContainerType(), dir);

    ArrayList<Task> tasks;
    file = new File(dir);
    if (!file.exists()) {
      file.createNewFile();
      tasks = new ArrayList<Task>();
    } else {
      FileInputStream is = new FileInputStream(file);
      ObjectInputStream isObj = new ObjectInputStream(is);

      tasks = (ArrayList<Task>) isObj.readObject();
      tasks.add(task);

      is.close();
    }

    FileOutputStream os = new FileOutputStream(file);
    ObjectOutputStream osObj = new ObjectOutputStream(os);

    osObj.writeObject(tasks);


    osObj.close();
    os.close();
  }

  @Override
  public void push(TermTaskContainer container) throws Exception {
    String dir = directory;

    File file = new File(directory);
    if (!file.exists()) {
      file.mkdir();
    }

    dir = addFileNameToPath(container.getContainerType(), dir);

    file = new File(dir);
    if (!file.exists()) {
      file.createNewFile();
    }

    FileOutputStream os = new FileOutputStream(file);
    ObjectOutputStream osObj = new ObjectOutputStream(os);

    osObj.writeObject(container.getList());

    osObj.close();
    os.close();
  }

  @Override
  public void remove(InformatedTask tasks) {}

  @Override
  public void remove(Queue<InformatedTask> tasks) {}

  @Override
  public TaskContainer getContainer(TermTaskContainer.ContainerType containerTask) throws Exception {
    String dir = directory;

    File file = new File(directory);
    if (!file.exists()) {
      return (new TermTaskContainer(containerTask));
    }

    dir = addFileNameToPath(containerTask, dir);

    file = new File(dir);
    if (!file.exists()) {
      return (new TermTaskContainer(containerTask));
    }

    FileInputStream is = new FileInputStream(file);
    ObjectInputStream isObj = new ObjectInputStream(is);

    TermTaskContainer tasks = (TermTaskContainer) isObj.readObject();

    isObj.close();
    is.close();

    return tasks;
  }

  private String addFileNameToPath(TermTaskContainer.ContainerType containerType, String path) {
    switch (containerType) {
      case DAY:
        path = Paths.get(directory + "/box_today.ser").toAbsolutePath().toString();
        break;

      case WEEK:
        path = Paths.get(directory + "/box_week.ser").toAbsolutePath().toString();
        break;

      case LATE:
        path = Paths.get(directory + "/box_late.ser").toAbsolutePath().toString();
        break;

      default:
        path = Paths.get(directory + "/box_inbox.ser").toAbsolutePath().toString();
        break;
    }

    return path;
  }
}
