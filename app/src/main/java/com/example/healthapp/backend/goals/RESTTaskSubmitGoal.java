package com.example.healthapp.backend.goals;

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

public class RESTTaskSubmitGoal implements RESTTask<Boolean> {

    private final Goal goal;

    public static void enqueue(Goal goal, Runnable onSuccess, Consumer<String> onFailure) {
        Consumer<Boolean> onSuccessProxy = val -> {
          if(val) onSuccess.run();
          else onFailure.accept("API failure again");
        };

        HealthApplication.getInstance().getAPIClient().submitTask(new RESTTaskSubmitGoal(goal), onSuccessProxy, () -> onFailure.accept("API failure"));
    }

    public RESTTaskSubmitGoal(Goal goal) { this.goal = goal; }

    @Override public String getMethod() { return "PUT"; }
    @Override public String getEndpoint() { return "goals"; }
    @Override public String getMessage() { return "Saving goal..."; }

    @Override
    public JSONObject getParameters() throws JSONException {
        return new JSONObject()
            .accumulate("name", goal.getName())
            .accumulate("type", goal.getGoalType())
            .accumulate("comp", goal.getGoalComparison())
            .accumulate("thresh", goal.getThreshold())
            .accumulate("param", goal.getGoalParameter())
            .accumulate("active", goal.isActive());
    }

    @Override
    public Boolean process(int responseCode, JSONObject json, Map<String, List<String>> headers) throws JSONException {
        if(responseCode == HttpsURLConnection.HTTP_OK) {
            return json.getBoolean("added");
        }

        return false;
    }
}
