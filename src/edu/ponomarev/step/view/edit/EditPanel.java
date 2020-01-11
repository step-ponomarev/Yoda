package edu.ponomarev.step.view.edit;

import edu.ponomarev.step.component.task.Task;

import javax.swing.*;
import java.awt.*;

public class EditPanel extends JPanel {
  private  ButtonPanel buttonPanel;

  private TextField taskNameField;

  private Task currentTask;
  private TaskState taskStateBeforeChanges;


  public EditPanel() {
    super();
    this.buttonPanel = new ButtonPanel();
    this.taskNameField = new TextField();
  }

  public void run() {
    this.taskNameField.setText(currentTask.toString());
    this.taskNameField.setBackground(Color.WHITE);
    this.taskNameField.setSize(new Dimension(200, 24));

    this.add(taskNameField, BorderLayout.NORTH);
    this.add(buttonPanel, BorderLayout.SOUTH);

    this.buttonPanel.run();

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

  public TaskState getTaskStateBeforeChanges() { return taskStateBeforeChanges; }

  public void setTaskStateBeforeChanges(TaskState taskStateBeforeChanges) { this.taskStateBeforeChanges = taskStateBeforeChanges; }
}
