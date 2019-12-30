package edu.ponomarev.step.graphics.Edit;

import edu.ponomarev.step.task.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class EditPanel extends JPanel {
  private JButton saveButton;

  private TextField taskNameField;
  private Task currentTask;


  public EditPanel(Task task) {
    super();
    currentTask = task;
    taskNameField = new TextField(currentTask.toString());
    saveButton = new JButton("Save");
  }

  public void run() {
    taskNameField.setText(currentTask.toString());
    this.add(saveButton, BorderLayout.SOUTH);
    this.add(taskNameField, BorderLayout.NORTH);

    this.setBackground(Color.WHITE);
    this.setVisible(true);
  }

  public void setSaveButtonListener(ActionListener listener) {
    saveButton.addActionListener(listener);
  }
}
