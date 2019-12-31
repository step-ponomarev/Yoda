package edu.ponomarev.step.graphics.Main;

import edu.ponomarev.step.task.Task;
import edu.ponomarev.step.task.TaskContainer;

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

  public void setLabel(String label) {
    boxLabel.setText(label);
  }

  public String getLabel() {
    return boxLabel.getText();
  }

  public void addTask(String task) {
    tasks.add(new Task(task));
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
