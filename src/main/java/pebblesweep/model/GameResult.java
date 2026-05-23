package pebblesweep.model;

/**
 * Represents the result of a finished game.
 */
public class GameResult {
    private String winner;
    private String date;

    /**
     * Default constructor required by Jackson.
     */
    public GameResult() {}

    /**
     * Creates a new GameResult
     *
     * @param winner the name of the winning player
     * @param date the date and time the game was finished
     */
    public GameResult(String winner, String date) {
        this.winner = winner;
        this.date = date;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
