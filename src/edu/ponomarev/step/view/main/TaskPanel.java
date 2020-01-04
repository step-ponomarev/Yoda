package edu.ponomarev.step.view.main;

import edu.ponomarev.step.component.task.Task;
import edu.ponomarev.step.component.task.TaskContainer;
import edu.ponomarev.step.manager.DataHandler;

import javax.swing.*;
import java.awt.*;

public class TaskPanel extends JPanel {
  private JList list;
  private JScrollPane scrollPane;
  private JLabel boxLabel;

  private DataHandler.BoxType currentBoxType;

  public TaskPanel() {
    super();
    this.boxLabel = new JLabel();
    this.list = new JList<Task>();
  }

  public void run() {
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    boxLabel.setBackground(Color.WHITE);

    scrollPane = new JScrollPane(list);
    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

    this.add(boxLabel);
    boxLabel.setHorizontalAlignment(JLabel.CENTER);
    this.add(scrollPane);

    this.setBackground(new Color(255, 255, 255));
    this.setVisible(true);
  }

  public DataHandler.BoxType getCurrentBoxType() {
    return currentBoxType;
  }

  public void setCurrentBoxType(DataHandler.BoxType currentBoxType) {
    this.currentBoxType = currentBoxType;
  }

  public void setList(JList list) {
    this.list = list;
  }

  public JList getList() {
    return list;
  }

  public JScrollPane getScrollPane() {
    return scrollPane;
  }

  public void setScrollPane(JScrollPane scrollPane) {
    this.scrollPane = scrollPane;
  }

  public void setBoxLabel(String label) {
    boxLabel.setText(label);
  }

  public JLabel getBoxLabel() {
    return boxLabel;
  }

  public Task getSelected() {
    return (Task) list.getSelectedValue();
  }
}
