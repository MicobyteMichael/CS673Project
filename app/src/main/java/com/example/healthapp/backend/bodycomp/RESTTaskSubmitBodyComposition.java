package com.example.healthapp.backend.bodycomp;

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

public class RESTTaskSubmitBodyComposition implements RESTTask<Boolean> {

    private final Date day;
    private final BodyComposition comp;

    public static void enqueue(Date day, BodyComposition comp, Runnable onSuccess, Consumer<String> onFailure) {
        if(day == null) {
            day = Date.from(Instant.now());
        }

        Consumer<Boolean> onSuccessProxy = val -> {
          if(val) onSuccess.run();
          else onFailure.accept("API failure again");
        };

        HealthApplication.getInstance().getAPIClient().submitTask(new RESTTaskSubmitBodyComposition(day, comp), onSuccessProxy, () -> onFailure.accept("API failure"));
    }

    public RESTTaskSubmitBodyComposition(Date day, BodyComposition comp) { this.day = day; this.comp = comp; }

    @Override public String getMethod() { return "PUT"; }
    @Override public String getEndpoint() { return "bodycomp"; }
    @Override public String getMessage() { return "Saving body composition..."; }

    @Override public JSONObject getParameters() throws JSONException {
        return new JSONObject()
            .accumulate("year", day.getYear() + 1900)
            .accumulate("day", day.getDay())
            .accumulate("weight", comp.getWeight())
            .accumulate("bodyfat", comp.getBodyFatPercentage())
            .accumulate("muscle", comp.getMuscleWeight());
    }

    @Override
    public Boolean process(int responseCode, JSONObject json, Map<String, List<String>> headers) throws JSONException {
        if(responseCode == HttpsURLConnection.HTTP_OK) {
            return json.getBoolean("submitted");
        }

        return false;
    }
}
