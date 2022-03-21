package com.example.healthapp.backend.exercise;

import com.example.healthapp.HealthApplication;
import com.example.healthapp.backend.RESTTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.net.ssl.HttpsURLConnection;

public class RESTTaskSetSteps implements RESTTask<Boolean> {

    private final Date day;
    private final int numSteps;

    public static void enqueue(int numSteps, Runnable onSuccess, Consumer<String> onFailure) {
        enqueue(null, numSteps, onSuccess, onFailure);
    }

    public static void enqueue(Date day, int numSteps, Runnable onSuccess, Consumer<String> onFailure) {
        if(day == null) {
            day = Date.from(Instant.now());
        }

        Consumer<Boolean> onSuccessProxy = val -> {
          if(val) onSuccess.run();
          else onFailure.accept("API failure again");
        };

        HealthApplication.getInstance().getAPIClient().submitTask(new RESTTaskSetSteps(day, numSteps), onSuccessProxy, () -> onFailure.accept("API failure"));
    }

    public RESTTaskSetSteps(Date day, int numSteps) { this.day = day; this.numSteps = numSteps; }

    @Override public String getMethod() { return "PUT"; }
    @Override public String getEndpoint() { return "steps"; }
    @Override public String getMessage() { return "Saving steps taken..."; }
    @Override public JSONObject getParameters() throws JSONException { return new JSONObject().accumulate("year", day.getYear() + 1900).accumulate("day", day.getDay()).accumulate("steps", numSteps); }

    @Override
    public Boolean process(int responseCode, JSONObject json, Map<String, List<String>> headers) throws JSONException {
        if(responseCode == HttpsURLConnection.HTTP_OK) {
            return json.getBoolean("updated");
        }

        return false;
    }
}
