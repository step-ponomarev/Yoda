package edu.ponomarev.step.MVC.model.repository.project;

import edu.ponomarev.step.MVC.model.component.project.Project;
import edu.ponomarev.step.MVC.model.repository.Repository;
import edu.ponomarev.step.MVC.model.repository.Specification;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ProjectSerializator implements Repository<Project> {
  private String directory;

  public ProjectSerializator() {
    resetPathAndCreateDirIfNotExists();
  }

  @Override
  public void add(Project project, Specification NULL) throws Exception {
    List<Project> projects = deseriadeserializeAndGetList();

    if (!projects.contains(project)) {
      projects.add(project);
    }

    serializeAndSaveList(projects);
  }

  @Override
  public void add(List<Project> newProjects, Specification NULL) throws Exception {
    if (newProjects.isEmpty()) {
      return;
    }

    List<Project> projects = deseriadeserializeAndGetList();

    for (var project : newProjects) {
      if (!projects.contains(project)) {
        projects.add(project);
      }
    }

    serializeAndSaveList(projects);
  }

  @Override
  public void remove(Project project, Specification NULL) throws Exception {
    List<Project> projects = deseriadeserializeAndGetList();

    projects.remove(project);

    serializeAndSaveList(projects);
  }

  @Override
  public void remove(List<Project> projectsToRemove) throws Exception {
    if (projectsToRemove.isEmpty()) {
      return;
    }

    List<Project> projects = deseriadeserializeAndGetList();

    projects.removeAll(projectsToRemove);

    serializeAndSaveList(projects);
  }

  @Override
  public void update(Project updatedProject, Specification NULL) throws Exception {
    List<Project> projects = deseriadeserializeAndGetList();

    if (!projects.contains(updatedProject)) {
      projects.add(updatedProject);
    } else {
      updateProjectInListIfOutdated(updatedProject, projects);
    }

    serializeAndSaveList(projects);
  }

  @Override
  public void update(List<Project> updatedProjects, Specification specification) throws Exception {
    List<Project> projects = deseriadeserializeAndGetList();

    for (var updatedProject : updatedProjects) {
      if (!projects.contains(updatedProject)) {
        projects.add(updatedProject);
      } else {
        updateProjectInListIfOutdated(updatedProject, projects);
      }
    }

    serializeAndSaveList(projects);
  }

  @Override
  public List<Project> getList(Specification NULL) throws Exception {
    return deseriadeserializeAndGetList();
  }

  private List<Project> deseriadeserializeAndGetList() throws Exception {
    List<Project> projects = new ArrayList<>();

    File file = new File(directory);
    createFileIfNotExists(file);

    if (file.length() != 0) {
      FileInputStream inputStream = new FileInputStream(file);
      ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

      projects = (List<Project>) objectInputStream.readObject();

      objectInputStream.close();
      inputStream.close();
    }

    return projects;
  }

  private void serializeAndSaveList(List<Project> projects) throws Exception {
    File file = new File(directory);
    createFileIfNotExists(file);

    FileOutputStream fileOutputStream = new FileOutputStream(file);
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

    objectOutputStream.writeObject(projects);

    objectOutputStream.close();
    fileOutputStream.close();
  }

  private void updateProjectInListIfOutdated(Project updatedProject, List<Project> listToUpdate) {
    Project projectToUpdate = listToUpdate.get(listToUpdate.indexOf(updatedProject));

    final boolean PROJECT_IS_OUTDATED = updatedProject.getTimeOfLastChange().isAfter(projectToUpdate.getTimeOfLastChange());

    if (PROJECT_IS_OUTDATED) {
      projectToUpdate.setName(updatedProject.getName());
      projectToUpdate.setTimeOfLastChange(projectToUpdate.getTimeOfLastChange());
    }
  }

  private void createFileIfNotExists(File file) throws Exception {
    if (!file.exists()) {
      file.createNewFile();
    }
  }

  private void resetPathAndCreateDirIfNotExists() {
    directory = Paths.get("projects").toAbsolutePath().toString();

    File file = new File(directory);
    if (!file.exists()) {
      file.mkdir();
    }

    directory += File.separator + "projects.ser";
  }
}
