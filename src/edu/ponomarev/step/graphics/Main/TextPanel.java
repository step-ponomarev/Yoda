package edu.ponomarev.step.graphics.Main;

import javax.swing.*;
import java.awt.*;

public class TextPanel extends JPanel {
  JTextField field;
  JComboBox boxList;

  public TextPanel() {
    super();
    field = new JTextField("Новая задача...");
    boxList = new JComboBox();
  }

  public void run() {
    this.setLayout(new FlowLayout());
    this.add(field, BorderLayout.CENTER);
    this.add(boxList, BorderLayout.EAST);

    this.setBackground(new Color(255, 255, 255));
    this.setVisible(true);
  }
}
