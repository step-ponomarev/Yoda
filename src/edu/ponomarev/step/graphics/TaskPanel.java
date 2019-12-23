package edu.ponomarev.step.graphics;

import edu.ponomarev.step.task.Task;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TaskPanel extends JPanel {
  private ArrayList tasks;
  public TaskPanel() {
    super();
    tasks = new ArrayList<Task>();
  }

  public void run() {
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.setBackground(new Color(255, 255, 255));
    this.setVisible(true);
  }

  public void setList(ArrayList<Task> tasks) {
    this.tasks = tasks;
  }
}
