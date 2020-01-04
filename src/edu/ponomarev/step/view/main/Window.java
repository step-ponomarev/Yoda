package edu.ponomarev.step.view.main;
import edu.ponomarev.step.manager.DataHandler;

import javax.swing.*;
import java.awt.*;


public class Window extends JFrame {
  private JPanel northPanel;
  private JPanel centerPanel;
  private JPanel eastPanel;
  private JPanel southPanel;

  private Container content;

  private TextPanel textPanel;
  private BoxButtonsPanel boxButtonsPanel;
  private ButtonPanel buttonPanel;
  private TaskPanel taskPanel;

  public Window(String applicationName) {
    super(applicationName);

    this.content = this.getContentPane();

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

  public Container getContent() {
    return content;
  }

  public void setContent(Container content) {
    this.content = content;
  }

  public TextPanel getTextPanel() {
    return textPanel;
  }

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

  public void setTaskPanel(TaskPanel taskPanel) {
    this.taskPanel = taskPanel;
  }

  public void repaintMainWindow() {
    content.removeAll();

    content.add(textPanel, BorderLayout.NORTH);
    content.add(taskPanel, BorderLayout.CENTER);
    content.add(boxButtonsPanel, BorderLayout.EAST);
    content.add(buttonPanel, BorderLayout.SOUTH);

    content.revalidate();
    content.repaint();
  }

}
