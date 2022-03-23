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

public class RESTTaskStartExerciseSession implements RESTTask<Boolean> {

    private final LocalDate day;
    private final String name;
    private final ExerciseType exerciseType;
    private final Instant startTime;

    public static void enqueue(String name, ExerciseType exerciseType, Runnable onSuccess, Consumer<String> onFailure) {
        enqueue(null, name, null, exerciseType, onSuccess, onFailure);
    }

    public static void enqueue(LocalDate day, String name, Instant startTime, ExerciseType exerciseType, Runnable onSuccess, Consumer<String> onFailure) {
        if(day == null) day = LocalDate.now();
        if(startTime == null) startTime = Instant.now();

        Consumer<Boolean> onSuccessProxy = val -> {
          if(val) onSuccess.run();
          else onFailure.accept("API failure again");
        };

        HealthApplication.getInstance().getAPIClient().submitTask(new RESTTaskStartExerciseSession(day, name, startTime, exerciseType), onSuccessProxy, () -> onFailure.accept("API failure"));
    }

    public RESTTaskStartExerciseSession(LocalDate day, String name, Instant startTime, ExerciseType exerciseType) { this.day = day; this.name = name; this.startTime = startTime; this.exerciseType = exerciseType; }

    @Override public String getMethod() { return "PUT"; }
    @Override public String getEndpoint() { return "exercise"; }
    @Override public String getMessage() { return "Starting exercise session..."; }

    @Override public JSONObject getParameters() throws JSONException {
        return new JSONObject()
            .accumulate("year",  day.getYear())
            .accumulate("day",   day.getDayOfYear())
            .accumulate("name", name)
            .accumulate("start", startTime.getEpochSecond())
            .accumulate("type",  exerciseType.name());
    }

    @Override
    public Boolean process(int responseCode, JSONObject json, Map<String, List<String>> headers) throws JSONException {
        if(responseCode == HttpsURLConnection.HTTP_OK) {
            return json.getBoolean("added");
        }

        return false;
    }
}
