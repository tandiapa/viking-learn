package viking;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class ProcessedPojo {
    private int sentiment = -1;

    private JsonObject originObject;

    public ProcessedPojo() {}

    public ProcessedPojo(JsonObject jsonObject) {
        this.originObject = jsonObject;
    }

    public JsonObject getResultObject(){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("tweet", originObject.get("tweet"));
        builder.add("id", originObject.get("id"));
        builder.add("sentiment",sentiment);
        return builder.build();
    }

    public int getSentiment() {
        return sentiment;
    }

    public void setSentiment(int sentiment) {
        this.sentiment = sentiment;
    }

    public JsonObject getOriginObject() {
        return originObject;
    }

    public void setOriginObject(JsonObject originObject) {
        this.originObject = originObject;
    }
}
