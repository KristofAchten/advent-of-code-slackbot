package adventofcode.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Author: kachte4
 * Date: 14-11-2020.
 */
public class BatchTools {

    public static Properties getConfig(final String configFile) {
        try {
            final Properties config = new Properties();
            config.load(new FileInputStream(new File(configFile)));

            return config;
        } catch (IOException e) {
            throw new RuntimeException("Error while reading config", e);
        }
    }
}
