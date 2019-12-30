package edu.ponomarev.step.graphics.Edit;

import edu.ponomarev.step.task.Task;

import javax.swing.*;
import java.awt.*;

public class EditPanel extends JPanel {
  JButton saveButton;

  private TextField taskNameField;
  private Task currentTask;


  public EditPanel(Task task) {
    super();
    currentTask = task;
    taskNameField = new TextField(currentTask.toString());
    saveButton = new JButton("Save");
  }

  public void run() {
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    this.add(saveButton);
    this.add(taskNameField);


    this.setVisible(true);
  }
}
