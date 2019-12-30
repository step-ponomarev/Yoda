package edu.ponomarev.step.graphics.Main;

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
}
