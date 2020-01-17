package edu.ponomarev.step.MVC.view;

import javax.swing.*;
import java.awt.*;

public class ButtonPanel extends JPanel {
  JButton addButton;
  JButton editButton;

  public ButtonPanel() {
    super();
    addButton = new JButton("Add");
    editButton = new JButton("Edit");
  }

  public void run() {
    this.setLayout(new FlowLayout());
    this.add(addButton);
    this.add(editButton);

    this.setBackground(new Color(255, 255, 255));
    this.setVisible(true);
  }

  public JButton getAddButton() {
    return addButton;
  }

  public void setAddButton(JButton addButton) {
    this.addButton = addButton;
  }

  public JButton getEditButton() {
    return editButton;
  }

  public void setEditButton(JButton editButton) {
    this.editButton = editButton;
  }
}
