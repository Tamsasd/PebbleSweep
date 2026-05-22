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
        if (!s.matches("\\d\\s\\d")) {
            Logger.warn("Invalid format! Please provide two numbers in the following format: 'row column' with numbers [1,4] (e.g. '1 4')");
            throw new IllegalArgumentException("Invalid format! Please provide two numbers in the following format: 'row column' with numbers [1,4] (e.g. '1 4')");
        }

        Scanner scanner = new Scanner(s);
        return new Position(scanner.nextInt()-1, scanner.nextInt()-1);
    }
}
