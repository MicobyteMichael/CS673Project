package com.example.healthapp.backend.sleeptracking;

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

public class RESTTaskEndSleepSession implements RESTTask<Boolean> {

    private final Date day;
    private final String name;
    private final Instant endTime;

    public static void enqueue(Date day, String name, Instant endTime, Runnable onSuccess, Consumer<String> onFailure) {
        if(day == null) day = Date.from(Instant.now());
        if(endTime == null) endTime = Instant.now();

        Consumer<Boolean> onSuccessProxy = val -> {
          if(val) onSuccess.run();
          else onFailure.accept("API failure again");
        };

        HealthApplication.getInstance().getAPIClient().submitTask(new RESTTaskEndSleepSession(day, name, endTime), onSuccessProxy, () -> onFailure.accept("API failure"));
    }

    public RESTTaskEndSleepSession(Date day, String name, Instant endTime) { this.day = day; this.name = name; this.endTime = endTime; }

    @Override public String getMethod() { return "PATCH"; }
    @Override public String getEndpoint() { return "sleeptracking"; }
    @Override public String getMessage() { return "Saving end time..."; }
    @Override public JSONObject getParameters() throws JSONException { return new JSONObject().accumulate("year", day.getYear() + 1900).accumulate("day", day.getDay()).accumulate("name", name).accumulate("end", endTime.getEpochSecond()); }

    @Override
    public Boolean process(int responseCode, JSONObject json, Map<String, List<String>> headers) throws JSONException {
        if(responseCode == HttpsURLConnection.HTTP_OK) {
            return json.getBoolean("updated");
        }

        return false;
    }
}
