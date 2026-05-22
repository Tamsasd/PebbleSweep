package pebblesweep.console;

import game.console.TwoPhaseMoveGame;
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
        if (!s.matches("\\d+\\s+\\d+")) {
            throw new IllegalArgumentException("Invalid format! Please provide two number divided by a space. (e.g. '0 1')");
        }

        Scanner scanner = new Scanner(s);
        return new Position(scanner.nextInt(), scanner.nextInt());
    }
}
