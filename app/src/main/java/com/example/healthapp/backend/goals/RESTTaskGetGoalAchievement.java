package com.example.healthapp.backend.goals;

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

public class RESTTaskGetGoalAchievement implements RESTTask<Boolean> {

    private final Date day;
    private final String name;

    public static void enqueue(String name, Consumer<Boolean> onSuccess, Consumer<String> onFailure) {
        enqueue(null, name, onSuccess, onFailure);
    }

    public static void enqueue(Date day, String name, Consumer<Boolean> onSuccess, Consumer<String> onFailure) {
        if(day == null) {
            day = Date.from(Instant.now());
        }

        Consumer<Boolean> onSuccessProxy = val -> {
            if(val == null) onFailure.accept("API failure again");
            else onSuccess.accept(val);
        };

        HealthApplication.getInstance().getAPIClient().submitTask(new RESTTaskGetGoalAchievement(day, name), onSuccessProxy, () -> onFailure.accept("API failure"));
    }

    public RESTTaskGetGoalAchievement(Date day, String name) { this.day = day; this.name = name; }

    @Override public String getMethod() { return "POST"; }
    @Override public String getEndpoint() { return "goalsuccesses"; }
    @Override public String getMessage() { return "Getting goal successes..."; }
    @Override public JSONObject getParameters() throws JSONException { return new JSONObject().accumulate("year", day.getYear() + 1900).accumulate("day", day.getDay()).accumulate("name", name); }

    @Override
    public Boolean process(int responseCode, JSONObject json, Map<String, List<String>> headers) throws JSONException {
        if(responseCode == HttpsURLConnection.HTTP_OK) {
            return json.getBoolean("achieved");
        }

        return null;
    }
}
