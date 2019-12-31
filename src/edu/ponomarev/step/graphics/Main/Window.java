package edu.ponomarev.step.graphics.Main;
import edu.ponomarev.step.graphics.Edit.EditPanel;
import edu.ponomarev.step.manager.TaskHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Window extends JFrame {
  //TODO Сделать смену контейнера(раз)
  // Придумать как обойтись одним классом листенером.
  private class InboxButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      TaskPanel taskP = (TaskPanel) centerPanel;
      taskP.changeTaskList(manager.getInbox());
      taskP.setLabel("Inbox");
      taskP.refresh();
    }
  }

  private class TodayButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      TaskPanel taskP = (TaskPanel) centerPanel;
      taskP.changeTaskList(manager.getTodayBox());
      taskP.setLabel("Today");
      taskP.refresh();
    }
  }

  private class WeekButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      TaskPanel taskP = (TaskPanel) centerPanel;
      taskP.changeTaskList(manager.getWeekBox());
      taskP.setLabel("Week");
      taskP.refresh();
    }
  }

  private class LateButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      TaskPanel taskP = (TaskPanel) centerPanel;
      taskP.changeTaskList(manager.getLateBox());
      taskP.setLabel("Late");
      taskP.refresh();
    }
  }

  private class AddTaskListener implements KeyListener {
    private final int ENTER = 10;

    @Override
    public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == ENTER) {
        buttonPanel.addButton.doClick();
      }
    }

    @Override
    public void keyReleased(KeyEvent e) { return; }

    @Override
    public void keyTyped(KeyEvent e) {
      return;
    }
  }

  private class AddButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      TextPanel.BoxItem item = (TextPanel.BoxItem) textP.boxList.getSelectedItem();

      switch (item.type) {
        case DAY:
          taskP.setList(manager.getTodayBox());
          break;
        case WEEK:
          taskP.setList(manager.getWeekBox());
          break;
        case LATE:
          taskP.setList(manager.getLateBox());
          break;
        default:
          taskP.setList(manager.getInbox());
          break;
      }

      String task = textP.field.getText().strip();
      if (!task.isEmpty()) {
        TaskPanel taskP = (TaskPanel) centerPanel;
        taskP.addTask(task);
        textP.field.selectAll();

        if (item.getName().strip().equals(taskP.getLabel().strip())) {
          taskP.refresh();
        }
      }
    }
  }

  private class EditMakePanelButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      if (taskP.getList().isSelectionEmpty()) {
        return;
      }

      repaintEditWindow();
    }

    private void repaintEditWindow() {
      content.removeAll();

      centerPanel =  new EditPanel(taskP.getSelected());
      content.add(centerPanel, BorderLayout.CENTER);
      setUpEditPanel((EditPanel) centerPanel);

      content.revalidate();
      content.repaint();
    }
  }

  private class EditSaveButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      EditPanel editP = (EditPanel) centerPanel;
      //TODO Сохранить изменения в таске
      repaintMainWindow();
    }
  }

  private TaskHandler manager;

  private JPanel northPanel;
  private JPanel centerPanel;
  private JPanel eastPanel;
  private  JPanel southPanel;

  private Container content;

  private TextPanel textP;
  private BoxPanel boxP;
  private ButtonPanel buttonPanel;
  private TaskPanel taskP;

  public Window(TaskHandler handler) {
    super("Yoda");
    manager = handler;

    content = this.getContentPane();

    textP = new TextPanel();
    northPanel = textP;

    taskP = new TaskPanel(handler.getTodayBox());
    centerPanel = taskP;

    boxP = new BoxPanel();
    eastPanel = boxP;

    buttonPanel = new ButtonPanel();
    southPanel = buttonPanel;
  }

  public void run() throws Exception {
    setUpBoxPanel();
    setUpTaskPanel();
    setUpTextPanel();
    setUpButtonPanel();

    repaintMainWindow();

    this.setSize(500, 500);
    this.getContentPane().setBackground(new Color(255, 255, 255));
    this.setVisible(true);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
  }

  private void setUpBoxPanel() {
    boxP.run();

    boxP.box[0].addActionListener(new InboxButtonListener());
    boxP.box[1].addActionListener(new TodayButtonListener());
    boxP.box[2].addActionListener(new WeekButtonListener());
    boxP.box[3].addActionListener(new LateButtonListener());
  }

  private void setUpTaskPanel() {
    taskP.run();

    taskP.setLabel("Today");

    centerPanel.revalidate();
    centerPanel.repaint();
  }

  private void setUpTextPanel() {
    textP.field.addKeyListener(new AddTaskListener());

    for (int i = 0; i < 4; ++i) {
      textP.boxList.addItem(new TextPanel.BoxItem(boxP.box[i].getText(), TaskHandler.BoxType.values()[i]));
    }

    textP.field.selectAll();

    textP.run();
    textP.revalidate();
    textP.repaint();
  }

  private void setUpButtonPanel() {
    buttonPanel.addButton.addActionListener(new AddButtonListener());
    buttonPanel.editButton.addActionListener(new EditMakePanelButtonListener());

    buttonPanel.run();
    buttonPanel.revalidate();
    buttonPanel.repaint();
  }

  private void setUpEditPanel(EditPanel editP) {
    editP.setSaveButtonListener(new EditSaveButtonListener());
    editP.run();
  }

  private void repaintMainWindow() {
    content.removeAll();

    northPanel = boxP;
    centerPanel = taskP;
    northPanel = textP;
    eastPanel = boxP;
    southPanel = buttonPanel;

    content.add(northPanel, BorderLayout.NORTH);
    content.add(centerPanel, BorderLayout.CENTER);
    content.add(eastPanel, BorderLayout.EAST);
    content.add(southPanel, BorderLayout.SOUTH);

    content.revalidate();
    content.repaint();
  }
}
