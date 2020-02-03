package edu.ponomarev.step.MVC.controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginWindowController {

  @FXML
  private PasswordField passwordField;

  @FXML
  private TextField loginField;

  @FXML
  void loginButton() {
    System.out.println(passwordField.getText() + " " + loginField.getText());
  }

}
