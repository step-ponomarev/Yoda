package edu.ponomarev.step.MVC.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WindowFactory {
  public void start() throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("../../windowTest.fxml"));
    Parent parent = loader.load();

    Stage stage = new Stage();
    stage.setScene(new Scene(parent));
    stage.show();
  }
}
