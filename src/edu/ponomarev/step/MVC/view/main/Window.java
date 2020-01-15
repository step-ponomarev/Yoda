package edu.ponomarev.step.MVC.view.main;

import edu.ponomarev.step.MVC.view.edit.EditPanel;
import edu.ponomarev.step.MVC.view.taskPanel.TaskPanel;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
  private TextPanel textPanel;
  private ButtonPanel buttonPanel;
  private TaskPanel taskPanel;
  private EditPanel editPanel;

  public Window(String applicationName) {
    super(applicationName);

    this.textPanel = new TextPanel();
    this.taskPanel = new TaskPanel();
    this.buttonPanel = new ButtonPanel();
    this.editPanel = new EditPanel();
  }

  public void run() {
    this.textPanel.run();
    this.buttonPanel.run();

    repaintMainWindow();

    this.setSize(500, 500);
    this.getContentPane().setBackground(new Color(255, 255, 255));
    this.setVisible(true);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
  }

  public void repaintMainWindow() {
    this.getContentPane().removeAll();

    this.getContentPane().add(textPanel, BorderLayout.NORTH);
    this.getContentPane().add(taskPanel, BorderLayout.CENTER);
    this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

    this.getContentPane().revalidate();
    this.getContentPane().repaint();
  }

  public TextPanel getTextPanel() { return textPanel; }

  public void setTextPanel(TextPanel textPanel) {
    this.textPanel = textPanel;
  }

  public ButtonPanel getButtonPanel() {
    return buttonPanel;
  }

  public void setButtonPanel(ButtonPanel buttonPanel) {
    this.buttonPanel = buttonPanel;
  }

  public TaskPanel getTaskPanel() {
    return taskPanel;
  }

  public void setTaskPanel(TaskPanel taskPanel) { this.taskPanel = taskPanel; }

  public EditPanel getEditPanel() {
    return editPanel;
  }

  public void setEditPanel(EditPanel editPanel) {
    this.editPanel = editPanel;
  }
}
