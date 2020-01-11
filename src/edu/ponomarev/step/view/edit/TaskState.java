package edu.ponomarev.step.view.edit;

import edu.ponomarev.step.component.task.Task;

public class TaskState {
  private String statement;

  public TaskState(final Task task) {
    this.statement = task.getStatement();
  }

  public boolean isEquals(Task task) {
    return task.getStatement().equals(this.statement);
  }

  public String getStatement() {
    return statement;
  }

  public void setStatement(String statement) {
    this.statement = statement;
  }
}
