package edu.ponomarev.step.test;

import edu.ponomarev.step.MVC.model.component.project.Project;
import edu.ponomarev.step.MVC.model.repository.specification.NoSpecification;
import edu.ponomarev.step.MVC.model.repository.RepositoryFactory;
import edu.ponomarev.step.MVC.model.repository.project.ProjectSerializator;
import edu.ponomarev.step.system.ApplicationConfigure;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;

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
  public void projectShouldBeAddedAndRemovedCorrect() {
    var newProject = new Project("Project");
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

  @Test
  public void projectListShouldBeAddedAndRemoveCorrect() {
    final int PROJECT_AMOUNT = 200;
    final List<Project> newProjects = new ArrayList<>(PROJECT_AMOUNT);
    final NoSpecification noSpecification = new NoSpecification();

    try {
      for (int i = 0; i < PROJECT_AMOUNT; ++i) {
        var newProject = new Project(Integer.toString(i));
        newProjects.add(newProject);
      }

      final var PROJECTS_BEFORE_ADDING = projectSerializator.getList(noSpecification);

      projectSerializator.add(newProjects, noSpecification);

      final var PROJECTS_AFTER_ADDING = projectSerializator.getList(noSpecification);

      final int SIZE_BEFORE_ADDING = PROJECTS_BEFORE_ADDING.size();
      final int SIZE_AFTER_ADDING = PROJECTS_AFTER_ADDING.size();

      final boolean SIZE_WAS_CHANGED_CORRECT = ( SIZE_AFTER_ADDING - SIZE_BEFORE_ADDING == PROJECT_AMOUNT );
      boolean EVERY_PROJECT_WAS_ADDED = true;
      for (var project : newProjects) {
        EVERY_PROJECT_WAS_ADDED &= PROJECTS_AFTER_ADDING.contains(project);
      }

      Assert.assertTrue(SIZE_WAS_CHANGED_CORRECT);
      Assert.assertTrue(EVERY_PROJECT_WAS_ADDED);

      projectSerializator.remove(newProjects);

      final var PROJECTS_AFTER_REMOVING = projectSerializator.getList(noSpecification);

      Assert.assertEquals(PROJECTS_BEFORE_ADDING, PROJECTS_AFTER_REMOVING);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void projectShouldBeUpdatedCorrect() {
    final String projectName = "Project";
    final String newProjectName = "New Project";

    final var newProject = new Project(projectName);
    final var noSpecification = new NoSpecification();

    try {
      projectSerializator.add(newProject, noSpecification);

      final var projectsBeforeUpdate = projectSerializator.getList(noSpecification);

      final var projectBeforeUpdate = projectsBeforeUpdate.get(projectsBeforeUpdate.indexOf(newProject));

      newProject.setName(newProjectName);
      newProject.updateTimeOfLastChange();

      projectSerializator.update(newProject, noSpecification);

      final var projectsAfterUpdate = projectSerializator.getList(noSpecification);

      final var projectAfterUpdate = projectsAfterUpdate.get(projectsBeforeUpdate.indexOf(newProject));

      final boolean PROJECT_NAME_WAS_CHANGED = !projectBeforeUpdate.getName().equals(projectAfterUpdate.getName());

      projectSerializator.remove(newProject, noSpecification);

      Assert.assertEquals(projectsBeforeUpdate, projectsAfterUpdate);
      Assert.assertTrue(PROJECT_NAME_WAS_CHANGED);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void projectListShouldBeUpdatedCorrect() {
    final int PROJECT_AMOUNT = 200;
    final List<Project> newProjects = new ArrayList<>(PROJECT_AMOUNT);
    final NoSpecification noSpecification = new NoSpecification();

    try {
      for (int i = 0; i < PROJECT_AMOUNT; ++i) {
        var newProject = new Project(Integer.toString(i));
        newProjects.add(newProject);
      }

      projectSerializator.add(newProjects, noSpecification);

      final var projectsBeforeUpdate = projectSerializator.getList(noSpecification);

      for (var projectToUpdate : newProjects) {
        projectToUpdate.setName(projectToUpdate.getUuid() + " Updated");
        projectToUpdate.updateTimeOfLastChange();
      }

      projectSerializator.update(newProjects, noSpecification);

      final var projectsAfterUpdate = projectSerializator.getList(noSpecification);

      boolean EVERY_NAME_WAS_UPDATED = true;
      for (var project : projectsBeforeUpdate) {
        final var updatedProject = projectsAfterUpdate.get(projectsAfterUpdate.indexOf(project));

        EVERY_NAME_WAS_UPDATED &= !updatedProject.getName().equals(project.getName());
      }

      projectSerializator.remove(newProjects);

      Assert.assertEquals(projectsBeforeUpdate, projectsAfterUpdate);
      Assert.assertTrue(EVERY_NAME_WAS_UPDATED);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
