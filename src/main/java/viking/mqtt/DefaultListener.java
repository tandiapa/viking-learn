package viking.mqtt;

import net.sf.xenqtt.client.AsyncClientListener;
import net.sf.xenqtt.client.MqttClient;
import net.sf.xenqtt.client.PublishMessage;
import net.sf.xenqtt.client.Subscription;
import net.sf.xenqtt.message.ConnectReturnCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class DefaultListener implements AsyncClientListener {
    private static Logger log = LoggerFactory.getLogger(DefaultListener.class);

    @Override
    public void publishReceived(MqttClient client, PublishMessage message) {
        message.ack();
    }

    @Override
    public void disconnected(MqttClient client, Throwable cause, boolean reconnecting) {
        if (cause != null) {
            log.error("Disconnected from the broker due to an exception.", cause);
        } else {
            log.info("Disconnecting from the broker.");
        }
        if (reconnecting) {
            log.info("Attempting to reconnect to the broker.");
        }
    }

    @Override
    public void connected(MqttClient client, ConnectReturnCode returnCode) {
        if (returnCode == null || returnCode != ConnectReturnCode.ACCEPTED) {
            log.error("Unable to connect to the MQTT broker. Reason: " + returnCode);
            throw new RuntimeException();
        }
    }

    @Override
    public void published(MqttClient client, PublishMessage message) {
    }

    @Override
    public void subscribed(MqttClient client, Subscription[] requestedSubscriptions, Subscription[] grantedSubscriptions, boolean requestsGranted) {
        if (!requestsGranted) {
            log.error("Unable to subscribe to the following subscriptions: " + Arrays.toString(requestedSubscriptions));
        }
        log.debug("Granted subscriptions: " + Arrays.toString(grantedSubscriptions));
    }

    @Override
    public void unsubscribed(MqttClient client, String[] topics) {
        client.unsubscribe(topics);
        log.debug("Unsubscribed from the following topics: " + Arrays.toString(topics));
    }
}
