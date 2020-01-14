package edu.ponomarev.step.controller;

import edu.ponomarev.step.component.task.Task;
import edu.ponomarev.step.view.edit.TaskState;
import edu.ponomarev.step.view.main.Window;
import edu.ponomarev.step.manager.DataHandler;

import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Controller {
  private Window window;
  private DataHandler handler;

  public Controller(Window window, DataHandler handler) {
    this.handler = handler;
    this.window = window;
  }

  public void initView() {
    window.run();

    initMainWindow();
    initTaskPanel();
    initBoxButtons();
    initTextPanel();
    initButtonPanel();
    changeWindowColorMode();

    synchFull();
  }

  private void initMainWindow() {
    window.addWindowListener(new WindowListener() {
      @Override
      public void windowOpened(WindowEvent e) {

      }

      @Override
      public void windowClosing(WindowEvent e) {

      }

      @Override
      public void windowClosed(WindowEvent e) {
        synchFull();
      }

      @Override
      public void windowIconified(WindowEvent e) {

      }

      @Override
      public void windowDeiconified(WindowEvent e) {

      }

      @Override
      public void windowActivated(WindowEvent e) {

      }

      @Override
      public void windowDeactivated(WindowEvent e) {

      }
    });
  }

  private void initTaskPanel() {
    window.getTaskPanel().setListMap(handler.getBox());
    window.getTaskPanel().setCurrentBoxType(DataHandler.BoxType.DAY);
    window.getTaskPanel().run();

    for (DataHandler.BoxVariable currentVariable : DataHandler.BOX_VARIABLES) {
      window.getTaskPanel().getListMap().get(currentVariable.type).addListSelectionListener(e -> {
        for (DataHandler.BoxVariable anotherVariable : DataHandler.BOX_VARIABLES) {
          if (!anotherVariable.type.equals(currentVariable.type)) {
            window.getTaskPanel().getListMap().get(anotherVariable.type).clearSelection();
          }
        }

        window.getEditPanel().setCurrentTask((Task) window.getTaskPanel().getListMap().get(currentVariable.type).getSelectedValue());
        window.getTaskPanel().setCurrentBoxType(currentVariable.type);
      });
    }
  }

  private void initBoxButtons() {
    for (JButton button : window.getBoxButtonsPanel().getBox()) {
      button.addActionListener(e -> {
        for (DataHandler.BoxVariable boxRequest : DataHandler.BOX_VARIABLES) {
          if (boxRequest.boxName.equals(button.getText())) {
            window.getTaskPanel().setCurrentBoxType(boxRequest.type);
            window.getTaskPanel().getListMap().get(boxRequest.type).setListData(handler.getBox().get(boxRequest.type).toArray());
            window.getTaskPanel().getBoxLabel().setText(boxRequest.boxName);
            break;
          }
        }
      });
    }
  }

  private void initTextPanel() {
    final int ENTER = 10;

    for (DataHandler.BoxVariable item : DataHandler.BOX_VARIABLES) {
      window.getTextPanel().getBoxList().addItem(item);
    }

    window.getTextPanel().resetTextField();
    window.getTextPanel().getTextField().selectAll();

    window.getTextPanel().getBoxList().setSelectedIndex(0);

    window.getTextPanel().getTextField().addKeyListener( new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == ENTER) {
          window.getButtonPanel().getAddButton().doClick();
        }
      }

      @Override
      public void keyPressed(KeyEvent e) {}

      @Override
      public void keyReleased(KeyEvent e) {}
    });

    window.getTextPanel().getSynchButton().addActionListener(e -> {
      synchFull();
    });
  }

  private void initButtonPanel() {
    //AddTask Button
    window.getButtonPanel().getAddButton().addActionListener(e -> {
      final DataHandler.BoxVariable item = (DataHandler.BoxVariable) window.getTextPanel().getBoxList().getSelectedItem();

      String taskStatement = window.getTextPanel().getTextField().getText().strip();
      if (!taskStatement.isEmpty()) {
        handler.addTask(item.type, new Task(taskStatement));

        window.getTextPanel().getTextField().selectAll();

        window.getTaskPanel().getListMap().get(item.type).setListData(handler.getBox().get(item.type).toArray());
        window.getTaskPanel().getListMap().get(item.type).repaint();
      }

      window.getTextPanel().resetTextField();
      window.getTextPanel().getTextField().selectAll();
    });

    window.getButtonPanel().getEditButton().addActionListener(e -> {
      //TODO Сделать проверку выбранного таска(у нас 4 блока)
      boolean VALUE_IS_SELECTED = false;
      for (DataHandler.BoxVariable boxVariable : DataHandler.BOX_VARIABLES) {
        VALUE_IS_SELECTED |= window.getTaskPanel().getListMap().get(boxVariable.type).isSelectionEmpty();
      }

      if (!VALUE_IS_SELECTED) {
        return;
      }

      //Clean window
      window.getContentPane().removeAll();
      window.getContentPane().add(window.getEditPanel(), BorderLayout.CENTER);

      DataHandler.BoxType currentType = window.getTaskPanel().getCurrentBoxType();
      Task currentTask = (Task) window.getTaskPanel().getListMap().get(currentType).getSelectedValue();

      window.getEditPanel().setCurrentTask(currentTask);
      window.getEditPanel().getTaskNameField().setText(currentTask.getStatement());
      window.getEditPanel().setTaskStateBeforeChanges(new TaskState(currentTask));

      initEditPanel();

      window.getEditPanel().run();

      window.getContentPane().revalidate();
      window.getContentPane().repaint();
    });
  }

  private void initEditPanel() {
    window.getEditPanel().getButtonPanel().getSaveButton().addActionListener(e -> {
      String taskName = window.getEditPanel().getTaskNameField().getText().strip();
      if (taskName.isEmpty()) {
        return;
      } else {
        window.getEditPanel().getCurrentTask().setStatement(taskName);

        final boolean TASK_WAS_CHANGED =
            !window.getEditPanel().getTaskStateBeforeChanges().isEquals(window.getEditPanel().getCurrentTask());

        if (TASK_WAS_CHANGED) {
          window.getEditPanel().getCurrentTask().isChanged();
          window.repaintMainWindow();
          synchAfterChanging();
          return;
        }

        window.repaintMainWindow();
      }
    });

    window.getEditPanel().getButtonPanel().getDeleteButton().addActionListener(e -> {
      //TODO Вызвать диалоговое окно подтверждения удаления
      //TODO Доделать удаление таска из нужной коробки

      Task removingTask = window.getEditPanel().getCurrentTask();
      DataHandler.BoxType typeOfRemovingTask = window.getTaskPanel().getCurrentBoxType();

      handler.removeTask(removingTask);

      window.getTaskPanel().getListMap().get(typeOfRemovingTask).setListData(handler.getBox().get(typeOfRemovingTask).toArray());
      window.getTaskPanel().getListMap().get(typeOfRemovingTask).repaint();
      window.repaintMainWindow();
      synchAfterRemoving();
    });
  }

  private void synchFull() {
    synchAfterChanging();
    if (handler.getDBmanager().isONLINE()) {
      handler.updateAll();
    }

    changeWindowColorMode();
  }

  private void synchAfterChanging() {
    handler.setOfflineWorker();
    handler.pushAll();

    handler.setOnlineWorker();
    if (handler.getDBmanager().isONLINE()) {
      handler.pushAll();
    }

    changeWindowColorMode();
  }

  private void synchAfterRemoving() {
    handler.setOfflineWorker();
    handler.pushAll();

    handler.setOnlineWorker();
    if (handler.getDBmanager().isONLINE()) {
      handler.removeAll();
    }

    changeWindowColorMode();
  }


  private void changeWindowColorMode() {
    if (handler.getDBmanager().isONLINE()) {
      window.setBackground(Color.GREEN);
    } else {
      window.setBackground(Color.RED);
    }
  }
}
