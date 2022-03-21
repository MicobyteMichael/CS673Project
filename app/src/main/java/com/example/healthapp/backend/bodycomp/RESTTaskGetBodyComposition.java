package com.example.healthapp.backend.bodycomp;

import com.example.healthapp.HealthApplication;
import com.example.healthapp.backend.RESTTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.net.ssl.HttpsURLConnection;

public class RESTTaskGetBodyComposition implements RESTTask<BodyComposition> {

    private final Date day;

    public static void enqueue(Consumer<BodyComposition> onSuccess, Consumer<String> onFailure) {
        enqueue(null, onSuccess, onFailure);
    }

    public static void enqueue(Date day, Consumer<BodyComposition> onSuccess, Consumer<String> onFailure) {
        if(day == null) {
            day = Date.from(Instant.now());
        }

        Consumer<BodyComposition> onSuccessProxy = val -> {
          if(val == null) onFailure.accept("API failure");
          else onSuccess.accept(val);
        };

        HealthApplication.getInstance().getAPIClient().submitTask(new RESTTaskGetBodyComposition(day), onSuccessProxy, () -> onFailure.accept("API failure"));
    }

    public RESTTaskGetBodyComposition(Date day) { this.day = day; }

    @Override public String getMethod() { return "POST"; }
    @Override public String getEndpoint() { return "bodycomp"; }
    @Override public String getMessage() { return "Getting body composition..."; }
    @Override public JSONObject getParameters() throws JSONException { return new JSONObject().accumulate("year", day.getYear() + 1900).accumulate("day", day.getDay()); }

    @Override
    public BodyComposition process(int responseCode, JSONObject json, Map<String, List<String>> headers) throws JSONException {
        if(responseCode == HttpsURLConnection.HTTP_OK) {
            return new BodyComposition(json.getInt("weight"), json.getInt("fatpercentage"), json.getInt("muscle"));
        }

        return null;
    }
}
