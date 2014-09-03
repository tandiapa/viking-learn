package viking.mqtt.consumer;

import net.sf.xenqtt.client.AsyncClientListener;
import net.sf.xenqtt.client.MqttClient;
import net.sf.xenqtt.client.PublishMessage;
import net.sf.xenqtt.client.Subscription;
import net.sf.xenqtt.message.ConnectReturnCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viking.ProcessedPojo;
import viking.SentimentAnalyzer;
import viking.mqtt.DefaultListener;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;


public class ConsumingListener extends DefaultListener {

    BlockingQueue outQueue;

    public ConsumingListener(BlockingQueue queue) {
        this.outQueue = queue;
    }

    private static Logger log = LoggerFactory.getLogger(ConsumingListener.class);
    SentimentAnalyzer sentimentAnalyzer = new SentimentAnalyzer();
    @Override
    public void publishReceived(MqttClient client, PublishMessage message) {
        JsonReader jsonReader = Json.createReader(new StringReader(message.getPayloadString()));
        JsonObject jsonObject = jsonReader.readObject();

        String text=((JsonObject)jsonObject.get("tweet")).get("text").toString();
        text.replaceAll("[#@]","");
        log.debug(text);
        ProcessedPojo processedPojo=new ProcessedPojo(jsonObject);
        int sentiment = sentimentAnalyzer.predict(text);
        processedPojo.setSentiment(sentiment);
        log.debug(""+sentiment);

        boolean offered = false;
        try {
            offered = this.outQueue.offer(processedPojo, 50, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            return;
        }

        if(offered) {
            message.ack();
        }
    }

}
