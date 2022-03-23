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

public class RESTTaskEndExerciseSession implements RESTTask<Boolean> {

    private final LocalDate day;
    private final String name;
    private final Instant endTime;
    private final int averageHeartRate, caloriesBurned;
    private final float measuredValue;

    public static void enqueue(String name, int averageHeartRate, int caloriesBurned, float measuredValue, Runnable onSuccess, Consumer<String> onFailure) {
        enqueue(null, name, null, averageHeartRate, caloriesBurned, measuredValue, onSuccess, onFailure);
    }

    public static void enqueue(LocalDate day, String name, Instant endTime, int averageHeartRate, int caloriesBurned, float measuredValue, Runnable onSuccess, Consumer<String> onFailure) {
        if(day == null) day = LocalDate.now();
        if(endTime == null) endTime = Instant.now();

        Consumer<Boolean> onSuccessProxy = val -> {
          if(val) onSuccess.run();
          else onFailure.accept("API failure again");
        };

        HealthApplication.getInstance().getAPIClient().submitTask(new RESTTaskEndExerciseSession(day, name, endTime, averageHeartRate, caloriesBurned, measuredValue), onSuccessProxy, () -> onFailure.accept("API failure"));
    }

    public RESTTaskEndExerciseSession(LocalDate day, String name, Instant endTime, int averageHeartRate, int caloriesBurned, float measuredValue) { this.day = day; this.name = name; this.endTime = endTime; this.averageHeartRate = averageHeartRate; this.caloriesBurned = caloriesBurned; this.measuredValue = measuredValue; }

    @Override public String getMethod() { return "PATCH"; }
    @Override public String getEndpoint() { return "exercise"; }
    @Override public String getMessage() { return "Saving exercise session..."; }

    @Override public JSONObject getParameters() throws JSONException {
        return new JSONObject()
            .accumulate("year",      day.getYear())
            .accumulate("day",       day.getDayOfYear())
            .accumulate("name",      name)
            .accumulate("end",       endTime.getEpochSecond())
            .accumulate("heartrate", averageHeartRate)
            .accumulate("calories",  caloriesBurned)
            .accumulate("parameter", measuredValue);
    }

    @Override
    public Boolean process(int responseCode, JSONObject json, Map<String, List<String>> headers) throws JSONException {
        if(responseCode == HttpsURLConnection.HTTP_OK) {
            return json.getBoolean("updated");
        }

        return false;
    }
}
