package viking.mqtt.publisher;

import net.sf.xenqtt.client.MqttClient;
import net.sf.xenqtt.client.PublishMessage;
import net.sf.xenqtt.client.SyncMqttClient;
import net.sf.xenqtt.message.ConnectReturnCode;
import net.sf.xenqtt.message.QoS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viking.Conf;
import viking.ProcessedPojo;
import viking.mqtt.DefaultListener;

import java.util.concurrent.*;

public class ProcessedPublisher implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(ProcessedPublisher.class);
    private BlockingQueue<ProcessedPojo> queue;
    private MqttClient client;
    private Conf conf;

    private volatile boolean stop = false;

    public ProcessedPublisher(Conf conf, BlockingQueue<ProcessedPojo> queue){
        this.queue = queue;
        this.conf = conf;
    }

    public void run() {
        init();

        ProcessedPojo processedPojo = null;
        log.info("publisher processing loop starting");
        for(;!stop;) {
            try {
                processedPojo = queue.poll(50, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                return;
            }

            if(processedPojo != null) {
                client.publish(new PublishMessage(conf.PROCESSED_TOPIC,
                    QoS.AT_MOST_ONCE,
                    processedPojo.getResultObject().toString()));
            }
        }
        log.info("publisher processing ended");
    }

    public void init() {
        client = new SyncMqttClient(conf.HOST, new DefaultListener(), 5);

        ConnectReturnCode returnCode = client.connect("viking-learn-pub", false);

        if(returnCode != ConnectReturnCode.ACCEPTED) {
            log.error(":(");
            return;
        }
        log.info("Connection to MQTT seems to work");
    }

    public void stop() {
        log.info("we are done, disconnecting");
        stop = true;
        client.disconnect();
    }

}
