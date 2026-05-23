package pebblesweep.model;

import game.TwoPhaseMoveState;
import org.tinylog.Logger;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

import static java.lang.Math.abs;

/**
 * Represents a state of the Pebble Sweep game.
 * The game is played on a 4x4 board where players take turns removing 1 to 4 pebbles
 * from a continuous horizontal or vertical line. The player forced to take the last pebble loses.
 */
public class PebbleSweepState implements TwoPhaseMoveState<Position, PebbleSweepState> {

    private Player player;

    private boolean[][] board = {
            {true, true, true, true},
            {true, true, true, true},
            {true, true, true, true},
            {true, true, true, true}
    };

    private int numberOfRows = board.length;
    private int numberOfColumns = board[0].length;

    /**
     * Creates a new, initial state of the Pebble Sweep game.
     * The board is fully populated with pebbles, and {@link Player#PLAYER_1} moves first.
     */
    public PebbleSweepState() {
        this.player = Player.PLAYER_1;
    }

    /**
     * Creates a game state specified by the next player and the board's configuration.
     *
     * @param player the player to move
     * @param board the current state of the board
     */
    PebbleSweepState(boolean[][] board, Player player) {
        this.player = player;
        this.numberOfRows = board.length;
        this.numberOfColumns = board[0].length;
        this.board = new boolean[this.numberOfRows][this.numberOfColumns];

        for (int i = 0; i < this.numberOfRows; i++) {
            this.board[i] = Arrays.copyOf(board[i], board[i].length);
        }
    }

    /**
     * Creates a deep copy of the specified state.
     *
     * @param other the state to be copied
     */
    public PebbleSweepState(PebbleSweepState other) {
        this.player = other.player;
        this.numberOfColumns = other.numberOfColumns;
        this.numberOfRows = other.numberOfRows;

        this.board = new boolean[other.numberOfRows][other.numberOfColumns];

        for (int i = 0; i < other.numberOfRows; i++) {
            this.board[i] = Arrays.copyOf(other.board[i], other.board[i].length);
        }
    }

    /**
     * {@return whether it is possible to make a move from the position
     * specified}
     *
     * @param from represents where to move from
     */
    @Override
    public boolean isLegalToMoveFrom(Position from) {
        return board[from.row()][from.column()];
    }

    /**
     * {@return the player who moves next}
     */
    @Override
    public Player getNextPlayer() {
        return player;
    }

    /**
     * {@return whether the game is over, by checking if there's any pebbles left to sweep}
     */
    @Override
    public boolean isGameOver() {
        for (int row = 0; row < numberOfRows; row++) {
            for (int col = 0; col < numberOfColumns; col++) {
                if (board[row][col]) return false;
            }
        }
        return true;
    }

    /**
     * {@return the status of the game}
     *
     * Draw status is not possible.
     */
    @Override
    public Status getStatus() {
        if (isGameOver()) {
            return isWinner(Player.PLAYER_1) ? Status.PLAYER_1_WINS : Status.PLAYER_2_WINS;
        }
        else return Status.IN_PROGRESS;
    }

    /**
     * {@return whether the player specified has won the game}
     *
     * @param player the player to be tested for win
     */
    @Override
    public boolean isWinner(Player player) {
        return isGameOver()
                && player.equals(this.player); // the player that picks the last pebble loses
    }

    /**
     * {@return whether the two endpoints are in the same column}
     *
     * @param move the move specified by a starting and ending position
     */
    private boolean isInSameColumn(TwoPhaseMove<Position> move) {
        return move.from().column() == move.to().column();
    }

    /**
     * {@return whether the two endpoints are in the same row}
     *
     * @param move the move specified by a starting and ending position
     */
    private boolean isInSameRow(TwoPhaseMove<Position> move)
    {
        return move.from().row() == move.to().row();
    }

    /**
     * {@return whether the row or column of the move has any spaces in between the endpoints}
     * This method should be called if and only if {@link #isInSameColumn(TwoPhaseMove)} or
     * {@link #isInSameRow(TwoPhaseMove)} returns {@code true}.
     *
     * @param move the move specified by a starting and ending position
     */
    private boolean isValidDistance(TwoPhaseMove<Position> move) throws IllegalArgumentException {
        int distance;
        if (isInSameColumn(move)) {
            distance = abs(move.from().row() - move.to().row());
        }
        else if (isInSameRow(move)){
            distance = abs(move.from().column() - move.to().column());
        }
        else {
            throw new IllegalArgumentException("Illegal Move: endpoints must be in the same row or column!");
        }
        return distance < 4;
    }

    /**
     * {@return whether the row or column of the move has any spaces in between the endpoints}
     * This method should be called if and only if {@link #isInSameColumn(TwoPhaseMove)} or
     * {@link #isInSameRow(TwoPhaseMove)} returns {@code true}.
     *
     * @param move the move specified by a starting and ending position
     */
    private boolean hasGap(TwoPhaseMove<Position> move) throws IllegalArgumentException {
        if (isInSameColumn(move)) {
            int col = move.from().column();
            int startRow = Math.min(move.from().row(), move.to().row());
            int endRow = Math.max(move.from().row(), move.to().row());
            for (int row = startRow; row <= endRow; row++) {
                if (!board[row][col]) return true;
            }
            return false;
        }
        else if (isInSameRow(move)){
            int row = move.from().row();
            int startCol = Math.min(move.from().column(), move.to().column());
            int endCol = Math.max(move.from().column(), move.to().column());
            for (int col = startCol; col <= endCol; col++) {
                if (!board[row][col]) return true;
            }
            return false;
        }
        else {
            throw new IllegalArgumentException("Illegal Move: endpoints must be in the same row or column!");
        }
    }

    /**
     * {@return whether the move provided can be applied to the state}
     *
     * @param move represents the move to be made
     */
    @Override
    public boolean isLegalMove(TwoPhaseMove<Position> move) {
        return (isInSameColumn(move) || isInSameRow(move))
                && isValidDistance(move)
                && !hasGap(move);
    }


    /**
     * Applies the move provided to the state. This method should be called if
     * and only if {@link #isLegalMove(TwoPhaseMove)} returns {@code true}.
     *
     * @param move represents the move to be made
     * @throws IllegalArgumentException if and illegal move is provided as a parameter
     */
    @Override
    public void makeMove(TwoPhaseMove<Position> move) throws IllegalArgumentException{
        if (!isLegalMove(move)) {
            Logger.warn("Attempted illegal move: {}", moveToString(move));
            throw new IllegalArgumentException("Cannot make illegal move: " + moveToString(move));
        }

        if (isInSameColumn(move)) {
            int col = move.from().column();
            int startRow = Math.min(move.from().row(), move.to().row());
            int endRow = Math.max(move.from().row(), move.to().row());
            for (int row = startRow; row <= endRow; row++) {
                board[row][col] = false;
            }
        }
        else {
            int row = move.from().row();
            int startCol = Math.min(move.from().column(), move.to().column());
            int endCol = Math.max(move.from().column(), move.to().column());
            for (int col = startCol; col <= endCol; col++) {
                board[row][col] = false;
            }
        }

        Logger.trace("Player {} executed move {}", this.player, moveToString(move));
        this.player = this.player.opponent();
    }

    /**
     * Computes and returns the set of all legal horizontal moves for a given row.
     *
     * @param row the index of the row to check for legal moves
     * @return a {@code Set} containing all valid horizontal moves in the specified row
     */
    private Set<TwoPhaseMove<Position>> getLegalMovesInRow(int row) {
        Set<TwoPhaseMove<Position>> legalMoves = new java.util.HashSet<>();

        for (int startCol = 0; startCol < numberOfColumns; startCol++) {
            if (!board[row][startCol]) continue;

            for (int endCol = startCol; endCol < numberOfColumns; endCol++) {
                if (!board[row][endCol]) break;

                Position from = new Position(row, startCol);
                Position to = new Position(row, endCol);

                legalMoves.add(new TwoPhaseMove<>(from, to));

                if (startCol != endCol) {
                    legalMoves.add(new TwoPhaseMove<>(to, from));
                }
            }
        }
        return legalMoves;
    }

    /**
     * Computes and returns the set of all legal vertical moves for a given column.
     *
     * @param col the index of the column to check for legal moves
     * @return a {@code Set} containing all valid vertical moves in the specified column
     */
    private Set<TwoPhaseMove<Position>> getLegalMovesInColumn(int col) {
        Set<TwoPhaseMove<Position>> legalMoves = new java.util.HashSet<>();

        for (int startRow = 0; startRow < numberOfRows; startRow++) {
            if (!board[startRow][col]) continue;

            for (int endRow = startRow; endRow < numberOfRows; endRow++) {
                if (!board[endRow][col]) break;

                Position from = new Position(startRow, col);
                Position to = new Position(endRow, col);

                legalMoves.add(new TwoPhaseMove<>(from, to));

                if (startRow != endRow) {
                    legalMoves.add(new TwoPhaseMove<>(to, from));
                }
            }
        }
        return legalMoves;
    }

    /**
     * {@return the set of all moves that can be applied to the state}
     */
    @Override
    public Set<TwoPhaseMove<Position>> getLegalMoves() {
        Set<TwoPhaseMove<Position>> legalMoves = new java.util.HashSet<>();

        for (int row = 0; row < numberOfRows; row++) {
            legalMoves.addAll(getLegalMovesInRow(row));
        }

        for (int col = 0; col < numberOfColumns; col++) {
            legalMoves.addAll(getLegalMovesInColumn(col));
        }

        return legalMoves;
    }

    /**
     * {@return a deep copy of this state}
     */
    @Override
    public PebbleSweepState copy() {
        return new PebbleSweepState(this);
    }

    /**
     * Compares the specified object with this state.
     *
     * @param o   the reference object with which to compare.
     * @return {@code true} if the specified object is equal to this state,
     * otherwise {@code false}
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PebbleSweepState that = (PebbleSweepState) o;
        return player == that.player && Objects.deepEquals(board, that.board);
    }

    /**
     * {@return the hash code value for this state}
     */
    @Override
    public int hashCode() {
        return Objects.hash(player, Arrays.deepHashCode(board));
    }

    /**
     * Returns a string representation of the board state.
     * Pebbles are represented by 'O', and empty spaces by '·'.
     *
     * @return a formatted string representing the current state of the board
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("    0 1 2 3\n");
        sb.append("___________\n");
        for (int row = 0; row < numberOfRows; row++) {
            sb.append(row + " | ");
            for (int col = 0; col < numberOfColumns-1; col++) {
                sb.append(board[row][col] ? "O " : ". ");
            }
            sb.append(board[row][numberOfColumns-1] ? "O\n" : ".\n");
        }
        return sb.toString();
    }

    /**
     * Formats a move variable into an easier-to-read format.
     *
     * @param move the variable to be formatted
     * @return a formatted string of the move
     */
    public String moveToString(TwoPhaseMove<Position> move) {
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(move.from().row()).append(", ").append(move.from().column()).append(")");
        sb.append(" --> ");
        sb.append("(").append(move.to().row()).append(", ").append(move.to().column()).append(")");
        return sb.toString();
    }
}
