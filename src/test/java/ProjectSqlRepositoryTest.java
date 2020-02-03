import edu.ponomarev.step.MVC.model.repository.specification.NoSpecification;
import edu.ponomarev.step.MVC.model.repository.RepositoryFactory;
import edu.ponomarev.step.MVC.model.repository.RepositoryFactory.RepositoryType;
import edu.ponomarev.step.MVC.model.repository.Specification;
import edu.ponomarev.step.MVC.model.repository.project.ProjectSqlRepository;

import edu.ponomarev.step.MVC.model.component.project.Project;
import edu.ponomarev.step.system.ApplicationConfigure;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProjectSqlRepositoryTest {
  private static final int PROJECT_AMOUNT = 10;

  private static RepositoryFactory repositoryFactory;

  private ProjectSqlRepository projectSqlRepository;

  private HashMap<String, Project> projects;

  @BeforeClass
  public static void setRepository() {
    var context = new AnnotationConfigApplicationContext(ApplicationConfigure.class);
    repositoryFactory = context.getBean("repositoryFactory", RepositoryFactory.class);
  }

  @Before
  public void beforeTest() {
    projectSqlRepository = (ProjectSqlRepository) repositoryFactory.getRepository(RepositoryType.PROJECT_SQL);

    projects = new HashMap<>();
    for (int i = 0; i < PROJECT_AMOUNT; ++i) {
      Project project = new Project(Integer.toString(i));
      projects.put(project.getUuid(), project);
    }
  }

  @Test
  public void projectShouldBeAddedAndRemovedCorrect() {
    try {
      final int SIZE_BEFORE_ADDING = projectSqlRepository.getList(null).size();
      final Specification noSpecification = new NoSpecification();

      for (var projectEntry : projects.entrySet()) {
        Project project = projectEntry.getValue();

        projectSqlRepository.add(project, noSpecification);
      }

      final int SIZE_AFTER_ADDING = projectSqlRepository.getList(noSpecification).size();

      var listAfterAdding = projectSqlRepository.getList(noSpecification);
      boolean EVERY_TASK_WAS_ADDED = true;
      for (var projectEntry : projects.entrySet()) {
        Project project = projectEntry.getValue();
        EVERY_TASK_WAS_ADDED &= listAfterAdding.contains(project);
      }

      Assert.assertTrue(EVERY_TASK_WAS_ADDED);
      Assert.assertTrue( (SIZE_AFTER_ADDING - SIZE_BEFORE_ADDING) == PROJECT_AMOUNT );

      for (var project : projects.entrySet()) {
        projectSqlRepository.remove(project.getValue(), noSpecification);
      }

      final int SIZE_AFTER_REMOVING = projectSqlRepository.getList(noSpecification).size();

      boolean EVERY_TASK_WAS_REMOVED = true;
      for (var project : projects.entrySet()) {
        EVERY_TASK_WAS_REMOVED &= !projectSqlRepository.getList(noSpecification).contains(project.getValue());
      }

      Assert.assertTrue(EVERY_TASK_WAS_REMOVED);
      Assert.assertEquals(SIZE_BEFORE_ADDING, SIZE_AFTER_REMOVING);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void projectListShouldBeAddedAndRemovedCorrect() {
    try {
      final int SIZE_BEFORE_ADDING = projectSqlRepository.getList(null).size();
      final Specification noSpecification = new NoSpecification();

      List<Project> projects = new ArrayList<>();
      for (var projectEntry : this.projects.entrySet()) {
        Project project = projectEntry.getValue();

        projects.add(project);
      }

      projectSqlRepository.add(projects, noSpecification);

      var projectsAfterAdding = projectSqlRepository.getList(noSpecification);

      final int SIZE_AFTER_ADDING = projectsAfterAdding.size();

      boolean TASK_LIST_WAS_ADDED = true;
      for (var project : projects) {
        TASK_LIST_WAS_ADDED &= projectsAfterAdding.contains(project);
      }

      Assert.assertTrue(TASK_LIST_WAS_ADDED);
      Assert.assertTrue( (SIZE_AFTER_ADDING - SIZE_BEFORE_ADDING) == PROJECT_AMOUNT );

      projectSqlRepository.remove(projects);

      List<Project> projectsAfterRemoving = projectSqlRepository.getList(noSpecification);
      final int SIZE_AFTER_REMOVING = projectsAfterRemoving.size();

      boolean EVERY_TASK_WAS_REMOVED = true;
      for (var project : projects) {
        EVERY_TASK_WAS_REMOVED &= !projectsAfterRemoving.contains(project);
      }

      Assert.assertTrue(EVERY_TASK_WAS_REMOVED);
      Assert.assertEquals(SIZE_BEFORE_ADDING, SIZE_AFTER_REMOVING);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void projectShouldBeUpdatedCorrect() {
    try {
      final String name = "New Project";
      final String newName = "New Statement";
      final Specification noSpecification = new NoSpecification();

      Project project = new Project(name);

      projectSqlRepository.add(project, noSpecification);

      Project projectBeforeUpdate =
          projectSqlRepository.getList(noSpecification).get(projectSqlRepository.getList(noSpecification).indexOf(project));

      project.setName(newName);
      project.updateTimeOfLastChange();

      projectSqlRepository.update(project, noSpecification);

      Project projectAfterUpdate = projectSqlRepository.getList(noSpecification).get(projectSqlRepository.getList(noSpecification).indexOf(project));

      projectSqlRepository.remove(project, noSpecification);

      Assert.assertEquals(projectBeforeUpdate, projectAfterUpdate);
      Assert.assertEquals(projectBeforeUpdate.getName(), name);
      Assert.assertEquals(projectAfterUpdate.getName(), newName);
      Assert.assertNotEquals(projectAfterUpdate.getName(), projectBeforeUpdate.getName());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
