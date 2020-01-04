package edu.ponomarev.step.view.Main;

import edu.ponomarev.step.component.task.Task;
import edu.ponomarev.step.component.task.TaskContainer;

import javax.swing.*;
import java.awt.*;

public class TaskPanel extends JPanel {
  private TaskContainer tasks;
  private JList list;
  private JScrollPane scrollPane;
  private JLabel boxLabel;

  public TaskPanel(TaskContainer tasks) {
    super();
    this.tasks = tasks;
    this.boxLabel = new JLabel();
    list = new JList<Task>();
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

    refresh();

    this.setBackground(new Color(255, 255, 255));
    this.setVisible(true);
  }

  public void setList(JList list) {
    this.list = list;
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

  public void changeTaskList(TaskContainer tasks) {
    setList(tasks);
    refresh();
  }

  public void setList(TaskContainer tasks) {
    this.tasks = tasks;
  }

  public JList getList() {
    return list;
  }

  public void refresh() {
    list.removeAll();
    list.setListData(tasks.toArray());
    list.revalidate();
    list.repaint();
  }
}
