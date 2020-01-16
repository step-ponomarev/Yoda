package edu.ponomarev.step.MVC.view.taskPanel;

import edu.ponomarev.step.component.taskContainer.TermTaskContainer;

import javax.swing.*;
import java.awt.*;

public class TaskBoxModule extends JPanel {
  private JLabel boxNameLabel;
  private JScrollPane taskScrollPane;
  private TermTaskContainer.ContainerType moduleType;

  public TaskBoxModule(String boxName, JList list, TermTaskContainer.ContainerType containerType) {
    super();

    this.boxNameLabel = new JLabel(boxName);
    this.taskScrollPane = new JScrollPane(list);
    this.moduleType = containerType;
  }

  public void run() {
    this.setLayout(new BorderLayout());

    this.add(BorderLayout.NORTH, boxNameLabel);
    this.add(BorderLayout.CENTER, taskScrollPane);

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

  public TermTaskContainer.ContainerType getModuleType() {
    return moduleType;
  }

  public void setModuleType(TermTaskContainer.ContainerType moduleType) {
    this.moduleType = moduleType;
  }
}

