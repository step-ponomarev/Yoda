package edu.ponomarev.step.MVC.controller;

import edu.ponomarev.step.component.task.InformatedTask;
import edu.ponomarev.step.component.task.Task;
import edu.ponomarev.step.MVC.view.Window;
import edu.ponomarev.step.MVC.model.TaskWorker;
import edu.ponomarev.step.component.taskContainer.termContainer.ContainerVariable;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

// TODO Переписать помойку

public class Controller {
  private Window window;
  private TaskWorker taskWorker;

  public Controller(Window window, TaskWorker taskWorker) {
    this.taskWorker = taskWorker;
    this.window = window;
  }

  public void initView() {
    window.run();

    initMainWindow();
    initTaskPanel();
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
        return;
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
    window.getTaskPanel().setListMap(taskWorker.getContainer());
    window.getTaskPanel().run();

    for (var currentVariable : ContainerVariable.BOX_VARIABLES) {
      window.getTaskPanel().getListMap().get(currentVariable.type).addListSelectionListener(e -> {
        //Clear selection of every box besides current.
        for (var anotherVariable : ContainerVariable.BOX_VARIABLES) {
          if (!anotherVariable.type.equals(currentVariable.type)) {
            window.getTaskPanel().getListMap().get(anotherVariable.type).clearSelection();
          }
        }

        window.getTaskPanel().setSelectedType(currentVariable.type);
      });
    }
  }

  private void initTextPanel() {
    final int ENTER = 10;

    for (var containerType : ContainerVariable.BOX_VARIABLES) {
      window.getTextPanel().getContainerTypeList().addItem(containerType);
    }

    window.getTextPanel().resetTextField();
    window.getTextPanel().getTaskStatementField().selectAll();
    window.getTextPanel().getContainerTypeList().setSelectedIndex(ContainerVariable.ContainerType.INBOX.toInteger());

    window.getTextPanel().getTaskStatementField().addKeyListener(new KeyListener() {
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
      window.getTaskPanel().repaintAllModules();
    });
  }

  private void initButtonPanel() {
    //AddTask Button
    window.getButtonPanel().getAddButton().addActionListener(e -> {
      final var selectedItem =
          (ContainerVariable) window.getTextPanel().getContainerTypeList().getSelectedItem();

      String taskStatement = window.getTextPanel().getTaskStatementField().getText().strip();
      if (!taskStatement.isEmpty()) {
        taskWorker.addTask(new InformatedTask(new Task(taskStatement), selectedItem.type));

        window.getTextPanel().getTaskStatementField().selectAll();

        window.getTaskPanel().getListMap().get(selectedItem.type).setListData(taskWorker.getContainer().get(selectedItem.type).getList().toArray());
        window.getTaskPanel().getListMap().get(selectedItem.type).repaint();
      }

      window.getTextPanel().resetTextField();
      window.getTextPanel().getTaskStatementField().selectAll();
    });

    window.getButtonPanel().getEditButton().addActionListener(e -> {
      boolean VALUE_IS_SELECTED = false;
      for (var containerVariable : ContainerVariable.BOX_VARIABLES) {
        VALUE_IS_SELECTED |= window.getTaskPanel().getListMap().get(containerVariable.type).isSelectionEmpty();
      }

      if (!VALUE_IS_SELECTED) {
        return;
      }

      //Clean window
      window.getContentPane().removeAll();
      window.getContentPane().add(window.getEditPanel(), BorderLayout.CENTER);

      InformatedTask informatedTask = new InformatedTask(window.getTaskPanel().getSelectedTask(),
          window.getTaskPanel().getSelectedType());

      window.getEditPanel().setInformatedTask(informatedTask);
      window.getEditPanel().getTaskNameField().setText(informatedTask.getStatement());

      initEditPanel();

      window.getEditPanel().run();

      window.getContentPane().revalidate();
      window.getContentPane().repaint();
    });
  }

  private void initEditPanel() {
    //SaveTusk button
    window.getEditPanel().getButtonPanel().getSaveButton().addActionListener(e -> {
      String taskName = window.getEditPanel().getTaskNameField().getText().strip();

      if (taskName.isEmpty()) {
        return;
      } else {
        final boolean STATEMENT_CHANGED = !taskName.equals(window.getEditPanel().getInformatedTask().getStatement());

        if (STATEMENT_CHANGED) {
          window.getEditPanel().getInformatedTask().setStatement(taskName);
          window.getEditPanel().getInformatedTask().updateTimeOfLastChange();
          taskWorker.updateTask(window.getEditPanel().getInformatedTask());
        }

        window.repaintMainWindow();
      }
    });

    //RemoveTask button
    window.getEditPanel().getButtonPanel().getRemoveButton().addActionListener(e -> {
      InformatedTask removingTask = window.getEditPanel().getInformatedTask();
      var typeOfRemovingTask = removingTask.getContainerType();

      taskWorker.removeTask(removingTask);

      window.getTaskPanel().getListMap().get(typeOfRemovingTask).setListData(taskWorker.getContainer().get(typeOfRemovingTask).getList().toArray());
      window.getTaskPanel().getListMap().get(typeOfRemovingTask).repaint();
      window.repaintMainWindow();
    });
  }
}
