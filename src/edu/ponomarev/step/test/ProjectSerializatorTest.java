package edu.ponomarev.step.test;

import edu.ponomarev.step.MVC.model.component.project.Project;
import edu.ponomarev.step.MVC.model.repository.NoSpecification;
import edu.ponomarev.step.MVC.model.repository.RepositoryFactory;
import edu.ponomarev.step.MVC.model.repository.project.ProjectSerializator;
import edu.ponomarev.step.system.ApplicationConfigure;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ProjectSerializatorTest {
  private static RepositoryFactory repositoryFactory;

  private ProjectSerializator projectSerializator;

  @BeforeClass
  public static void beforeClass() {
    var context = new AnnotationConfigApplicationContext(ApplicationConfigure.class);
    repositoryFactory = context.getBean("repositoryFactory", RepositoryFactory.class);
  }

  @Before
  public void beforeTest() {
    projectSerializator = (ProjectSerializator) repositoryFactory.getRepository(RepositoryFactory.RepositoryType.PROJECT_OFFLINE);
  }

  @Test
  public void taskShouldBeAddedAndRemovedCorrect() {
    var newProject = new Project("New Project!");
    var noSpecification = new NoSpecification();

    try {
      var projectsBeforeAdding = projectSerializator.getList(noSpecification);

      projectSerializator.add(newProject, noSpecification);

      var projectsAfterAdding = projectSerializator.getList(noSpecification);

      final int SIZE_BEFORE_ADDING = projectsBeforeAdding.size();
      final int SIZE_AFTER_ADDING = projectsAfterAdding.size();


      final boolean PROJECT_WAS_ADDED = projectsAfterAdding.contains(newProject)
          && ( SIZE_AFTER_ADDING - SIZE_BEFORE_ADDING == 1 );

      Assert.assertNotEquals(SIZE_BEFORE_ADDING, SIZE_AFTER_ADDING);
      Assert.assertTrue(PROJECT_WAS_ADDED);

      projectSerializator.remove(newProject, noSpecification);

      var projectAfterRemoving = projectSerializator.getList(noSpecification);

      final int SIZE_AFTER_REMOVING = projectAfterRemoving.size();
      final boolean PROJECT_WAS_REMOVED = !projectAfterRemoving.contains(newProject)
          && ( SIZE_AFTER_REMOVING == SIZE_BEFORE_ADDING );

      Assert.assertTrue(PROJECT_WAS_REMOVED);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
