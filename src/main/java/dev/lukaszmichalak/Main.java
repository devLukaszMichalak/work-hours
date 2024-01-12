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
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        
        mainStage.setScene(scene);
        mainStage.show();
    }
}
