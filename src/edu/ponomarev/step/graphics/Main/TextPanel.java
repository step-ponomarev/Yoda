package edu.ponomarev.step.graphics.Main;

import edu.ponomarev.step.manager.TaskHandler;

import javax.swing.*;
import java.awt.*;

public class TextPanel extends JPanel {
  static class BoxItem {
    TaskHandler.BoxType type;
    String name;

    BoxItem(String name, TaskHandler.BoxType type) {
      this.name = name;
      this.type = type;
    }

    public TaskHandler.BoxType getType() {
      return type;
    }

    public String getName() {
      return name;
    }

    @Override
    public String toString() {
      return name;
    }
  }

  JTextField field;
  JComboBox<TextPanel.BoxItem> boxList;

  public TextPanel() {
    super();
    field = new JTextField("Новая задача...");
    boxList = new JComboBox<TextPanel.BoxItem>();
  }

  public void run() {
    this.setLayout(new BorderLayout());
    this.add(field, BorderLayout.CENTER);
    this.add(boxList, BorderLayout.WEST);

    this.setBackground(new Color(255, 255, 255));
    this.setVisible(true);
  }
}
