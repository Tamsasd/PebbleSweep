
# Pebble Sweep

> A 2D puzzle game implemented in Java using JavaFX, following the MVC architecture.
> *This project was originally developed for the Software Development course at the University of Debrecen (2025/26)*

## The Puzzle
The following game is played by two players.
Consider a game board consisting of a 4×4 grid of cells, each containing a stone.
Players move in turns.
In a move, a player must choose a (nonempty) row or column and remove 1, 2, 3, or 4 stones from it.
If two or more stones are removed, they must be adjacent.
The winner of the game is the player who does not make the last move.

## Features

* **Interactive GUI:** A clean and fully responsive JavaFX graphical interface with hover effects, path highlighting, and intuitive mouse controls (Left Click to select/move, Right Click to cancel).
* **Two Game Modes:** Playable via the graphical user interface or a lightweight terminal-based console application.
* **Persistent Leaderboard:** Game results (winner and timestamp) are automatically saved to and loaded from a `results.json` file.

## Tech Stack

* **Language:** Java (JDK 25)
* **Build Tool:** Apache Maven
* **UI Framework:** JavaFX
* **Testing:** JUnit 5, JaCoCo
* **Data Serialization:** Jackson (JSON)
* **Logging:** Tinylog

## How to Run

1. Clone this repository.
2. Open a terminal and navigate to the project's root directory.
3. Compile the project and build the executable JAR file:
   ``` bash
   mvn clean package
   ```
5.  To play via the Graphical Interface (GUI):
    ``` bash
    java -jar target/PebbleSweep-1.0-SNAPSHOT.jar
    ```

6.  To play via the Console Interface:
    ``` bash
    java -cp target/PebbleSweep-1.0-SNAPSHOT.jar pebblesweep.console.ConsoleMain
    ```

## How to play (GUI mode)

1. Start the game
2. Type your name(s) in the corresponding input boxes. By default, the names are *PLAYER_1* and *PLAYER_2*.
3. The in-turn player's name is highlighted. They have to click on two pebbles (or twice on a single pebble). The pebble(s) clicked on, and the pebbles between them will be selected as their move.
4. The pebbles are sweeped, and now it's the other player's turn. The steps are repeated from step 3, until there's no pebbles left.
5. The player who sweeps the last pebble, loses; the other player wins.

## Example Game (Console)

The following sequence demonstrates a complete, valid game played through the console interface. In the console, a move consists of selecting the starting coordinate `row col` and then the ending coordinate `row col`.

```
1. PLAYER_1: 0 0
2. PLAYER_1: 0 3
3. PLAYER_2: 1 0
4. PLAYER_2: 1 3
5. PLAYER_1: 2 0
6. PLAYER_1: 2 3
7. PLAYER_2: 3 0
8. PLAYER_2: 3 3
PLAYER_1 wins
```

_(Explanation: Players take turns sweeping entire rows by selecting the first and last pebbles in the rows. Player 2 sweeps the last remaining row, leaving the board empty, thus ending the game)._

## Acknowledgments

-   University of Debrecen, Faculty of Informatics - `homework-project-utils-2026` and `homework-project-jfxutils-2026` libraries.