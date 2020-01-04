package edu.ponomarev.step.view.main;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
  private TextPanel textPanel;
  private BoxButtonsPanel boxButtonsPanel;
  private ButtonPanel buttonPanel;
  private TaskPanel taskPanel;

  public Window(String applicationName) {
    super(applicationName);

    this.textPanel = new TextPanel();
    this.taskPanel = new TaskPanel();
    this.boxButtonsPanel = new BoxButtonsPanel();
    this.buttonPanel = new ButtonPanel();
  }

  public void run() {
    boxButtonsPanel.run();
    taskPanel.run();
    textPanel.run();
    buttonPanel.run();

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
    this.getContentPane().add(boxButtonsPanel, BorderLayout.EAST);
    this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

    this.getContentPane().revalidate();
    this.getContentPane().repaint();
  }

  public TextPanel getTextPanel() { return textPanel; }

  public void setTextPanel(TextPanel textPanel) {
    this.textPanel = textPanel;
  }

  public BoxButtonsPanel getBoxButtonsPanel() {
    return boxButtonsPanel;
  }

  public void setBoxButtonsPanel(BoxButtonsPanel boxButtonsPanel) {
    this.boxButtonsPanel = boxButtonsPanel;
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
}
