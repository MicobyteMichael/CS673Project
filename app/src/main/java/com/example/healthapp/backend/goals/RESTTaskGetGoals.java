package com.example.healthapp.backend.goals;

import com.example.healthapp.HealthApplication;
import com.example.healthapp.backend.RESTTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.net.ssl.HttpsURLConnection;

public class RESTTaskGetGoals implements RESTTask<Goal[]> {

    public static void enqueue(Consumer<Goal[]> onSuccess, Consumer<String> onFailure) {
        Consumer<Goal[]> onSuccessProxy = val -> {
          if(val == null) onFailure.accept("API failure");
          else onSuccess.accept(val);
        };

        HealthApplication.getInstance().getAPIClient().submitTask(new RESTTaskGetGoals(), onSuccessProxy, () -> onFailure.accept("API failure"));
    }

    @Override public String getMethod() { return "GET"; }
    @Override public String getEndpoint() { return "goals"; }
    @Override public String getMessage() { return "Getting goals..."; }
    @Override public JSONObject getParameters() throws JSONException { return null; }

    @Override
    public Goal[] process(int responseCode, JSONObject json, Map<String, List<String>> headers) throws JSONException {
        if(responseCode == HttpsURLConnection.HTTP_OK) {
            JSONArray goalsRaw = json.getJSONArray("meals");
            ArrayList<Goal> goals = new ArrayList<>();

            for(int i = 0; i < goalsRaw.length(); i++) {
                JSONObject goalRaw = goalsRaw.getJSONObject(i);
                Goal g = Goal.parseGoal(goalRaw.getString("name"), goalRaw.getString("type"), goalRaw.getString("comp"), goalRaw.getInt("thresh"), goalRaw.getString("param"), goalRaw.getBoolean("active"));

                if(g != null) goals.add(g);
            }

            return goals.toArray(new Goal[goals.size()]);
        }

        return null;
    }
}
