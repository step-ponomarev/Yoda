package edu.ponomarev.step.MVC.view.taskPanel;

import edu.ponomarev.step.component.taskContainer.termContainer.ContainerVariable.ContainerType;

import javax.swing.*;
import java.awt.*;

public class TaskBoxModule extends JPanel {
  private JLabel boxNameLabel;
  private JScrollPane taskScrollPane;
  private ContainerType moduleType;

  public TaskBoxModule(String boxName, JList list, ContainerType containerType) {
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

  public ContainerType getModuleType() {
    return moduleType;
  }

  public void setModuleType(ContainerType moduleType) {
    this.moduleType = moduleType;
  }
}

