package edu.ponomarev.step.graphics.Main;

import edu.ponomarev.step.task.Task;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TaskPanel extends JPanel {
  private ArrayList<Task> tasks;
  private JList list;
  private JScrollPane scrollPane;

  public TaskPanel(ArrayList<Task> tasks) {
    super();
    this.tasks = tasks;
    list = new JList<Task>();
  }

  public void run() {
    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    scrollPane = new JScrollPane(list);
    scrollPane.setSize(400, 800);
    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

    this.add(scrollPane, BorderLayout.CENTER);

    refreshList();

    this.setBackground(new Color(255, 255, 255));
    this.setVisible(true);

  }

  public void addTask(String task) {
    tasks.add(new Task(task));
    refreshList();
  }

  public Task getSelected() {
    return (Task)list.getSelectedValue();
  }

  public void reemoveTask() {
    Task removed = (Task) list.getSelectedValue();
    tasks.remove(removed);
    list.setListData(tasks.toArray());
    refreshList();
  }

  public void setList(ArrayList<Task> tasks) {
    this.tasks = tasks;
  }

  public JList getList() {
    return list;
  }

  private void refreshList() {
    list.removeAll();
    list.setListData(tasks.toArray());
    list.revalidate();
    list.repaint();
  }
}
