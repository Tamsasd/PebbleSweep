package pebblesweep.console;

import org.junit.jupiter.api.Test;
import pebblesweep.model.Position;

import static org.junit.jupiter.api.Assertions.*;

public class ConsoleMainTest {
    @Test
    void testParseMoveValid() {
        assertEquals(new Position(0,0), ConsoleMain.parseMove("0 0"));
        assertEquals(new Position(1,2), ConsoleMain.parseMove("1 2"));
        assertEquals(new Position(3,3), ConsoleMain.parseMove("3 3"));
    }

    @Test
    void testParseMoveExtraSpaces() {
        assertEquals(new Position(1,2), ConsoleMain.parseMove("    1 2      "));
        assertEquals(new Position(1,2), ConsoleMain.parseMove("1         2"));
    }

    @Test
    void testParseMoveThrows() {
        assertThrows(IllegalArgumentException.class, () -> ConsoleMain.parseMove("a b"));
        assertThrows(IllegalArgumentException.class, () -> ConsoleMain.parseMove("1"));
        assertThrows(IllegalArgumentException.class, () -> ConsoleMain.parseMove("1 2 3"));
        assertThrows(IllegalArgumentException.class, () -> ConsoleMain.parseMove("1,2"));
        assertThrows(IllegalArgumentException.class, () -> ConsoleMain.parseMove(""));
        assertThrows(NullPointerException.class, () -> ConsoleMain.parseMove(null));
        assertThrows(IllegalArgumentException.class, () -> ConsoleMain.parseMove("10 2"));
        assertThrows(IllegalArgumentException.class, () -> ConsoleMain.parseMove("6 7"));
    }
}
