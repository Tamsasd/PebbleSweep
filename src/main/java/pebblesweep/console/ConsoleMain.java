package pebblesweep.console;

import game.State;
import game.console.TwoPhaseMoveGame;
import org.tinylog.Logger;
import pebblesweep.model.GameResult;
import pebblesweep.model.PebbleSweepState;
import pebblesweep.model.Position;
import pebblesweep.model.ResultManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * The entry point for the console-based version of the Pebble Sweep game.
 */
public class ConsoleMain {

    /**
     * The standard main method that delegates the execution of the console game.
     * When the game is over it saves the results to a JSON file.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        PebbleSweepState state = new PebbleSweepState();

        TwoPhaseMoveGame<Position, PebbleSweepState> game = new TwoPhaseMoveGame<>(state, ConsoleMain::parseMove);

        game.start();

        if (state.isGameOver()) {
            String winner = state.isWinner(State.Player.PLAYER_1) ? "PLAYER_1" : "PLAYER_2";
            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            ResultManager.saveResult(new GameResult(winner, date));
        }
    }

    /**
     * Parses a string input into a {@link Position} object on the game board.
     * The input is expected to contain exactly two integers between 0 and 3, separated by whitespace.
     *
     * @param s the raw string input from the user
     * @return a valid {@link Position} object corresponding to the parsed coordinates
     * @throws IllegalArgumentException if the input format is invalid or contains numbers out of bounds
     */
    public static Position parseMove(String s) {
        s = s.trim();
        if (!s.matches("[0-3]\\s+[0-3]")) {
            Logger.warn("Invalid format! Please provide two numbers in the following format: 'row column' with numbers [0,3] (e.g. '2 3')");
            throw new IllegalArgumentException("Invalid format! Please provide two numbers in the following format: 'row column' with numbers [0,3] (e.g. '2 3')");
        }

        Scanner scanner = new Scanner(s);
        return new Position(scanner.nextInt(), scanner.nextInt());
    }
}