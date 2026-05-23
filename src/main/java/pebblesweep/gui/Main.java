package pebblesweep.gui;

import javafx.application.Application;

/**
 * A wrapper entry class used to launch the JavaFX application.
 */
public class Main {

    /**
     * The standard Java main method that delegates the execution to the JavaFX application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Application.launch(PebbleSweepApplication.class, args);
    }
}