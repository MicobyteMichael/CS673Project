package com.example.healthapp.backend.goals;

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

public class RESTTaskGetAllGoalAchievements implements RESTTask<String[]> {

    private final LocalDate day;

    public static void enqueue(Consumer<String[]> onSuccess, Consumer<String> onFailure) {
        enqueue(null, onSuccess, onFailure);
    }

    public static void enqueue(LocalDate day, Consumer<String[]> onSuccess, Consumer<String> onFailure) {
        if(day == null) day = LocalDate.now();

        Consumer<String[]> onSuccessProxy = val -> {
            if(val == null) onFailure.accept("API failure again");
            else onSuccess.accept(val);
        };

        HealthApplication.getInstance().getAPIClient().submitTask(new RESTTaskGetAllGoalAchievements(day), onSuccessProxy, () -> onFailure.accept("API failure"));
    }

    public RESTTaskGetAllGoalAchievements(LocalDate day) { this.day = day; }

    @Override public String getMethod() { return "PATCH"; }
    @Override public String getEndpoint() { return "goalsuccesses"; }
    @Override public String getMessage() { return "Getting all goal successes..."; }
    @Override public JSONObject getParameters() throws JSONException { return new JSONObject().accumulate("year", day.getYear()).accumulate("day", day.getDayOfYear()); }

    @Override
    public String[] process(int responseCode, JSONObject json, Map<String, List<String>> headers) throws JSONException {
        if(responseCode == HttpsURLConnection.HTTP_OK) {
            JSONArray raw = json.getJSONArray("successes");
            String[] successes = new String[raw.length()];
            for(int i = 0; i < raw.length(); i++) successes[i] = raw.getString(i);

            return successes;
        }

        return null;
    }
}
