import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.util.Random;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        Controller c = new Controller();
        c.setUp(primaryStage);
    }
    public static void main (String [] args) {
        launch(args);
    }
}
