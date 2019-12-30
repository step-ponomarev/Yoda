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
      taskP.setList(new ArrayList<Task>());
    }
  }

  private class TodayButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      taskP.setList(new ArrayList<Task>());
    }
  }

  private class WeekButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      taskP.setList(new ArrayList<Task>());
    }
  }

  private class LateButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
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
        taskP.addTask(task);
        textP.field.selectAll();
      }
    }
  }

  private class EditButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      editP = new EditPanel(taskP.getSelected());
      content.add(editP, BorderLayout.CENTER);
      setUpEditPanel();
    }
  }

  //private TaskHandler manager; // TODO Эта штука должна связывать графику и
  // задачи, проекты и пр!
  private Container content;

  private TextPanel textP;
  private TaskPanel taskP;
  private BoxPanel boxP;
  private ButtonPanel buttonPanel;
  private EditPanel editP;

  public static void main(String[] args) {
    Window window = new Window(new TaskHandler());
    window.run();
  }

  public Window(TaskHandler handler) {
    super("Yoda");
    //manager = handler;
    content = this.getContentPane();
    taskP = new TaskPanel();
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
    this.getContentPane().add(boxP, BorderLayout.EAST);

    boxP.run();

    boxP.box[0].addActionListener(new InboxButtonListener());
    boxP.box[1].addActionListener(new TodayButtonListener());
    boxP.box[2].addActionListener(new WeekButtonListener());
    boxP.box[3].addActionListener(new LateButtonListener());
  }

  private void setUpTaskPanel() {
    content.add(taskP, BorderLayout.CENTER);

    taskP.run();

    taskP.setList(new ArrayList<Task>()); // TODO коробка сегодня по умолчанию
  }

  private void setUpTextPanel() {
    content.add(textP, BorderLayout.NORTH);
    textP.field.addKeyListener(new AddTaskListener());

    for (JButton button : boxP.box) {
      textP.boxList.addItem(button.getText());
    }

    textP.run();
  }

  private void setUpButtonPanel() {
    content.add(buttonPanel, BorderLayout.SOUTH);

    buttonPanel.addButton.addActionListener(new AddButtonListener());
    buttonPanel.editButton.addActionListener(new EditButtonListener());

    buttonPanel.run();
  }

  private void setUpEditPanel() {
    editP.run();
    editP.revalidate();
    editP.repaint();
  }
}
