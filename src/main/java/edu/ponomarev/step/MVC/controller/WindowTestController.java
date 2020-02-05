package edu.ponomarev.step.MVC.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class WindowTestController {

  @FXML
  private Label changableLabel;

  @FXML
  void changeLabelButtonAction(ActionEvent event) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("../../windowTest.fxml"));
    Parent parent = loader.load();

    Stage stage = new Stage();
    stage.setScene(new Scene(parent));
    stage.show();
  }
}
