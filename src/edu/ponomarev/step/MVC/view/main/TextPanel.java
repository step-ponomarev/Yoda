package edu.ponomarev.step.MVC.view.main;

import javax.swing.*;
import java.awt.*;

public class TextPanel extends JPanel {
  JTextField textField;
  JComboBox boxList;
  JButton synchButton;

  public TextPanel() {
    super();
    textField = new JTextField("Новая задача...");
    boxList = new JComboBox();
    synchButton = new JButton("Synch");
  }

  public void run() {
    this.setLayout(new BorderLayout());
    this.add(textField, BorderLayout.CENTER);
    this.add(boxList, BorderLayout.WEST);
    this.add(synchButton, BorderLayout.EAST);

    this.textField.setPreferredSize( new Dimension(200, 24));

    this.setBackground(new Color(255, 255, 255));
    this.setVisible(true);
  }

  public void resetTextField() {
    textField.setText("Новая задача...");
  }

  public JTextField getTextField() {
    return textField;
  }

  public void setTextField(JTextField textField) {
    this.textField = textField;
  }

  public JComboBox getBoxList() {
    return boxList;
  }

  public void setBoxList(JComboBox boxList) {
    this.boxList = boxList;
  }

  public JButton getSynchButton() {
    return synchButton;
  }

  public void setSynchButton(JButton synchButton) {
    this.synchButton = synchButton;
  }
}
