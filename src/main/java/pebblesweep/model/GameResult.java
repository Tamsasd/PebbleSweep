package pebblesweep.model;

import java.time.LocalDateTime;

/**
 * Represents the result of a finished game.
 */
public class GameResult {
    /**
     * The name of the winning player.
     */
    private String winner;

    /**
     * The {@code date} and time when the game was finished.
     */
    private LocalDateTime date;

    /**
     * Default constructor required by Jackson.
     */
    public GameResult() {}

    /**
     * Creates a new {@link GameResult}.
     *
     * @param winner the name of the winning player
     * @param date the {@code date} and time the game was finished
     */
    public GameResult(String winner, LocalDateTime date) {
        this.winner = winner;
        this.date = date;
    }

    /**
     * Retrieves the name of the winning player.
     *
     * @return the {@code winner}'s name
     */
    public String getWinner() {
        return winner;
    }

    /**
     * Sets the name of the winning player.
     *
     * @param winner the {@code winner}'s name to set
     */
    public void setWinner(String winner) {
        this.winner = winner;
    }

    /**
     * Retrieves the date and time when the game was completed.
     *
     * @return the date string
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Sets the date and time for the game result.
     *
     * @param date the date string to set
     */
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
