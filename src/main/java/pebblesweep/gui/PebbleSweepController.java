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
import pebblesweep.model.GameResult;
import pebblesweep.model.PebbleSweepState;
import pebblesweep.model.Position;
import pebblesweep.model.ResultManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Controller class for the Pebble Sweep game GUI.
 */
public class PebbleSweepController {

    /**
     * The grid pane representing the game board UI.
     */
    @FXML
    private GridPane boardGrid;

    /**
     * The label indicating Player 1.
     */
    @FXML
    private Label player1Label;

    /**
     * The label indicating Player 2.
     */
    @FXML
    private Label player2Label;

    /**
     * The name of Player 1.
     */
    private String p1Name = "PLAYER_1";

    /**
     * The name of Player 2.
     */
    private String p2Name = "PLAYER_2";

    /**
     * The current game state model.
     */
    private PebbleSweepState gameState;

    /**
     * The starting position of the current move.
     */
    private Position startPos = null;

    /**
     * The visual representation of the board cells.
     */
    private StackPane[][] cells = new StackPane[4][4];

    /**
     * The image used to display the pebbles.
     */
    private Image pebbleImage;

    /**
     * The default style for an empty cell.
     */
    private final String DEFAULT_STYLE = "-fx-background-color: transparent";

    /**
     * The style for a highlighted cell during hover.
     */
    private final String HIGHLIGHT_STYLE = "-fx-background-color: #E0E0E0";

    /**
     * The style for the selected starting position.
     */
    private final String STARTPOINT_HIGHLIGHT_STYLE = "-fx-background-color: green";

    /**
     * The style for the active player's label.
     */
    private final String CURRENT_PLAYER_STYLE = "-fx-background-color: #ADD8E6; -fx-padding: 10px; -fx-background-radius: 5px;";

    /**
     * The style for the inactive player's label.
     */
    private final String OTHER_PLAYER_STYLE = "-fx-background-color: transparent; -fx-padding: 10px;";

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded. Sets up the initial game state,
     * loads resources and draws the initial board.
     */
    @FXML
    public void initialize() {
        Logger.info("Initializing PebbleSweepController");
        this.gameState = new PebbleSweepState();

        loadPebbleImage();
        setupBoard();
        updateUI();
    }

    /**
     * Loads the pebble image from the application resources.
     * Logs an error if the image file cannot be found.
     */
    private void loadPebbleImage() {
        try {
            pebbleImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pebble.gif")));
            Logger.info("Pebble image loaded into memory.");
        }
        catch (Exception e) {
            Logger.error("Pebble image not found in resources folder.");
        }
    }

    /**
     * Prepares the GridPane by creating and adding interactive cells.
     * Clears any existing children before generating the new 4x4 grid.
     */
    private void setupBoard() {
        Logger.info("Setting up board.");
        boardGrid.getChildren().clear();

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                Position currentPos = new Position(row, col);
                StackPane cell = createCell(currentPos);

                cells[row][col] = cell;
                boardGrid.add(cell, col, row);
            }
        }
    }

    /**
     * Creates a single interactive cell for the game board, and
     * binds mouse click and hover events to it.
     *
     * @param pos the logical position of the cell on the board
     * @return the configured StackPane cell
     */
    private StackPane createCell(Position pos) {
        StackPane cell = new StackPane();

        cell.setOnMouseClicked(e -> {
            var button = e.getButton();
            if (button == MouseButton.SECONDARY) {
                cancelSelection();
            }
            else if (button == MouseButton.PRIMARY) {
                handleCellClick(pos);
            }
        });

        cell.setOnMouseEntered(e -> updateBackgrounds(pos));
        cell.setOnMouseExited( e-> updateBackgrounds(null));

        return cell;
    }

    /**
     * Draws all pebbles on the board by iterating through all grid cells.
     */
    private void drawPebbles() {
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                drawPebble(row, col);
            }
        }
    }

    /**
     * Draws a single pebble image in a specific cell, if the game
     * model indicates that the position specified contains a pebble.
     *
     * @param row the row index of the cell
     * @param col the column index of the cell
     */
    private void drawPebble(int row, int col) {
        cells[row][col].getChildren().clear();

        if (gameState.isLegalToMoveFrom(new Position(row, col))) {
            ImageView pebbleView = new ImageView(pebbleImage);
            pebbleView.setPreserveRatio(true);
            pebbleView.setFitHeight(70);
            cells[row][col].getChildren().add(pebbleView);
        }
    }

    /**
     * Handles primary mouse clicks on a board cell.
     * Detemines whether to start a new move or complete an ongoing move.
     *
     * @param clickedPos the logical position of the clicked cell
     */
    private void handleCellClick(Position clickedPos) {
        if (gameState.isGameOver()) {
            return;
        }

        if (gameState.isLegalToMoveFrom(clickedPos))
        {
            if (startPos == null) {
                setStartPosition(clickedPos);
            }
            else {
                setEndPosition(clickedPos);
            }
        }

    }

    /**
     * Sets the starting position for a two-phase move and updates the corresponding UI highlights.
     *
     * @param pos the starting position selected by the player
     */
    private void setStartPosition(Position pos) {
        startPos = pos;
        Logger.debug("Starting endpoint: {}", startPos);
        updateBackgrounds(startPos);
    }

    /**
     * Attempts to execute a move ending at the specified position.
     * Updates the UI if the move is valid.
     * Shows and error dialog, if the move is invalid.
     *
     * @param pos the target end position for the move
     */
    private void setEndPosition(Position pos) {
        TwoPhaseMoveState.TwoPhaseMove<Position> move = new TwoPhaseMoveState.TwoPhaseMove<>(startPos, pos);

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

    /**
     * Syncronizes the GUI with the current game state.
     */
    private void updateUI() {
        drawPebbles();
        updateBackgrounds(null);
        setPlayerLabel();
        checkGameOver();
    }

    /**
     * Checks for a game over condition.
     * If the game is over, shows a game over dialog and saves the results to a JSON file.
     */
    private void checkGameOver() {
        if (gameState.isGameOver()) {
            String winner = gameState.isWinner(State.Player.PLAYER_1) ? "PLAYER_1" : "PLAYER_2";
            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            ResultManager.saveResult(new GameResult(winner, date));

            showInfo("Game Over!", gameState.getStatus().equals(State.Status.PLAYER_1_WINS) ? p1Name + " wins!" : p2Name + " wins!");
        }
    }

    /**
     * Updates the styling of the player labels to visually indicate whose turn it is.
     */
    private void setPlayerLabel() {
        player1Label.setText(p1Name);
        player2Label.setText(p2Name);
        if (gameState.getNextPlayer() == State.Player.PLAYER_1) {
            player1Label.setStyle(CURRENT_PLAYER_STYLE);
            player2Label.setStyle(OTHER_PLAYER_STYLE);
        } else {
            player1Label.setStyle(OTHER_PLAYER_STYLE);
            player2Label.setStyle(CURRENT_PLAYER_STYLE);
        }
    }

    /**
     * Displays a blocking error alert dialog.
     *
     * @param title the title of the alert window
     * @param message the detailed error message
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Displays a blocking informational alert dialog.
     *
     * @param title the title of the alert window
     * @param message the detailed informational message
     */
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Cancels the currently selected start position and resets
     * the cell highlights.
     */
    private void cancelSelection() {
        if (startPos != null) {
            Logger.debug("Selection cancelled.");
            startPos = null;
            updateBackgrounds(null);
        }
    }

    /**
     * Sets the background coloring of all cells to reflect the current
     * selection state and mouse hover position.
     *
     * @param hoverPos the position currently being hovered over. {@code null}, if outside the board
     */
    private void updateBackgrounds(Position hoverPos) {
        resetBackgrounds();

        setStartpointBackground();

        if (hoverPos == null || gameState.isGameOver()) return;

        if (startPos == null) {
            setHoverBackground(hoverPos);
        }
        else {
            setPathBackground(hoverPos);
        }
    }

    /**
     * Clears all highlighting by applying the default style to every cell.
     */
    private void resetBackgrounds() {
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                cells[row][col].setStyle(DEFAULT_STYLE);
            }
        }
    }

    /**
     * Highlights the currently selected starting position, if exists.
     */
    private void setStartpointBackground() {
        if (startPos != null) {
            cells[startPos.row()][startPos.column()].setStyle(STARTPOINT_HIGHLIGHT_STYLE);
        }
    }

    /**
     * Applies a hover highlight to the specified position, if it
     * contains a pebble.
     *
     * @param hoverPos the position to highlight
     */
    private void setHoverBackground(Position hoverPos) {
        if (gameState.isLegalToMoveFrom(hoverPos)) {
            cells[hoverPos.row()][hoverPos.column()].setStyle(HIGHLIGHT_STYLE);
        }
    }

    /**
     * Highlights the valid straight-line path between the start and
     * currently hovered positions.
     *
     * @param hoverPos the endpoint of the potential path being hovered
     */
    private void setPathBackground(Position hoverPos) {
        TwoPhaseMoveState.TwoPhaseMove<Position> potentialMove = new TwoPhaseMoveState.TwoPhaseMove<>(startPos, hoverPos);

        if (gameState.isLegalMove(potentialMove)) {
            int startRow = Math.min(startPos.row(), hoverPos.row());
            int endRow = Math.max(startPos.row(), hoverPos.row());
            int startCol = Math.min(startPos.column(), hoverPos.column());
            int endCol = Math.max(startPos.column(), hoverPos.column());

            for (int row = startRow; row <= endRow; row++) {
                for (int col = startCol; col <= endCol; col++) {
                    if (!(row == startPos.row() && col == startPos.column())) {
                        setHoverBackground(new Position(row, col));
                    }
                }
            }
        }
    }

    /**
     * Sets player names based on the provided names.
     *
     * @param p1 the name of player 1
     * @param p2 the name of player 2
     */
    public void setPlayerNames(String p1, String p2) {
        this.p1Name = p1;
        this.p2Name = p2;
        setPlayerLabel();
    }
}
