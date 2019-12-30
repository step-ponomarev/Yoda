package edu.ponomarev.step.graphics.Main;

import javax.swing.*;
import java.awt.*;

public class BoxPanel extends JPanel {
  JButton[] box;

  public BoxPanel() {
    super();
    box = new JButton[] {
        new JButton("Inbox"),
        new JButton("TD"),
        new JButton("W"),
        new JButton("L")};

  }

  public void run() {
    for (JButton box : box) {
      this.add(box);
    }

    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.setBackground(new Color(255, 255, 255));
    this.setVisible(true);
  }
}
