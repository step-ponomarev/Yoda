package edu.ponomarev.step.MVC.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class WindowTestController {

  @FXML
  private Label changableLabel;

  @FXML
  void changeLabelButtonAction(ActionEvent event) {

    System.out.println("SUKA");
  }
}
