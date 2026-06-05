package pebblesweep.model;

import common.TwoPhaseMoveState;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * A bot implementation that selects a completely random valid move.
 */
public class RandomBotPlayer implements BotPlayer {
    private final Random random = new Random();

    @Override
    public TwoPhaseMoveState.TwoPhaseMove<Position> chooseMove(
            Set<TwoPhaseMoveState.TwoPhaseMove<Position>> legalMoves,
            PebbleSweepState state) {

        if (legalMoves == null || legalMoves.isEmpty()) {
            throw new IllegalArgumentException("No legal moves available for the bot.");
        }

        List<TwoPhaseMoveState.TwoPhaseMove<Position>> movesList = new ArrayList<>(legalMoves);
        return movesList.get(random.nextInt(movesList.size()));
    }
}