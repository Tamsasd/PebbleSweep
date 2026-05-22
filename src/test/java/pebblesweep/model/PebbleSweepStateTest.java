package pebblesweep.model;

import common.TwoPhaseMoveState;
import game.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PebbleSweepStateTest {

    private PebbleSweepState state1;
    private PebbleSweepState state2;
    private PebbleSweepState state3;
    private PebbleSweepState state4;

    @BeforeEach
    void setUp() {
        state1 = new PebbleSweepState(); // initial state

        boolean[][] state2Board = {
                {false, false, false, false},
                {false, false, false, false},
                {false, false, false, false},
                {false, false, false, false}
        };
        state2 = new PebbleSweepState(state2Board, State.Player.PLAYER_1); // finish state (player 1 wins)

        boolean[][] state3Board = {
                {true, false, false, false},
                {false, false, true, true},
                {false, false, false, false},
                {true, false, true, false}
        };
        state3 = new PebbleSweepState(state3Board, State.Player.PLAYER_2); // intermediate state

        boolean[][] state4Board = new boolean[4][4]; // finish state (player 2 wins)
        state4 = new PebbleSweepState(state4Board, State.Player.PLAYER_2);
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

    @Test
    void testGetStatus() {
        assertEquals(State.Status.IN_PROGRESS, state1.getStatus());
        assertEquals(State.Status.PLAYER_1_WINS, state2.getStatus());
        assertNotEquals(State.Status.PLAYER_2_WINS, state2.getStatus());
        assertEquals(State.Status.IN_PROGRESS, state3.getStatus());
        assertEquals(State.Status.PLAYER_2_WINS, state4.getStatus());
    }

    @Test
    void testIsWinner() {
        assertFalse(state1.isWinner(State.Player.PLAYER_1));
        assertFalse(state1.isWinner(State.Player.PLAYER_2));

        assertTrue(state2.isWinner(State.Player.PLAYER_1));
        assertFalse(state2.isWinner(State.Player.PLAYER_2));

        assertFalse(state3.isWinner(State.Player.PLAYER_1));
        assertFalse(state3.isWinner(State.Player.PLAYER_2));

        assertFalse(state4.isWinner(State.Player.PLAYER_1));
        assertTrue(state4.isWinner(State.Player.PLAYER_2));
    }

    @Test
    void testIsLegalMove() {
        Position pos1 = new Position(0,0);
        Position pos2 = new Position(3,0);
        Position pos3 = new Position(1,3);
        Position pos4 = new Position(3,2);

        TwoPhaseMoveState.TwoPhaseMove<Position> move1 = new TwoPhaseMoveState.TwoPhaseMove<>(pos1, pos2);
        TwoPhaseMoveState.TwoPhaseMove<Position> move2 = new TwoPhaseMoveState.TwoPhaseMove<>(pos1, pos3);
        TwoPhaseMoveState.TwoPhaseMove<Position> move3 = new TwoPhaseMoveState.TwoPhaseMove<>(pos2, pos4);
        TwoPhaseMoveState.TwoPhaseMove<Position> move4 = new TwoPhaseMoveState.TwoPhaseMove<>(pos1, pos1);

        assertTrue(state1.isLegalMove(move1));
        assertFalse(state1.isLegalMove(move2));
        assertTrue(state1.isLegalMove(move3));

        assertFalse(state2.isLegalMove(move1));
        assertFalse(state2.isLegalMove(move4));

        assertFalse(state3.isLegalMove(move3));
        assertTrue(state3.isLegalMove(move4));
    }

    @Test
    void testMakeMove_column() {
        Position pos1 = new Position(0,0);
        Position pos2 = new Position(3,0);
        Position pos3 = new Position(1, 0);

        TwoPhaseMoveState.TwoPhaseMove<Position> move = new TwoPhaseMoveState.TwoPhaseMove<>(pos1, pos2);

        state1.makeMove(move);

        assertFalse(state1.isLegalToMoveFrom(pos1));
        assertFalse(state1.isLegalToMoveFrom(pos2));
        assertFalse(state1.isLegalToMoveFrom(pos3));
        assertEquals(State.Player.PLAYER_2, state1.getNextPlayer());
    }

    @Test
    void testMakeMove_row() {
        Position pos1 = new Position(0,0);
        Position pos2 = new Position(0,3);
        Position pos3 = new Position(0, 1);

        TwoPhaseMoveState.TwoPhaseMove<Position> move = new TwoPhaseMoveState.TwoPhaseMove<>(pos1, pos2);

        state1.makeMove(move);

        assertFalse(state1.isLegalToMoveFrom(pos1));
        assertFalse(state1.isLegalToMoveFrom(pos2));
        assertFalse(state1.isLegalToMoveFrom(pos3));
        assertEquals(State.Player.PLAYER_2, state1.getNextPlayer());
    }

    @Test
    void testMakeMoveThrows() {
        Position pos1 = new Position(0, 0);
        Position pos2 = new Position(1, 1);
        TwoPhaseMoveState.TwoPhaseMove<Position> illegalMove = new TwoPhaseMoveState.TwoPhaseMove<>(pos1, pos2);
        TwoPhaseMoveState.TwoPhaseMove<Position> legalMove = new TwoPhaseMoveState.TwoPhaseMove<>(pos1, pos1);

        assertThrows(IllegalArgumentException.class, () -> state1.makeMove(illegalMove));
        assertDoesNotThrow(() -> state1.makeMove(legalMove));
    }

    @Test
    void testGetLegalMoves() {
        assertEquals(112, (long) state1.getLegalMoves().size());
        assertEquals(Set.of(), state2.getLegalMoves());
        assertEquals(7, (long) state3.getLegalMoves().size());
    }

    @Test
    void copy() {
        var copy = state1.copy();
        assertEquals(state1, copy);
        assertNotSame(state1, copy);
    }

    @Test
    void testEquals() {
        assertEquals(state1, state1);
        assertEquals(state1, new PebbleSweepState());
        assertFalse(state1.equals("asd"));
        assertFalse(state1.equals(null));
        assertNotEquals(state1, state2);
        assertNotEquals(state1, state3);
        PebbleSweepState sameBoardDifferentPlayer = new PebbleSweepState(
                new boolean[][]{
                        {true, true, true, true},
                        {true, true, true, true},
                        {true, true, true, true},
                        {true, true, true, true}
                },
                State.Player.PLAYER_2
        );
        assertNotEquals(state1, sameBoardDifferentPlayer);
    }

    @Test
    void testHashCode() {
        assertEquals(state1.hashCode(), new PebbleSweepState().hashCode());
        assertEquals(state1.hashCode(), state1.copy().hashCode());
    }

    @Test
    void testToString() {
        var str1 = """
                O O O O
                O O O O
                O O O O
                O O O O
                """;
        var str2 = """
                · · · ·
                · · · ·
                · · · ·
                · · · ·
                """;
        var str3 = """
                O · · ·
                · · O O
                · · · ·
                O · O ·
                """;

        assertEquals(str1, state1.toString());
        assertEquals(str2, state2.toString());
        assertEquals(str3, state3.toString());
        assertNotEquals(str3, state1.toString());
        assertNotEquals("asd", state1.toString());
        assertNotEquals(null, state1.toString());
    }

    @Test
    void testDistanceLimitWithLargeBoard() {
        boolean[][] largeBoard = {
                {true, true, true, true, true},
                {true, true, true, true, true},
                {true, true, true, true, true},
                {true, true, true, true, true},
                {true, true, true, true, true},
        };

        PebbleSweepState largeState = new PebbleSweepState(largeBoard, State.Player.PLAYER_1);

        Position pos1 = new Position(0, 0);
        Position pos2 = new Position(4, 0); // this is 5 pebbles
        TwoPhaseMoveState.TwoPhaseMove<Position> tooLongMove = new TwoPhaseMoveState.TwoPhaseMove<>(pos1, pos2);

        assertFalse(largeState.isLegalMove(tooLongMove)); // by the rules, we can only sweep a maximum of 4 pebbles
    }
}
