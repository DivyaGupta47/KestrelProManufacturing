package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PayloadUtil {

    public static String loadJsonAsString(String relativePath) {
        try {
            return new String(Files.readAllBytes(Paths.get("src/test/resources/" + relativePath)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON file: " + relativePath, e);
        }
    }
}
