package edu.ponomarev.step.MVC.view;

import edu.ponomarev.step.component.taskContainer.termContainer.TermTaskContainer;

import javax.swing.*;
import java.awt.*;

public class TextPanel extends JPanel {
  private JTextField taskStatementField;
  private JComboBox<TermTaskContainer.ContainerVariable> containerTypeList;
  private JButton synchButton;

  public TextPanel() {
    super();
    taskStatementField = new JTextField("Новая задача...");
    containerTypeList = new JComboBox();
    synchButton = new JButton("Synch");
  }

  public void run() {
    this.setLayout(new BorderLayout());
    this.add(taskStatementField, BorderLayout.CENTER);
    this.add(containerTypeList, BorderLayout.WEST);
    this.add(synchButton, BorderLayout.EAST);

    this.taskStatementField.setPreferredSize( new Dimension(200, 24));

    this.setBackground(new Color(255, 255, 255));
    this.setVisible(true);
  }

  public void resetTextField() {
    taskStatementField.setText("Новая задача...");
  }

  public JTextField getTaskStatementField() {
    return taskStatementField;
  }

  public void setTaskStatementField(JTextField taskStatementField) {
    this.taskStatementField = taskStatementField;
  }

  public JComboBox getContainerTypeList() {
    return containerTypeList;
  }

  public void setContainerTypeList(JComboBox containerTypeList) {
    this.containerTypeList = containerTypeList;
  }

  public JButton getSynchButton() {
    return synchButton;
  }

  public void setSynchButton(JButton synchButton) {
    this.synchButton = synchButton;
  }
}
