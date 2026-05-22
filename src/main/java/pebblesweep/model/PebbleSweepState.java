package pebblesweep.model;

import game.TwoPhaseMoveState;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.IntStream;

import static java.lang.Math.abs;

public class PebbleSweepState implements TwoPhaseMoveState<Position, PebbleSweepState> {

    private Player player;

    private boolean[][] BOARD = {
            {true, true, true, true},
            {true, true, true, true},
            {true, true, true, true},
            {true, true, true, true}
    };

    private int numberOfRows = BOARD.length;
    private int numberOfColumns = BOARD[0].length;

    public PebbleSweepState() {
        this.player = Player.PLAYER_1;
    }

    public PebbleSweepState(PebbleSweepState other) {
        this.player = other.player;
        this.numberOfColumns = other.numberOfColumns;
        this.numberOfRows = other.numberOfRows;

        this.BOARD = new boolean[other.numberOfRows][other.numberOfColumns];

        for (int i = 0; i < other.numberOfRows; i++) {
            this.BOARD[i] = Arrays.copyOf(other.BOARD[i], other.BOARD[i].length);
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
        return BOARD[from.row()][from.column()];
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
                if (BOARD[row][col]) return false;
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
                && !player.equals(this.player); // the player that picks the last pebble loses
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
    private boolean isValidDistance(TwoPhaseMove<Position> move) {
        int distance;
        if (isInSameColumn(move)) {
            distance = abs(move.from().row() - move.to().row());
        }
        else {
            distance = abs(move.from().column() - move.to().column());
        }
        return distance <= 3;
    }

    /**
     * {@return whether the row or column of the move has any spaces in between the endpoints}
     * This method should be called if and only if {@link #isInSameColumn(TwoPhaseMove)} or
     * {@link #isInSameRow(TwoPhaseMove)} returns {@code true}.
     *
     * @param move the move specified by a starting and ending position
     */
    private boolean hasGap(TwoPhaseMove<Position> move) {
        if (isInSameColumn(move)) {
            short col = move.from().column();
            int startRow = Math.min(move.from().row(), move.to().row());
            int endRow = Math.max(move.from().row(), move.to().row());
            for (int row = startRow; row <= endRow; row++) {
                if (!BOARD[row][col]) return true;
            }
            return false;
        }
        else {
            short row = move.from().row();
            int startCol = Math.min(move.from().column(), move.to().column());
            int endCol = Math.max(move.from().column(), move.to().column());
            for (int col = startCol; col <= endCol; col++) {
                if (!BOARD[row][col]) return true;
            }
            return false;
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
     */
    @Override
    public void makeMove(TwoPhaseMove<Position> move) {
        if (isInSameColumn(move)) {
            short col = move.from().column();
            int startRow = Math.min(move.from().row(), move.to().row());
            int endRow = Math.max(move.from().row(), move.to().row());
            for (int row = startRow; row <= endRow; row++) {
                BOARD[row][col] = false;
            }
        }
        else {
            short row = move.from().row();
            int startCol = Math.min(move.from().column(), move.to().column());
            int endCol = Math.max(move.from().column(), move.to().column());
            for (int col = startCol; col <= endCol; col++) {
                BOARD[row][col] = false;
            }
        }

        this.player = this.player.opponent();
    }

    /**
     * {@return the set of all moves that can be applied to the state}
     */
    @Override
    public Set<TwoPhaseMove<Position>> getLegalMoves() {
        return Set.of();
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
        return player == that.player && Objects.deepEquals(BOARD, that.BOARD);
    }

    /**
     * {@return the hash code value for this state}
     */
    @Override
    public int hashCode() {
        return Objects.hash(player, Arrays.deepHashCode(BOARD));
    }
}
