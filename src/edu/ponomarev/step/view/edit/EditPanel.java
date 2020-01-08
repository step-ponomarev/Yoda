package edu.ponomarev.step.view.edit;

import edu.ponomarev.step.component.task.Task;

import javax.swing.*;
import java.awt.*;

public class EditPanel extends JPanel {
  private  ButtonPanel buttonPanel;

  private TextField taskNameField;
  private Task currentTask;


  public EditPanel(Task task) {
    super();
    this.currentTask = task;
    this.buttonPanel = new ButtonPanel();
    this.taskNameField = new TextField(currentTask.toString());
  }

  public void run() {
    taskNameField.setText(currentTask.toString());
    this.taskNameField.setBackground(Color.WHITE);

    this.add(taskNameField, BorderLayout.NORTH);
    this.add(buttonPanel, BorderLayout.SOUTH);

    buttonPanel.run();

    this.setBackground(Color.WHITE);
    this.setVisible(true);
  }

  public ButtonPanel getButtonPanel() {
    return buttonPanel;
  }

  public void setButtonPanel(ButtonPanel buttonPanel) {
    this.buttonPanel = buttonPanel;
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
