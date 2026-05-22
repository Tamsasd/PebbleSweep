package pebblesweep.model;

import game.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PebbleSweepStateTest {

    private PebbleSweepState state1;
    private PebbleSweepState state2;
    private PebbleSweepState state3;

    @BeforeEach
    void setUp() {
        state1 = new PebbleSweepState(); // initial state

        boolean[][] state2Board = {
                {false, false, false, false},
                {false, false, false, false},
                {false, false, false, false},
                {false, false, false, false}
        };
        state2 = new PebbleSweepState(state2Board, State.Player.PLAYER_1); // finish state

        boolean[][] state3Board = {
                {true, false, false, false},
                {false, false, true, true},
                {false, false, false, false},
                {true, false, true, false}
        };
        state3 = new PebbleSweepState(state3Board, State.Player.PLAYER_2); // intermediate state
    }

    @Test
    void isGameOver() {
        assertFalse(state1.isGameOver());
        assertTrue(state2.isGameOver());
        assertFalse(state3.isGameOver());
    }
}
