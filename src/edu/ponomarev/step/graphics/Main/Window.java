package edu.ponomarev.step.graphics.Main;
import edu.ponomarev.step.graphics.Edit.EditPanel;
import edu.ponomarev.step.manager.TaskHandler;
import edu.ponomarev.step.task.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Window extends JFrame {
  //TODO Сделать смену контейнера(раз)
  // Придумать как обойтись одним классом листенером.
  private class InboxButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      TaskPanel taskP = (TaskPanel) centerPanel;
      taskP.setList(new ArrayList<Task>());
    }
  }

  private class TodayButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      TaskPanel taskP = (TaskPanel) centerPanel;
      taskP.setList(new ArrayList<Task>());
    }
  }

  private class WeekButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      TaskPanel taskP = (TaskPanel) centerPanel;
      taskP.setList(new ArrayList<Task>());
    }
  }

  private class LateButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      TaskPanel taskP = (TaskPanel) centerPanel;
      taskP.setList(new ArrayList<Task>());
    }
  }

  private class AddTaskListener implements KeyListener {
    private final int ENTER = 10;

    @Override
    public void keyPressed(KeyEvent e) {
      //TODO Добавить добавление задачи в зависимости
      // от выбранного бокса!
      if (e.getKeyCode() == ENTER) {
        buttonPanel.addButton.doClick();
      }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {
      return;
    }
  }

  private class AddButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      String task = textP.field.getText().strip();
      if (!task.isEmpty()) {
        TaskPanel taskP = (TaskPanel) centerPanel;
        taskP.addTask(task);
        textP.field.selectAll();
      }
    }
  }

  private class EditMakePanelButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      if (taskP.getList().isSelectionEmpty()) {
        return;
      }

      content.removeAll();

      centerPanel =  new EditPanel(taskP.getSelected());
      content.add(centerPanel, BorderLayout.CENTER);
      setUpEditPanel();

      content.revalidate();
      content.repaint();
    }
  }

  private class EditSaveButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      //EditPanel editP = (EditPanel) centerPanel;
      //TODO Сохранить изменения в таске
      centerPanel =  taskP;

      content.removeAll();

      setUpBoxPanel();
      setUpTaskPanel();
      setUpTextPanel();
      setUpButtonPanel();

      content.revalidate();
      content.repaint();
    }
  }

  //private TaskHandler manager; // TODO Эта штука должна связывать графику и
  // задачи, проекты и пр!
  private JPanel centerPanel;
  private Container content;

  private TextPanel textP;
  private BoxPanel boxP;
  private ButtonPanel buttonPanel;
  private TaskPanel taskP;



  public static void main(String[] args) {
    Window window = new Window(new TaskHandler());
    window.run();
  }

  public Window(TaskHandler handler) {
    super("Yoda");
    //manager = handler;
    taskP = new TaskPanel(new ArrayList<Task>());
    centerPanel = taskP;

    content = this.getContentPane();
    boxP = new BoxPanel();
    textP = new TextPanel();
    buttonPanel = new ButtonPanel();
  }

  public void run() {
    setUpBoxPanel();
    setUpTaskPanel();
    setUpTextPanel();
    setUpButtonPanel();

    this.setSize(500, 500);
    this.getContentPane().setBackground(new Color(255, 255, 255));
    this.setVisible(true);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
  }

  private void setUpBoxPanel() {
    content.add(boxP, BorderLayout.EAST);

    boxP.run();

    boxP.box[0].addActionListener(new InboxButtonListener());
    boxP.box[1].addActionListener(new TodayButtonListener());
    boxP.box[2].addActionListener(new WeekButtonListener());
    boxP.box[3].addActionListener(new LateButtonListener());
  }

  private void setUpTaskPanel() {
    content.add(centerPanel, BorderLayout.CENTER);

    taskP.run();

    //taskP.setList(); // TODO коробка сегодня по умолчанию
    centerPanel.revalidate();
    centerPanel.repaint();
  }

  private void setUpTextPanel() {
    content.add(textP, BorderLayout.NORTH);
    textP.field.addKeyListener(new AddTaskListener());

    for (JButton button : boxP.box) {
      textP.boxList.addItem(button.getText());
    }

    textP.run();
    textP.revalidate();
    textP.repaint();
  }

  private void setUpButtonPanel() {
    content.add(buttonPanel, BorderLayout.SOUTH);

    buttonPanel.addButton.addActionListener(new AddButtonListener());
    buttonPanel.editButton.addActionListener(new EditMakePanelButtonListener());

    buttonPanel.run();
    buttonPanel.revalidate();
    buttonPanel.repaint();
  }

  private void setUpEditPanel() {
    EditPanel editP = (EditPanel) centerPanel;
    editP.setSaveButtonListener(new EditSaveButtonListener());
    editP.run();
  }
}
