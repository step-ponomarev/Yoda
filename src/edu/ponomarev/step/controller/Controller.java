package edu.ponomarev.step.controller;

import edu.ponomarev.step.view.Main.Window;
import edu.ponomarev.step.manager.DataHandler;

import javax.swing.*;

public class Controller {
  private Window window;
  private DataHandler handler;

  public Controller(Window window, DataHandler handler) {
    this.handler = handler;
    this.window = window;

  }

  public void initView() {
    window.run();
    initBoxButtons();

  }

  private void initBoxButtons() {
    for (JButton button : window.getBoxPanel().getBox()) {
      button.addActionListener(e -> {

      });
    }
  }
}
