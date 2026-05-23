package pebblesweep.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.tinylog.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages IO operations of the game results to and from a JSON file.
 */
public class ResultManager {
    /**
     * The path to the JSON file where results are stored.
     */
    private static final String FILE_PATH = "results.json";

    /**
     * The Jackson ObjectMapper used for JSON serialization and deserialization.
     */
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Loads the list of game results from the JSON file.
     *
     * @return a list of {@link GameResult} objects, or and empty list if the file does not exist.
     */
    public static List<GameResult> loadResults() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try {
            return mapper.readValue(file, new TypeReference<List<GameResult>>() {});
        } catch (IOException e) {
            Logger.error(e, "Failed to load results from JSON.");
            return new ArrayList<>();
        }
    }

    /**
     * Adds a new result to the JSON file.
     *
     * @param result the {@link GameResult} to be saved.
     */
    public static void saveResult(GameResult result) {
        List<GameResult> results = loadResults();
        results.add(result);
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), results);
            Logger.info("Game result saved successfully.");
        } catch (IOException e) {
            Logger.error(e, "Failed to save result to JSON.");
        }
    }
}
