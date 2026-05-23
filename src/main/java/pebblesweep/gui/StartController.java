package pebblesweep.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.tinylog.Logger;

import javafx.event.ActionEvent;
import pebblesweep.model.GameResult;
import pebblesweep.model.ResultManager;

import java.io.IOException;
import java.util.List;
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

    @FXML
    public void showLeaderboard(ActionEvent event) {
        Logger.info("Opening leaderboard...");
        List<GameResult> results = ResultManager.loadResults();

        StringBuilder sb = new StringBuilder();
        if (results.isEmpty()) {
            sb.append("No games played yet.");
        } else {
            for (int i = 0; i < results.size(); i++) {
                GameResult r = results.get(i);
                sb.append(i + 1).append(". ").append(r.getWinner())
                        .append(" (").append(r.getDate()).append(")\n");
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Leaderboard");
        alert.setHeaderText("Past Game Results");
        alert.setContentText(sb.toString());
        alert.showAndWait();
    }
}
