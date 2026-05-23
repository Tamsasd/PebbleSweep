package pebblesweep.model;

public class GameResult {
    private String winner;
    private String date;

    public GameResult() {}

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
