package pebblesweep.gui;

import common.TwoPhaseMoveState;
import game.State;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
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

    private final String DEFAULT_STYLE = "-fx-background-color: transparent";
    private final String HIGHLIGHT_STYLE = "-fx-background-color: #E0E0E0";
    private final String STARTPOINT_HIGHLIGHT_STYLE = "-fx-background-color: green";
    private final String CURRENT_PLAYER_STYLE = "-fx-background-color: #ADD8E6; -fx-padding: 10px; -fx-background-radius: 5px;";
    private final String OTHER_PLAYER_STYLE = "-fx-background-color: transparent; -fx-padding: 10px;";

    @FXML
    public void initialize() {
        Logger.info("Initializing PebbleSweepController");
        this.gameState = new PebbleSweepState();

        loadPebbleImage();
        setupBoard();
        updateUI();
    }

    private void loadPebbleImage() {
        try {
            pebbleImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pebble.gif")));
        }
        catch (Exception e) {
            Logger.error("Pebble image not found in resources folder.");
        }
        Logger.info("Pebble image loaded into memory.");
    }

    private void setupBoard() {
        Logger.info("Setting up board.");
        boardGrid.getChildren().clear();

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                Position currentPos = new Position(row, col);
                StackPane cell = new StackPane();

                cell.setOnMouseClicked(e -> {
                    var button = e.getButton();
                    if (button == MouseButton.SECONDARY) {
                        cancelSelection();
                    }
                    else if (button == MouseButton.PRIMARY) {
                        handleCellClick(currentPos);
                    }
                });

                cell.setOnMouseEntered(e -> updateBackgrounds(currentPos));
                cell.setOnMouseExited( e-> updateBackgrounds(null));

                cells[row][col] = cell;
                boardGrid.add(cell, col, row);
            }
        }
    }

    private void drawPebbles() {
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                drawPebble(row, col);
            }
        }
    }

    private void drawPebble(int row, int col) {
        cells[row][col].getChildren().clear();

        if (gameState.isLegalToMoveFrom(new Position(row, col))) {
            ImageView pebbleView = new ImageView(pebbleImage);
            pebbleView.setPreserveRatio(true);
            pebbleView.setFitHeight(70);
            cells[row][col].getChildren().add(pebbleView);
        }
    }

    private void handleCellClick(Position clickedPos) {
        if (gameState.isGameOver()) {
            return;
        }

        if (startPos == null && gameState.isLegalToMoveFrom(clickedPos)) {
            startPos = clickedPos;
            Logger.debug("Starting endpoint: {}", startPos);
            updateBackgrounds(clickedPos);
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
                cancelSelection();
                showError("Illegal move.","The selected move does not comply with the rules.");
            }
        }
    }

    private void updateUI() {
        drawPebbles();
        updateBackgrounds(null);
        setStatusLabel();
        setPlayerLabel();
    }

    private void setStatusLabel() {
        statusLabel.setText(gameState.getStatus().toString());
    }

    private void setPlayerLabel() {
        if (gameState.getNextPlayer() == State.Player.PLAYER_1) {
            player1Label.setStyle(CURRENT_PLAYER_STYLE);
            player2Label.setStyle(OTHER_PLAYER_STYLE);
        } else {
            player1Label.setStyle(OTHER_PLAYER_STYLE);
            player2Label.setStyle(CURRENT_PLAYER_STYLE);
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

    private void cancelSelection() {
        if (startPos != null) {
            Logger.debug("Selection cancelled.");
            startPos = null;
            updateBackgrounds(null);
        }
    }

    private void updateBackgrounds(Position hoverPos) {
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                cells[row][col].setStyle(DEFAULT_STYLE);

                if (startPos != null && startPos.row() == row && startPos.column() == col) {
                    cells[row][col].setStyle(STARTPOINT_HIGHLIGHT_STYLE);
                }
            }
        }

        if (hoverPos == null || gameState.isGameOver()) return;

        if (startPos == null) {
            cells[hoverPos.row()][hoverPos.column()].setStyle(HIGHLIGHT_STYLE);
        }
        else {
            TwoPhaseMoveState.TwoPhaseMove<Position> potentialMove = new TwoPhaseMoveState.TwoPhaseMove<>(startPos, hoverPos);

            if (gameState.isLegalMove(potentialMove)) {
                int startRow = Math.min(startPos.row(), hoverPos.row());
                int endRow = Math.max(startPos.row(), hoverPos.row());
                int startCol = Math.min(startPos.column(), hoverPos.column());
                int endCol = Math.max(startPos.column(), hoverPos.column());

                for (int row = startRow; row <= endRow; row++) {
                    for (int col = startCol; col <= endCol; col++) {
                        if (!(row == startPos.row() && col == startPos.column())) {
                            cells[row][col].setStyle(HIGHLIGHT_STYLE);
                        }
                    }
                }
            }
        }
    }
}
