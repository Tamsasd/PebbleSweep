package pebblesweep.gui;

import common.TwoPhaseMoveState;
import game.State;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.tinylog.Logger;
import pebblesweep.model.PebbleSweepState;
import pebblesweep.model.Position;

import java.util.Objects;

public class PebbleSweepController {
    @FXML
    private Label statusLabel;

    @FXML
    private GridPane boardGrid;

    @FXML
    private Label player1Label;

    @FXML
    private Label player2Label;

    private PebbleSweepState gameState;

    private Position startPos = null;

    private StackPane[][] cells = new StackPane[4][4];
    private Image pebbleImage;

    @FXML
    public void initialize() {
        Logger.info("Initializing PebbleSweepController");
        this.gameState = new PebbleSweepState();

        try {
            pebbleImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pebble.gif")));
        }
        catch (Exception e) {
            Logger.error("Pebble image not found in resources folder.");
        }

        drawBoard();
    }

    private void drawBoard() {
        Logger.info("Drawing board.");
        boardGrid.getChildren().clear();

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                Position currentPos = new Position(row, col);

                StackPane cell = new StackPane();

                if (gameState.isLegalToMoveFrom(currentPos)) {
                    ImageView pebbleView = new ImageView(pebbleImage);
                    pebbleView.setPreserveRatio(true);
                    pebbleView.setFitHeight(70);
                    cell.getChildren().add(pebbleView);
                }

                cell.setOnMouseClicked(e -> handleCellClick(currentPos));

                boardGrid.add(cell, col, row);
            }
        }
    }

    private void handleCellClick(Position clickedPos) {
        if (gameState.isGameOver()) {
            return;
        }

        if (startPos == null && gameState.isLegalToMoveFrom(clickedPos)) {
            startPos = clickedPos;
            Logger.debug("Starting endpoint: {}", startPos);
        }
        else {
            TwoPhaseMoveState.TwoPhaseMove<Position> move = new TwoPhaseMoveState.TwoPhaseMove<>(startPos, clickedPos);

            try {
                gameState.makeMove(move);
                Logger.info("Move executed: {}", gameState.moveToString(move));
                startPos = null;
                updateUI();
            }
            catch (IllegalArgumentException e) {
                Logger.warn("Invalid move attempted: {}", gameState.moveToString(move));
                drawBoard();
                showError("Illegal move.","The selected move does not comply with the rules.");
            }
        }
    }

    private void updateUI() {
        drawBoard();

        State.Status status = gameState.getStatus();
        statusLabel.setText(status.toString());

        if (gameState.getNextPlayer() == State.Player.PLAYER_1) {
            player1Label.setStyle("-fx-background-color: #ADD8E6; -fx-padding: 10px; -fx-background-radius: 5px;");
            player2Label.setStyle("-fx-background-color: transparent; -fx-padding: 10px;");
        } else {
            player1Label.setStyle("-fx-background-color: transparent; -fx-padding: 10px;");
            player2Label.setStyle("-fx-background-color: #ADD8E6; -fx-padding: 10px; -fx-background-radius: 5px;");
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
