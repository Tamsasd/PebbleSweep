package pebblesweep.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.tinylog.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResultManager {
    private static final String FILE_PATH = "results.json";
    private static final ObjectMapper mapper = new ObjectMapper();

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
