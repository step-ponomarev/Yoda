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

    defaultSynch();
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
        defaultSynch();
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
    window.getTaskPanel().getList().setListData(handler.getBox().get(DataHandler.BoxType.DAY).toArray());
    window.getTaskPanel().setCurrentBoxType(DataHandler.BoxType.DAY);
    window.getTaskPanel().getBoxLabel().setText("Today");
  }

  private void initBoxButtons() {
    for (JButton button : window.getBoxButtonsPanel().getBox()) {
      button.addActionListener(e -> {
        for (DataHandler.BoxRequestWrap boxRequest : DataHandler.BOX_VARIABLES) {
          if (boxRequest.boxName.equals(button.getText())) {
            window.getTaskPanel().setCurrentBoxType(boxRequest.type);
            window.getTaskPanel().getList().setListData(handler.getBox().get(boxRequest.type).toArray());
            window.getTaskPanel().getBoxLabel().setText(boxRequest.boxName);
            break;
          }
        }
      });
    }
  }

  private void initTextPanel() {
    final int ENTER = 10;

    for (DataHandler.BoxRequestWrap item : DataHandler.BOX_VARIABLES) {
      window.getTextPanel().getBoxList().addItem(item);
    }
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
      defaultSynch();
    });
  }

  private void initButtonPanel() {
    window.getButtonPanel().getAddButton().addActionListener(e -> {
      final DataHandler.BoxRequestWrap item = (DataHandler.BoxRequestWrap) window.getTextPanel().getBoxList().getSelectedItem();

      String taskStatement = window.getTextPanel().getTextField().getText().strip();
      if (!taskStatement.isEmpty()) {
        handler.addTask(item.type, new Task(taskStatement));

        window.getTextPanel().getTextField().selectAll();

        if (item.type.equals(window.getTaskPanel().getCurrentBoxType())) {
          window.getTaskPanel().getList().setListData(handler.getBox().get(item.type).toArray());
          window.getTaskPanel().getList().repaint();
        }
      }
    });

    window.getButtonPanel().getEditButton().addActionListener(e -> {
      if (window.getTaskPanel().getList().isSelectionEmpty()) {
        return;
      }

      window.getContentPane().removeAll();
      window.getContentPane().add(window.getEditPanel(), BorderLayout.CENTER);

      window.getEditPanel().setCurrentTask(window.getTaskPanel().getSelected());
      window.getEditPanel().getTaskNameField().setText(window.getTaskPanel().getSelected().getStatement().trim());
      window.getEditPanel().setTaskStateBeforeChanges(new TaskState(window.getTaskPanel().getSelected()));

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
        // TODO Запустить диалог ошибки.
      } else {
        window.getEditPanel().getCurrentTask().setStatement(taskName);

        if (!window.getEditPanel().getTaskStateBeforeChanges().isEquals(window.getEditPanel().getCurrentTask())) {
          window.getEditPanel().getCurrentTask().isChanged();
        }

        window.repaintMainWindow();
      }
    });
  }

  private void defaultSynch() {
    handler.setOfflineDataWorker();
    handler.pushDate();
    handler.setDataWorkerAuto();
    if (handler.getDBmanager().isONLINE()) {
      handler.pushDate();
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
