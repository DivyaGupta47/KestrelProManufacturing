package utils;

import java.io.InputStream;
import java.util.Properties;
/**
 * Utility class for loading and accessing key-value pairs from the `config.properties` file.
 * 
 * This class performs:
 * - Loading properties from a file named `config.properties` located in the `resources` directory.
 * - Providing a static `get(key)` method to retrieve property values by key.
 * 
 * Key Features:
 * - Automatically loads properties at class initialization.
 * - Throws runtime exceptions if the file is missing or cannot be loaded.
 * - Simplifies access to credentials and environment-specific configurations.
 * 
 * Usage Example:
 *   String customerName = CredentialsUtil.get("customer.name");
 * 
 * Prerequisites:
 * - `config.properties` must exist in the `src/test/resources` or `src/main/resources` folder.
 */
public class CredentialsUtil {
    private static final Properties props = new Properties();

    static {
        try (InputStream input = CredentialsUtil.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("config.properties not found in resources folder!");
            }
            props.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties: " + e.getMessage(), e);
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
    
    public static String getUniqueAssigneeEmail() {
        String template = get("assignee.email.template"); // Fetch from config
        String timestamp = String.valueOf(System.currentTimeMillis());
        return template.replace("{timestamp}", timestamp);
    }
}
