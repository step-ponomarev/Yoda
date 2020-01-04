package edu.ponomarev.step.view.edit;

import edu.ponomarev.step.component.task.Task;

import javax.swing.*;
import java.awt.*;

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

  public JButton getSaveButton() {
    return saveButton;
  }

  public void setSaveButton(JButton saveButton) {
    this.saveButton = saveButton;
  }

  public TextField getTaskNameField() {
    return taskNameField;
  }

  public void setTaskNameField(TextField taskNameField) {
    this.taskNameField = taskNameField;
  }

  public Task getCurrentTask() {
    return currentTask;
  }

  public void setCurrentTask(Task currentTask) {
    this.currentTask = currentTask;
  }
}
