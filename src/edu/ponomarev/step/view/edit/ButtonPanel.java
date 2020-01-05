package edu.ponomarev.step.view.edit;

import javax.swing.*;
import java.awt.*;

public class ButtonPanel extends JPanel {
  private JButton saveButton;
  private JButton deleteButton;

  public ButtonPanel() {
    super();
    this.saveButton = new JButton("Save");
    this.deleteButton = new JButton("Remove");
  }

  public void run() {
    this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    this.add(saveButton);
    this.add(deleteButton);

    this.setBackground(Color.WHITE);
    this.setVisible(true);
  }

  public JButton getSaveButton() {
    return saveButton;
  }

  public void setSaveButton(JButton saveButton) {
    this.saveButton = saveButton;
  }

  public JButton getDeleteButton() {
    return deleteButton;
  }

  public void setDeleteButton(JButton deleteButton) {
    this.deleteButton = deleteButton;
  }
}
