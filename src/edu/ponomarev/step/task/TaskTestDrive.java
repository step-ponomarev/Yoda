package edu.ponomarev.step.task;

public class TaskTestDrive {
  public static void main(String[] args) {
    TaskContainer tc = new TaskContainer();

    for (Integer i = 24; i > 0; --i) {
      tc.add(new Task(i.toString()));
    }

    tc.show();

    tc.setSortStrategy(TaskContainer.Strategy.STATEMENT);

    tc.sort();

    System.out.println("After sorting");

    tc.show();
  }
}
