package com.example.healthapp.backend.exercise;

import com.example.healthapp.HealthApplication;
import com.example.healthapp.backend.RESTTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.net.ssl.HttpsURLConnection;

public class RESTTaskGetSteps implements RESTTask<Integer> {

    private final LocalDate day;

    public static void enqueue(Consumer<Integer> onSuccess, Consumer<String> onFailure) {
        enqueue(null, onSuccess, onFailure);
    }

    public static void enqueue(LocalDate day, Consumer<Integer> onSuccess, Consumer<String> onFailure) {
        if(day == null) day = LocalDate.now();

        Consumer<Integer> onSuccessProxy = val -> {
          if(val == -1) onFailure.accept("API failure");
          else onSuccess.accept(val);
        };

        HealthApplication.getInstance().getAPIClient().submitTask(new RESTTaskGetSteps(day), onSuccessProxy, () -> onFailure.accept("API failure"));
    }

    public RESTTaskGetSteps(LocalDate day) { this.day = day; }

    @Override public String getMethod() { return "POST"; }
    @Override public String getEndpoint() { return "steps"; }
    @Override public String getMessage() { return "Getting steps..."; }
    @Override public JSONObject getParameters() throws JSONException { return new JSONObject().accumulate("year", day.getYear()).accumulate("day", day.getDayOfYear()); }

    @Override
    public Integer process(int responseCode, JSONObject json, Map<String, List<String>> headers) throws JSONException {
        if(responseCode == HttpsURLConnection.HTTP_OK) {
            return json.getInt("steps");
        }

        return -1;
    }
}
