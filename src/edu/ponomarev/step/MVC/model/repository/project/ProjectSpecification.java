package edu.ponomarev.step.MVC.model.repository.project;

import edu.ponomarev.step.MVC.model.repository.Specification;
import edu.ponomarev.step.component.project.Project;

public class ProjectSpecification implements Specification<String> {
  private String ProjectUUID;

  public ProjectSpecification(Project project) {
    ProjectUUID = project.getUuid();
  }

  @Override
  public String getSpecification() {
    return ProjectUUID;
  }
}
