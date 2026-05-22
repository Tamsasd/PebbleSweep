package pebblesweep.console;

import game.console.TwoPhaseMoveGame;
import org.tinylog.Logger;
import pebblesweep.model.PebbleSweepState;
import pebblesweep.model.Position;

import java.util.Scanner;

public class ConsoleMain {

    public static void main(String[] args) {
        PebbleSweepState state = new PebbleSweepState();

        TwoPhaseMoveGame<Position, PebbleSweepState> game = new TwoPhaseMoveGame<>(state, ConsoleMain::parseMove);

        game.start();
    }

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
