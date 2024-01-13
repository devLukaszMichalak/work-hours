package dev.lukaszmichalak;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    
    protected static Stage mainStage;
    
    public static void main(String[] args) {
        launch();
    }
    
    @Override
    public void start(Stage mainStage) throws Exception {
        Main.mainStage = mainStage;
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 270, 200);
        scene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
        
        mainStage.setTitle("Work hours");
        mainStage.setScene(scene);
        mainStage.setResizable(false);
        mainStage.show();
    }
}
