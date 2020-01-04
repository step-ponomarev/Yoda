package edu.ponomarev.step.controller;

import edu.ponomarev.step.component.task.Task;
import edu.ponomarev.step.view.edit.EditPanel;
import edu.ponomarev.step.view.main.TaskPanel;
import edu.ponomarev.step.view.main.TextPanel;
import edu.ponomarev.step.view.main.Window;
import edu.ponomarev.step.manager.DataHandler;

import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.function.Consumer;

public class Controller {
  private Window window;
  private DataHandler handler;

  public Controller(Window window, DataHandler handler) {
    this.handler = handler;
    this.window = window;
  }

  public void initView() {
    window.run();
    initTaskPanel();
    initTextPanel();
    initBoxButtons();
    initButtonPanel();
  }

  private void initTaskPanel() {
    window.getTaskPanel().getList().setListData(handler.getTodayBox().toArray());
    window.getTaskPanel().getBoxLabel().setText("Today");
  }

  private void initBoxButtons() {
    for (JButton button : window.getBoxButtonsPanel().getBox()) {
      button.addActionListener(e -> {
        window.getTaskPanel().getList().setListData(handler.getBox(button.getText().strip()).toArray());
        window.getTaskPanel().getBoxLabel().setText(button.getText().strip());
      });
    }
  }

  private void initTextPanel() {
    final int ENTER = 10;
    window.getTextPanel().getTextField().addKeyListener( new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {
        if (e.getKeyCode() == ENTER) {
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

      window.getTaskPanel().refresh();
    });

    for (int i = 0; i < 4; ++i) {
      window.getTextPanel().getBoxList().addItem(new TextPanel.BoxItem(window.getBoxButtonsPanel().getBox()[i].getText(), DataHandler.BoxType.values()[i]));
    }
  }

  private void initButtonPanel() {
    final Consumer<EditPanel> repaintEditWindow =  panel -> {

      window.getContentPane().removeAll();

      window.getContentPane().add(panel, BorderLayout.CENTER);

      panel.getSaveButton().addActionListener(e2 -> {
        // TODO Сохранить измененный таск и т д
        window.repaintMainWindow();
      });

      panel.run();

      window.getContentPane().revalidate();
      window.getContentPane().repaint();
    };

    window.getButtonPanel().getAddButton().addActionListener(e -> {
      TextPanel.BoxItem item = (TextPanel.BoxItem) window.getTextPanel().getBoxList().getSelectedItem();

      switch (item.type) {
        case DAY:
          window.getTaskPanel().getList().setListData(handler.getTodayBox().toArray());
          break;

        case WEEK:
          window.getTaskPanel().getList().setListData(handler.getWeekBox().toArray());
          break;

        case LATE:
          window.getTaskPanel().getList().setListData(handler.getLateBox().toArray());
          break;

        default:
          window.getTaskPanel().getList().setListData(handler.getInbox().toArray());
          break;
      }

      String task = window.getTextPanel().getTextField().getText().strip();
      if (!task.isEmpty()) {
        handler.addTask(item.type, new Task(task));

        window.getTextPanel().getTextField().selectAll();

        if (item.getName().strip().equals(window.getTaskPanel().getBoxLabel().getText().strip())) {
          //window.getTaskPanel().refresh();
        }
      }
    });

    window.getButtonPanel().getEditButton().addActionListener(e -> {
      if (!window.getTaskPanel().getList().isSelectionEmpty()) {
        repaintEditWindow.accept(new EditPanel(window.getTaskPanel().getSelected()));
      }
    });
  }
}
