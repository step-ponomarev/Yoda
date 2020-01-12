package edu.ponomarev.step.data.offile;

import edu.ponomarev.step.data.DataWorker;
import edu.ponomarev.step.manager.DataHandler;
import edu.ponomarev.step.component.task.Task;

import java.io.*;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Serializator implements DataWorker {
  private String directory;

  public Serializator() {
    this.directory = Paths.get("data").toAbsolutePath().toString();
  }

  @Override
  public void push(Task task, DataHandler.BoxType type) throws Exception {
    String dir = directory;

    File file = new File(directory);
    if (!file.exists()) {
      file.mkdir();
    }

    dir = addFileNameToPath(type, dir);

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
  public void pushAll(List<Task> task, DataHandler.BoxType type) throws Exception {
    String dir = directory;

    File file = new File(directory);
    if (!file.exists()) {
      file.mkdir();
    }

    dir = addFileNameToPath(type, dir);

    file = new File(dir);
    if (!file.exists()) {
      file.createNewFile();
    }

    FileOutputStream os = new FileOutputStream(file);
    ObjectOutputStream osObj = new ObjectOutputStream(os);

    osObj.writeObject(task);

    osObj.close();
    os.close();
  }

  @Override
  public List getAll(DataHandler.BoxType type) throws Exception {
    String dir = directory;

    File file = new File(directory);
    if (!file.exists()) {
      return (new ArrayList<Task>());
    }

    dir = addFileNameToPath(type, dir);

    file = new File(dir);
    if (!file.exists()) {
      return (new ArrayList<Task>());
    }

    FileInputStream is = new FileInputStream(file);
    ObjectInputStream isObj = new ObjectInputStream(is);

    ArrayList<Task> tasks = (ArrayList<Task>) isObj.readObject();

    isObj.close();
    is.close();

    return tasks;
  }

  private String addFileNameToPath(DataHandler.BoxType type, String path) {
    switch (type) {
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
