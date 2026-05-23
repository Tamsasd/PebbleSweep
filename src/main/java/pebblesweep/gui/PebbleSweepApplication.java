package pebblesweep.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PebbleSweepApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/game.fxml"));
        stage.setTitle("Pebble Sweep Game");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }
}
