package edu.ponomarev.step.view.Main;

import javax.swing.*;
import java.awt.*;

public class BoxPanel extends JPanel {
  JButton[] box;

  public BoxPanel() {
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
