package viking.mqtt.consumer;

import net.sf.xenqtt.client.*;
import net.sf.xenqtt.message.QoS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viking.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class RawConsumer {
    private final BlockingQueue queue;
    private AsyncMqttClient client;
    private Conf conf;

    private static final Logger log = LoggerFactory.getLogger(RawConsumer.class);

    public RawConsumer(Conf conf, BlockingQueue queue) {
        this.queue = queue;
        this.conf = conf;
    }

    public void start() {
        client = new AsyncMqttClient(conf.HOST, new ConsumingListener(this.queue), 5);
        try {
            log.info("Try to connect to mqtt");
            client.connect("viking-learn-sub", true);
            log.info("Connected to mqtt");

            List<Subscription> subscriptions = new ArrayList<Subscription>();

            subscriptions.add(new Subscription(conf.RAW_TOPIC, QoS.AT_MOST_ONCE));
            client.subscribe(subscriptions);

        } catch (Exception ex) {
            log.error("An unexpected exception has occurred.", ex);
        }
    }

    public void stop() {
        client.close();
    }
}
