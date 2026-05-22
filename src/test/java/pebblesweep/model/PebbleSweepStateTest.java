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
    void testIsLegalToMoveFrom_state1() {
        Position pos1 = new Position(0, 0);
        Position pos2 = new Position(3,3);
        Position pos3 = new Position(1,2);

        assertTrue(state1.isLegalToMoveFrom(pos1));
        assertTrue(state1.isLegalToMoveFrom(pos2));
        assertTrue(state1.isLegalToMoveFrom(pos3));
    }

    @Test
    void testIsLegalToMoveFrom_state2() {
        Position pos1 = new Position(0, 0);
        Position pos2 = new Position(3,3);
        Position pos3 = new Position(1,2);

        assertFalse(state2.isLegalToMoveFrom(pos1));
        assertFalse(state2.isLegalToMoveFrom(pos2));
        assertFalse(state2.isLegalToMoveFrom(pos3));
    }

    @Test
    void testIsLegalToMoveFrom_state3() {
        Position pos1 = new Position(0, 0);
        Position pos2 = new Position(3,3);
        Position pos3 = new Position(1,2);

        assertTrue(state3.isLegalToMoveFrom(pos1));
        assertFalse(state3.isLegalToMoveFrom(pos2));
        assertTrue(state3.isLegalToMoveFrom(pos3));
    }

    @Test
    void testGetNextPlayer() {
        assertEquals(State.Player.PLAYER_1, state1.getNextPlayer());
        assertEquals(State.Player.PLAYER_1, state2.getNextPlayer());
        assertEquals(State.Player.PLAYER_2, state3.getNextPlayer());
    }

    @Test
    void testIsGameOver() {
        assertFalse(state1.isGameOver());
        assertTrue(state2.isGameOver());
        assertFalse(state3.isGameOver());
    }
}
