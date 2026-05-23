package pebblesweep.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.tinylog.Logger;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.Objects;

/**
 * Controller class for the start menu of the Pebble Sweep game.
 * Transitions to the chosen game board.
 */
public class StartController {

    /**
     * Handles the action event triggered by clicking the two player button.
     * Loads the main game board and replaces the current scene on the active stage.
     *
     * @param event the action event containing information about the click
     */
    @FXML
    public void startTwoPlayer(ActionEvent event) {
        try {
            Logger.info("Starting two player game");
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/game.fxml")));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            Logger.error(e, "Failed to load game.fxml");
        }
    }

    /**
     * Handles the action event triggered by clicking the singleplayer button.
     * Currently not implemented
     *
     * @param event the action event containing information about the click
     */
    @FXML
    public void startSinglePlayer(ActionEvent event) {
        Logger.info("Single player mode is not yet implemented.");
    }
}
