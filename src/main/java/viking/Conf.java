package viking;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Conf {
    public Conf() throws IOException {
        FileInputStream fis = new FileInputStream("vk-learn.properties");
        Properties properties = new Properties();
        properties.load(fis);

        HOST = properties.getProperty("host");
        RAW_TOPIC = properties.getProperty("raw_topic");
        PROCESSED_TOPIC= properties.getProperty("processed_topic");
    }

    public final String HOST;
    public final String RAW_TOPIC;
    public final String PROCESSED_TOPIC;
}
