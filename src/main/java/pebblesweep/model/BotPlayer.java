package pebblesweep.model;

import common.TwoPhaseMoveState;
import java.util.Set;

/**
 * Defines the contract for an automated opponent strategy.
 */
public interface BotPlayer {
    /**
     * Chooses a move from the available legal moves.
     *
     * @param legalMoves the set of available legal moves
     * @param state the current state of the game
     * @return the selected move
     */
    TwoPhaseMoveState.TwoPhaseMove<Position> chooseMove(
            Set<TwoPhaseMoveState.TwoPhaseMove<Position>> legalMoves,
            PebbleSweepState state
    );
}