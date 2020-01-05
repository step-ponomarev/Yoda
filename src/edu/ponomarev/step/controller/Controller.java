package edu.ponomarev.step.controller;

import edu.ponomarev.step.component.task.Task;
import edu.ponomarev.step.view.edit.EditPanel;
import edu.ponomarev.step.view.main.Window;
import edu.ponomarev.step.manager.DataHandler;

import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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
    initTaskPanel();
    initBoxButtons();
    initTextPanel();
    initButtonPanel();
  }

  private void initTaskPanel() {
    window.getTaskPanel().getList().setListData(handler.getBox(DataHandler.BoxType.DAY).toArray());
    window.getTaskPanel().setCurrentBoxType(DataHandler.BoxType.DAY);
    window.getTaskPanel().getBoxLabel().setText("Today");
  }

  private void initBoxButtons() {
    for (JButton button : window.getBoxButtonsPanel().getBox()) {
      button.addActionListener(e -> {
        for (BoxRequestWrap boxWrap : boxVariables) {
          if (boxWrap.boxName.equals(button.getText())) {
            window.getTaskPanel().setCurrentBoxType(boxWrap.type);
            window.getTaskPanel().getList().setListData(handler.getBox(boxWrap.type).toArray());
            window.getTaskPanel().getBoxLabel().setText(boxWrap.boxName);
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
      handler.setDataWorker();
      handler.pushDate();
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
          window.getTaskPanel().getList().setListData(handler.getBox(item.type).toArray());
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
}
