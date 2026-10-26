package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Reads values from src/test/resources/config.properties.
 * A value passed on the command line (mvn test -Dbrowser=firefox) wins over the file.
 */
public class ConfigReader {

    private static final Properties props = new Properties();

    static {
        try (InputStream in = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in == null) {
                throw new RuntimeException("config.properties not found in src/test/resources");
            }
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Could not load config.properties", e);
        }
    }

    public static String get(String key) {
        String fromCli = System.getProperty(key);
        if (fromCli != null && !fromCli.isBlank() && !fromCli.startsWith("${")) {
            return fromCli.trim();
        }
        String value = props.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Key '" + key + "' not found in config.properties");
        }
        return value.trim();
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }
}
