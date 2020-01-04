package edu.ponomarev.step.view.main;

import edu.ponomarev.step.manager.DataHandler;

import javax.swing.*;
import java.awt.*;

public class TextPanel extends JPanel {
  public static class BoxItem {
    public DataHandler.BoxType type;
    public String name;

    public BoxItem(String name, DataHandler.BoxType type) {
      this.name = name;
      this.type = type;
    }

    public DataHandler.BoxType getType() {
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

  JTextField textField;
  JComboBox<TextPanel.BoxItem> boxList;
  JButton synchButton;

  public TextPanel() {
    super();
    textField = new JTextField("Новая задача...");
    boxList = new JComboBox<TextPanel.BoxItem>();
    synchButton = new JButton("Synch");
  }

  public void run() {
    this.setLayout(new BorderLayout());
    this.add(textField, BorderLayout.CENTER);
    this.add(boxList, BorderLayout.WEST);
    this.add(synchButton, BorderLayout.EAST);

    this.setBackground(new Color(255, 255, 255));
    this.setVisible(true);
  }

  public JTextField getTextField() {
    return textField;
  }

  public void setTextField(JTextField textField) {
    this.textField = textField;
  }

  public JComboBox<BoxItem> getBoxList() {
    return boxList;
  }

  public void setBoxList(JComboBox<BoxItem> boxList) {
    this.boxList = boxList;
  }

  public JButton getSynchButton() {
    return synchButton;
  }

  public void setSynchButton(JButton synchButton) {
    this.synchButton = synchButton;
  }
}
