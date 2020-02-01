package edu.ponomarev.step.test;

import edu.ponomarev.step.MVC.model.component.project.Project;
import edu.ponomarev.step.MVC.model.repository.RepositoryFactory;
import edu.ponomarev.step.MVC.model.repository.project.ProjectSerializator;
import edu.ponomarev.step.system.ApplicationConfigure;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class ProjectSerializatorTest {
  private static RepositoryFactory repositoryFactory;

  private ProjectSerializator projectSerializator;

  private List<Project> projects;

  @BeforeClass
  public static void beforeClass() {
    var context = new AnnotationConfigApplicationContext(ApplicationConfigure.class);
    repositoryFactory = context.getBean("repositoryFactory", RepositoryFactory.class);
  }

  @Before
  public void beforeTest() {
    projectSerializator = (ProjectSerializator) repositoryFactory.getRepository(RepositoryFactory.RepositoryType.PROJECT_OFFLINE);

    for (int i = 0; i < 100; ++i) {
      projects.add( new Project(Integer.toString(i) ));
    }
  }
}
