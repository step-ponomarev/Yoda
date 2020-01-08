package edu.ponomarev.step.controller;

import edu.ponomarev.step.component.task.Task;
import edu.ponomarev.step.view.edit.EditPanel;
import edu.ponomarev.step.view.main.Window;
import edu.ponomarev.step.manager.DataHandler;

import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.function.Consumer;

public class Controller {
  private class BoxRequestWrap {
    public DataHandler.BoxType type;
    public String boxName;

    public BoxRequestWrap(DataHandler.BoxType type, String boxName) {
      this.type = type;
      this.boxName = boxName;
    }

    @Override
    public String toString() {
      return boxName;
    }
  }

  private Window window;
  private DataHandler handler;
  private BoxRequestWrap [] boxVariables;

  public Controller(Window window, DataHandler handler) {
    this.handler = handler;
    this.window = window;
    this.boxVariables = new BoxRequestWrap [] {
        new BoxRequestWrap(DataHandler.BoxType.INBOX, "Inbox"),
        new BoxRequestWrap(DataHandler.BoxType.DAY, "Today"),
        new BoxRequestWrap(DataHandler.BoxType.WEEK, "Week"),
        new BoxRequestWrap(DataHandler.BoxType.LATE, "Late")
    };

  }

  public void initView() {
    window.run();
    initMainWindow();
    initTaskPanel();
    initBoxButtons();
    initTextPanel();
    initButtonPanel();
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
        for (BoxRequestWrap boxRequest : boxVariables) {
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

    for (BoxRequestWrap item : boxVariables) {
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
      final BoxRequestWrap item = (BoxRequestWrap) window.getTextPanel().getBoxList().getSelectedItem();

      String task = window.getTextPanel().getTextField().getText().strip();
      if (!task.isEmpty()) {
        handler.addTask(item.type, new Task(task));

        window.getTextPanel().getTextField().selectAll();

        if (item.type.equals(window.getTaskPanel().getCurrentBoxType())) {
          window.getTaskPanel().getList().setListData(handler.getBox().get(item.type).toArray());
          window.getTaskPanel().getList().repaint();
        }
      }
    });

    final Consumer<EditPanel> repaintEditWindow =  panel -> {
      window.getContentPane().removeAll();
      window.getContentPane().add(panel, BorderLayout.CENTER);

      panel.getButtonPanel().getSaveButton().addActionListener(e2 -> {
        String taskName = panel.getTaskNameField().getText().strip();
        if (taskName.isEmpty()) {
          // TODO Запустить диалог ошибки.
        } else {
          panel.getCurrentTask().setStatement(taskName);
          window.repaintMainWindow();
        }
      });

      panel.run();

      window.getContentPane().revalidate();
      window.getContentPane().repaint();
    };

    window.getButtonPanel().getEditButton().addActionListener(e -> {
      if (!window.getTaskPanel().getList().isSelectionEmpty()) {
        repaintEditWindow.accept(new EditPanel(window.getTaskPanel().getSelected()));
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
  }
}
