package com.example.healthapp.backend.sleeptracking;

import com.example.healthapp.HealthApplication;
import com.example.healthapp.backend.RESTTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.net.ssl.HttpsURLConnection;

public class RESTTaskGetSleep implements RESTTask<SleepSession[]> {

    private final LocalDate day;

    public static void enqueue(Consumer<SleepSession[]> onSuccess, Consumer<String> onFailure) {
        enqueue(null, onSuccess, onFailure);
    }

    public static void enqueue(LocalDate day, Consumer<SleepSession[]> onSuccess, Consumer<String> onFailure) {
        if(day == null) day = LocalDate.now();

        Consumer<SleepSession[]> onSuccessProxy = val -> {
          if(val == null) onFailure.accept("API failure");
          else onSuccess.accept(val);
        };

        HealthApplication.getInstance().getAPIClient().submitTask(new RESTTaskGetSleep(day), onSuccessProxy, () -> onFailure.accept("API failure"));
    }

    public RESTTaskGetSleep(LocalDate day) { this.day = day; }

    @Override public String getMethod() { return "POST"; }
    @Override public String getEndpoint() { return "sleeptracking"; }
    @Override public String getMessage() { return "Getting sleep times..."; }
    @Override public JSONObject getParameters() throws JSONException { return new JSONObject().accumulate("year", day.getYear()).accumulate("day", day.getDayOfYear()); }

    @Override
    public SleepSession[] process(int responseCode, JSONObject json, Map<String, List<String>> headers) throws JSONException {
        if(responseCode == HttpsURLConnection.HTTP_OK) {
            JSONArray sleepsRaw = json.getJSONArray("sleeps");
            SleepSession[] sleeps = new SleepSession[sleepsRaw.length()];

            for(int i = 0; i < sleeps.length; i++) {
                JSONObject sleepRaw = sleepsRaw.getJSONObject(i);
                long start = -1, end = -1;

                if(sleepRaw.has("start") && sleepRaw.get("start") != JSONObject.NULL) {
                    start = sleepRaw.getLong("start");
                }

                if(sleepRaw.has("end") && sleepRaw.get("end") != JSONObject.NULL) {
                    end = sleepRaw.getLong("end");
                }

                sleeps[i] = new SleepSession(sleepRaw.getString("name"), start, end);
            }

            return sleeps;
        }

        return null;
    }
}
