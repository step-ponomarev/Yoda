package edu.ponomarev.step.MVC.model.repository.task;

import edu.ponomarev.step.MVC.model.component.task.Task;
import edu.ponomarev.step.MVC.model.repository.QueueRepository;
import edu.ponomarev.step.MVC.model.repository.Specification;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TaskQueueRepository implements QueueRepository<Task> {
  private String directory;

  public TaskQueueRepository() {
    setUpPath();
  }

  @Override
  public void push(List<Task> tasks, Specification specification) throws Exception {
    directory += File.separator + specification.getSpecification();
    File file = new File(directory);
    createFileIfNotExists(file);

    serializeAndSaveList(tasks);

    setUpPath();
  }

  @Override
  public List<Task> get(Specification specification) throws Exception {
    directory += File.separator + specification.getSpecification();

    var list = deseriadeserializeAndGetList();

    setUpPath();

    return list;
  }

  private void serializeAndSaveList(List<Task> tasks) throws Exception {
    File file = new File(directory);

    FileOutputStream fostream = new FileOutputStream(file);
    ObjectOutputStream objOstream = new ObjectOutputStream(fostream);

    objOstream.writeObject(tasks);

    objOstream.close();
    fostream.close();
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

  private void createFileIfNotExists(File file) throws Exception {
    if (!file.exists()) {
      file.createNewFile();
    }
  }

  private void setUpPath() {
    directory = Paths.get("queues").toAbsolutePath().toString();
    File file = new File(directory);
    if (!file.exists()) {
      file.mkdir();
    }
  }
}
