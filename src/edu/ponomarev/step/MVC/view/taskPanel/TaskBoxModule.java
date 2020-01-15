package edu.ponomarev.step.MVC.view.taskPanel;

import edu.ponomarev.step.component.task.Task;
import edu.ponomarev.step.MVC.model.DataHandler;

import javax.swing.*;
import java.awt.*;

public class TaskBoxModule extends JPanel {
  private JLabel boxNameLabel;
  private JScrollPane taskScrollPane;
  private DataHandler.BoxType currentType;

  public TaskBoxModule(String boxName, JList<Task> list, DataHandler.BoxType type) {
    super();

    this.boxNameLabel = new JLabel(boxName);
    this.taskScrollPane = new JScrollPane(list);
    this.currentType = type;
  }

  public void run() {
    this.setLayout(new BorderLayout());

    this.add(BorderLayout.NORTH, boxNameLabel);
    this.add(BorderLayout.CENTER, taskScrollPane);

    //TODO Make it alwasys in focus

    taskScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    taskScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    taskScrollPane.setVisible(true);
    this.setBackground(Color.WHITE);
    this.setVisible(true);
  }

  public JScrollPane getTaskScrollPane() {
    return taskScrollPane;
  }

  public void setTaskScrollPane(JScrollPane taskScrollPane) {
    this.taskScrollPane = taskScrollPane;
  }

  public DataHandler.BoxType getCurrentType() {
    return currentType;
  }

  public void setCurrentType(DataHandler.BoxType currentType) {
    this.currentType = currentType;
  }
}

