package viking;

import viking.mqtt.consumer.RawConsumer;
import viking.mqtt.publisher.ProcessedPublisher;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {

    public static void main(String args[]) {
        Conf conf = null;
        try {
             conf = new Conf();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        BlockingQueue<ProcessedPojo> processedPojos = new LinkedBlockingQueue<ProcessedPojo>(10000);

        ProcessedPublisher publisher = new ProcessedPublisher(conf, processedPojos);
        RawConsumer consumer = new RawConsumer(conf, processedPojos);
        Thread publisherThread = new Thread(publisher, "publisher-thread");
        publisherThread.start();
        consumer.start();
    }
}
