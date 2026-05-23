package pebblesweep.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The entry point for the graphical user interface (GUI) of the Pebble Sweep game.
 * This class extends the JavaFX {@link Application} and is responsible for loading
 * the main FXML layout and setting up the primary window.
 */
public class PebbleSweepApplication extends Application {

    /**
     * Starts the JavaFX application.
     * Loads the FXML file and configures the primary stage.
     *
     * @param stage the primary stage for this application, onto which the scene will be set
     * @throws Exception if the FXML resource cannot be loaded
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/start.fxml"));
        stage.setTitle("Pebble Sweep Game");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }
}