package edu.ponomarev.step.view.main;

import javax.swing.*;
import java.awt.*;

public class BoxButtonsPanel extends JPanel {
  JButton[] box;

  public BoxButtonsPanel() {
    super();
    box = new JButton[] {
        new JButton("Inbox"),
        new JButton("Today"),
        new JButton("Week"),
        new JButton("Late")};
  }

  public void run() {
    for (JButton box : box) {
      this.add(box);
    }

    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.setBackground(new Color(255, 255, 255));
    this.setVisible(true);
  }

  public JButton[] getBox() {
    return box;
  }

  public void setBox(JButton[] box) {
    this.box = box;
  }
}
