package dev.lukaszmichalak;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {

  protected static Stage mainStage;

  public static void main(String[] args) {
    launch();
  }

  @Override
  public void start(Stage mainStage) throws Exception {
    App.mainStage = mainStage;

    Image icon = new Image(getClass().getResourceAsStream("icon.png"));
    mainStage.getIcons().add(icon);

    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("app.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 270, 200);
    scene.getStylesheets().add(getClass().getResource("app.css").toExternalForm());

    mainStage.setTitle("Work hours");
    mainStage.setScene(scene);
    mainStage.setMinHeight(230);
    mainStage.setMinWidth(270);
    mainStage.show();
  }
}
