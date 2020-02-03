package edu.ponomarev.step.MVC.model.repository.project;

import edu.ponomarev.step.MVC.model.component.project.Project;
import edu.ponomarev.step.MVC.model.component.task.Task;
import edu.ponomarev.step.MVC.model.repository.QueueRepository;
import edu.ponomarev.step.MVC.model.repository.Specification;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ProjectQueueRepository implements QueueRepository<Project> {
  private String directory;

  public ProjectQueueRepository() {
    setUpPath();
  }

  @Override
  public void push(List<Project> projects, Specification specification) throws Exception {
    directory += File.separator + specification.getSpecification();

    File file = new File(directory);
    createFileIfNotExists(file);

    serializeAndSaveList(projects);

    setUpPath();
  }

  @Override
  public List<Project> get(Specification specification) throws Exception {
    directory += File.separator + specification.getSpecification();

    var projects = deseriadeserializeAndGetList();

    setUpPath();

    return projects;
  }

  private void serializeAndSaveList(List<Project> projects) throws Exception {
    File file = new File(directory);

    FileOutputStream fostream = new FileOutputStream(file);
    ObjectOutputStream objOstream = new ObjectOutputStream(fostream);

    objOstream.writeObject(projects);

    objOstream.close();
    fostream.close();
  }

  private List<Project> deseriadeserializeAndGetList() throws Exception {
    List<Project> projects = new ArrayList<>();

    File file = new File(directory);
    createFileIfNotExists(file);

    if (file.length() != 0) {
      FileInputStream is = new FileInputStream(file);
      ObjectInputStream isObj = new ObjectInputStream(is);

      projects = (List<Project>) isObj.readObject();
      isObj.close();
      is.close();
    }

    return projects;
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
