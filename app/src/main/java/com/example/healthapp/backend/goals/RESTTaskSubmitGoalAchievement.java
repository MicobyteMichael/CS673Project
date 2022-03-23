package com.example.healthapp.backend.goals;

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

public class RESTTaskSubmitGoalAchievement implements RESTTask<Boolean> {

    private final LocalDate day;
    private final String name;

    public static void enqueue(String name, Runnable onSuccess, Consumer<String> onFailure) {
        enqueue(null, name, onSuccess, onFailure);
    }

    public static void enqueue(LocalDate day, String name, Runnable onSuccess, Consumer<String> onFailure) {
        if(day == null) day = LocalDate.now();

        Consumer<Boolean> onSuccessProxy = val -> {
          if(val) onSuccess.run();
          else onFailure.accept("API failure again");
        };

        HealthApplication.getInstance().getAPIClient().submitTask(new RESTTaskSubmitGoalAchievement(day, name), onSuccessProxy, () -> onFailure.accept("API failure"));
    }

    public RESTTaskSubmitGoalAchievement(LocalDate day, String name) { this.day = day; this.name = name; }

    @Override public String getMethod() { return "PUT"; }
    @Override public String getEndpoint() { return "goalsuccesses"; }
    @Override public String getMessage() { return "Saving goal success..."; }
    @Override public JSONObject getParameters() throws JSONException { return new JSONObject().accumulate("year", day.getYear()).accumulate("day", day.getDayOfYear()).accumulate("name", name); }

    @Override
    public Boolean process(int responseCode, JSONObject json, Map<String, List<String>> headers) throws JSONException {
        if(responseCode == HttpsURLConnection.HTTP_OK) {
            return json.getBoolean("submitted");
        }

        return false;
    }
}
