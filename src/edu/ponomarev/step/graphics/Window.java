package edu.ponomarev.step.graphics;
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

  private class WeeekButtonListener implements ActionListener {
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
        System.out.println("Энтер нажат!");
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

  private TaskHandler manager; // TODO Эта штука должна связывать графику и
  // задачи, проекты и пр!

  private TextPanel textP;
  private TaskPanel taskP;
  private BoxPanel boxP;
  private JPanel optionsP;

  public static void main(String[] args) {

    Window window = new Window(new TaskHandler());
    window.run();
  }

  public Window(TaskHandler handler) {
    super("Yoda");
    manager = handler;
    taskP = new TaskPanel();
    boxP = new BoxPanel();
    textP = new TextPanel();
  }

  public void run() {
    setUpBoxPanel();
    setUpTaskPanel();
    setUpTextPanel();


    this.setSize(500, 500);
    this.getContentPane().setBackground(new Color(255, 255, 255));
    this.setVisible(true);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
  }

  private void setUpBoxPanel() {
    this.getContentPane().add(boxP, BorderLayout.EAST);

    boxP.run();

    boxP.box[0].addActionListener( new InboxButtonListener());
    boxP.box[1].addActionListener(new TodayButtonListener());
    boxP.box[2].addActionListener(new WeeekButtonListener());
    boxP.box[3].addActionListener(new LateButtonListener());
  }

  private void setUpTaskPanel() {
    this.getContentPane().add(taskP, BorderLayout.CENTER);

    taskP.run();

    taskP.setList(new ArrayList<Task>()); // TODO коробка сегодня по умолчанию
  }

  private void setUpTextPanel() {
    this.getContentPane().add(textP, BorderLayout.NORTH);
    textP.field.addKeyListener(new AddTaskListener());

    for (JButton button : boxP.box) {
      textP.boxList.addItem(button.getText());
    }

    textP.run();
  }
}
