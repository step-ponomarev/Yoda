package edu.ponomarev.step.MVC.view.edit;

import javax.swing.*;
import java.awt.*;

public class ButtonPanel extends JPanel {
  private JButton saveButton;
  private JButton removeButton;

  public ButtonPanel() {
    super();
    this.saveButton = new JButton("Save");
    this.removeButton = new JButton("Remove");
  }

  public void run() {
    this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    this.add(saveButton);
    this.add(removeButton);

    this.setBackground(Color.WHITE);
    this.setVisible(true);
  }

  public JButton getSaveButton() {
    return saveButton;
  }

  public void setSaveButton(JButton saveButton) {
    this.saveButton = saveButton;
  }

  public JButton getRemoveButton() {
    return removeButton;
  }

  public void setRemoveButton(JButton removeButton) {
    this.removeButton = removeButton;
  }
}
