package com.example.healthapp.backend.sleeptracking;

import com.example.healthapp.HealthApplication;
import com.example.healthapp.backend.RESTTask;
import com.example.healthapp.backend.foodanddrink.Meal;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.net.ssl.HttpsURLConnection;

public class RESTTaskStartSleepSession implements RESTTask<Boolean> {

    private final Date day;
    private final String name;
    private final Instant startTime;

    public static void enqueue(String name, Runnable onSuccess, Consumer<String> onFailure) {
        enqueue(null, name, null, onSuccess, onFailure);
    }

    public static void enqueue(Date day, String name, Instant startTime, Runnable onSuccess, Consumer<String> onFailure) {
        if(day == null) day = Date.from(Instant.now());
        if(startTime == null) startTime = Instant.now();

        Consumer<Boolean> onSuccessProxy = val -> {
          if(val) onSuccess.run();
          else onFailure.accept("API failure again");
        };

        HealthApplication.getInstance().getAPIClient().submitTask(new RESTTaskStartSleepSession(day, name, startTime), onSuccessProxy, () -> onFailure.accept("API failure"));
    }

    public RESTTaskStartSleepSession(Date day, String name, Instant startTime) { this.day = day; this.name = name; this.startTime = startTime; }

    @Override public String getMethod() { return "PUT"; }
    @Override public String getEndpoint() { return "sleeptracking"; }
    @Override public String getMessage() { return "Saving start time..."; }
    @Override public JSONObject getParameters() throws JSONException { return new JSONObject().accumulate("year", day.getYear() + 1900).accumulate("day", day.getDay()).accumulate("name", name).accumulate("start", startTime.getEpochSecond()); }

    @Override
    public Boolean process(int responseCode, JSONObject json, Map<String, List<String>> headers) throws JSONException {
        if(responseCode == HttpsURLConnection.HTTP_OK) {
            return json.getBoolean("added");
        }

        return false;
    }
}
