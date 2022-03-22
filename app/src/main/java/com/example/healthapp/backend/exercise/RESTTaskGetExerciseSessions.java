package com.example.healthapp.backend.exercise;

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

public class RESTTaskGetExerciseSessions implements RESTTask<ExerciseSession[]> {

    private final Date day;

    public static void enqueue(Consumer<ExerciseSession[]> onSuccess, Consumer<String> onFailure) {
        enqueue(null, onSuccess, onFailure);
    }

    public static void enqueue(Date day, Consumer<ExerciseSession[]> onSuccess, Consumer<String> onFailure) {
        if(day == null) {
            day = Date.from(Instant.now());
        }

        Consumer<ExerciseSession[]> onSuccessProxy = val -> {
          if(val == null) onFailure.accept("API failure");
          else onSuccess.accept(val);
        };

        HealthApplication.getInstance().getAPIClient().submitTask(new RESTTaskGetExerciseSessions(day), onSuccessProxy, () -> onFailure.accept("API failure"));
    }

    public RESTTaskGetExerciseSessions(Date day) { this.day = day; }

    @Override public String getMethod() { return "POST"; }
    @Override public String getEndpoint() { return "exercise"; }
    @Override public String getMessage() { return "Getting exercise sessions..."; }
    @Override public JSONObject getParameters() throws JSONException { return new JSONObject().accumulate("year", day.getYear() + 1900).accumulate("day", day.getDay()); }

    @Override
    public ExerciseSession[] process(int responseCode, JSONObject json, Map<String, List<String>> headers) throws JSONException {
        if(responseCode == HttpsURLConnection.HTTP_OK) {
            JSONArray sessionsRaw = json.getJSONArray("sessions");
            ExerciseSession[] sessions = new ExerciseSession[sessionsRaw.length()];

            for(int i = 0; i < sessions.length; i++) {
                JSONObject sessionRaw = sessionsRaw.getJSONObject(i);
                long start = -1, end = -1;
                int heart = -1, calories = -1;

                if(sessionRaw.has("start") && sessionRaw.get("start") != JSONObject.NULL) {
                    start = sessionRaw.getLong("start");
                }

                if(sessionRaw.has("end") && sessionRaw.get("end") != JSONObject.NULL) {
                    end = sessionRaw.getLong("end");
                }

                if(sessionRaw.has("heartrate") && sessionRaw.get("heartrate") != JSONObject.NULL) {
                    heart = sessionRaw.getInt("heartrate");
                }

                if(sessionRaw.has("calories") && sessionRaw.get("calories") != JSONObject.NULL) {
                    calories = sessionRaw.getInt("calories");
                }

                sessions[i] = new ExerciseSession(sessionRaw.getString("name"), sessionRaw.getString("type"), start, end, heart, calories);
            }

            return sessions;
        }

        return null;
    }
}
