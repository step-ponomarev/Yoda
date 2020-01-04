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
    this.directory = Paths.get("").toAbsolutePath().toString() + "\\data";
  }

  @Override
  public void put(DataHandler.BoxType type, Task task) throws Exception {
    String dir = directory;

    File file = new File(directory);
    if (!file.exists()) {
      file.mkdir();
    }

    switch (type) {
      case DAY:
        dir += "\\box_today.ser";
        break;

      case WEEK:
        dir += "\\box_week.ser";
        break;

      case LATE:
        dir += "\\box_late.ser";
        break;

      default:
        dir += "\\box_inbox.ser";
        break;
    }

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
  public void putAll(DataHandler.BoxType type, List<Task> task) throws Exception {
    String dir = directory;

    File file = new File(directory);
    if (!file.exists()) {
      file.mkdir();
    }

    switch (type) {
      case DAY:
        dir += "\\box_today.ser";
        break;

      case WEEK:
        dir += "\\box_week.ser";
        break;

      case LATE:
        dir += "\\box_late.ser";
        break;

      default:
        dir += "\\box_inbox.ser";
        break;
    }

    ArrayList<Task> tasks;

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
  public List pull(DataHandler.BoxType type) throws Exception {
    String dir = directory;

    File file = new File(directory);
    if (!file.exists()) {
      return (new ArrayList<Task>());
    }

    switch (type) {
      case DAY:
        dir += "\\box_today.ser";
        break;

      case WEEK:
        dir += "\\box_week.ser";
        break;

      case LATE:
        dir += "\\box_late.ser";
        break;

      default:
        dir += "\\box_inbox.ser";
        break;
    }

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
}
