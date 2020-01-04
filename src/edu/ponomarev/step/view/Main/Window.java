package edu.ponomarev.step.view.Main;

import edu.ponomarev.step.view.Edit.EditPanel;
import edu.ponomarev.step.manager.DataBaseManager;
import edu.ponomarev.step.manager.DataHandler;
import edu.ponomarev.step.component.task.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Window extends JFrame {
  //TODO Сделать смену контейнера(раз)
  // Придумать как обойтись одним классом листенером.


  private class TodayButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      TaskPanel taskP = (TaskPanel) centerPanel;
      taskP.changeTaskList(handler.getTodayBox());
      taskP.setBoxLabel("Today");
      taskP.refresh();
    }
  }

  private class WeekButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      TaskPanel taskP = (TaskPanel) centerPanel;
      taskP.changeTaskList(handler.getWeekBox());
      taskP.setBoxLabel("Week");
      taskP.refresh();
    }
  }

  private class LateButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      TaskPanel taskP = (TaskPanel) centerPanel;
      taskP.changeTaskList(handler.getLateBox());
      taskP.setBoxLabel("Late");
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
      TextPanel.BoxItem item = (TextPanel.BoxItem) textPanel.boxList.getSelectedItem();

      switch (item.type) {
        case DAY:
          taskPanel.setList(handler.getTodayBox());
          break;

        case WEEK:
          taskPanel.setList(handler.getWeekBox());
          break;

        case LATE:
          taskPanel.setList(handler.getLateBox());
          break;

        default:
          taskPanel.setList(handler.getInbox());
          break;
      }

      String task = textPanel.textField.getText().strip();
      if (!task.isEmpty()) {
        TaskPanel taskP = (TaskPanel) centerPanel;
        handler.addTask(item.type, new Task(task));
        textPanel.textField.selectAll();

        if (item.getName().strip().equals(taskP.getBoxLabel().getText().strip())) {
          taskP.refresh();
        }
      }
    }
  }

  private class EditMakePanelButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      if (taskPanel.getList().isSelectionEmpty()) {
        return;
      }

      repaintEditWindow();
    }

    private void repaintEditWindow() {
      content.removeAll();

      centerPanel = new EditPanel(taskPanel.getSelected());
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

  private class SynchButtonListener implements ActionListener {

    //TODO Синхронизация в отдельном потоке (???)
    @Override
    public void actionPerformed(ActionEvent e) {
      handler.setDataWorker();
      handler.pushDate();

      taskPanel.refresh();
    }
  }

  private DataHandler handler;

  private JPanel northPanel;
  private JPanel centerPanel;
  private JPanel eastPanel;
  private JPanel southPanel;

  private Container content;

  private TextPanel textPanel;
  private BoxPanel boxPanel;
  private ButtonPanel buttonPanel;
  private TaskPanel taskPanel;

  public Window(DataHandler handler) {
    super("Yoda");
    this.handler = handler;

    this.content = this.getContentPane();

    this.textPanel = new TextPanel();
    this.northPanel = textPanel;

    this.taskPanel = new TaskPanel(handler.getTodayBox());
    this.centerPanel = taskPanel;

    this.boxPanel = new BoxPanel();
    this.eastPanel = boxPanel;

    this.buttonPanel = new ButtonPanel();
    this.southPanel = buttonPanel;
  }

  public void run() {
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

  public TextPanel getTextPanel() {
    return textPanel;
  }

  public void setTextPanel(TextPanel textPanel) {
    this.textPanel = textPanel;
  }

  public BoxPanel getBoxPanel() {
    return boxPanel;
  }

  public void setBoxPanel(BoxPanel boxPanel) {
    this.boxPanel = boxPanel;
  }

  public ButtonPanel getButtonPanel() {
    return buttonPanel;
  }

  public void setButtonPanel(ButtonPanel buttonPanel) {
    this.buttonPanel = buttonPanel;
  }

  public TaskPanel getTaskPanel() {
    return taskPanel;
  }

  public void setTaskPanel(TaskPanel taskPanel) {
    this.taskPanel = taskPanel;
  }

  private void setUpBoxPanel() {
    boxPanel.run();

    //TODO Вынести это все в контроллер

  }

  private void setUpTaskPanel() {
    taskPanel.run();

    taskPanel.setBoxLabel("Today");

    centerPanel.revalidate();
    centerPanel.repaint();
  }

  private void setUpTextPanel() {
    // TODO ОТ СИХ
    textPanel.textField.addKeyListener(new AddTaskListener());

    textPanel.synchButton.addActionListener(new SynchButtonListener());

    for (int i = 0; i < 4; ++i) {
      textPanel.boxList.addItem(new TextPanel.BoxItem(boxPanel.box[i].getText(), DataHandler.BoxType.values()[i]));
    }

    // TODO ДО СИХ
    //  ВЫНЕСТИ ЭТО ВСЕ В КОНТРОЛЛЕР

    textPanel.textField.selectAll();

    textPanel.run();
    textPanel.revalidate();
    textPanel.repaint();
  }

  private void setUpButtonPanel() {
    //TODO ВЫНЕСТИ ЭТО ВСЕ В КОНТРОЛЛЕР
    buttonPanel.addButton.addActionListener(new AddButtonListener());
    buttonPanel.editButton.addActionListener(new EditMakePanelButtonListener());

    buttonPanel.run();
    buttonPanel.revalidate();
    buttonPanel.repaint();
  }

  private void setUpEditPanel(EditPanel editP) {
    // TODO ВЫНЕСТИ В КОНТРОЛЛЕР
    editP.setSaveButtonListener(new EditSaveButtonListener());
    editP.run();
  }

  public void repaintMainWindow() {
    content.removeAll();

    northPanel = boxPanel;
    centerPanel = taskPanel;
    northPanel = textPanel;
    eastPanel = boxPanel;
    southPanel = buttonPanel;

    content.add(northPanel, BorderLayout.NORTH);
    content.add(centerPanel, BorderLayout.CENTER);
    content.add(eastPanel, BorderLayout.EAST);
    content.add(southPanel, BorderLayout.SOUTH);

    content.revalidate();
    content.repaint();
  }

}
