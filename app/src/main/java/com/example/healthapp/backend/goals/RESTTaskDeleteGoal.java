package com.example.healthapp.backend.goals;

import com.example.healthapp.HealthApplication;
import com.example.healthapp.backend.RESTTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.net.ssl.HttpsURLConnection;

public class RESTTaskDeleteGoal implements RESTTask<Boolean> {

    private final String goalName;

    public static void enqueue(String goalName, Runnable onSuccess, Consumer<String> onFailure) {
        Consumer<Boolean> onSuccessProxy = val -> {
          if(val) onSuccess.run();
          else onFailure.accept("API failure again");
        };

        HealthApplication.getInstance().getAPIClient().submitTask(new RESTTaskDeleteGoal(goalName), onSuccessProxy, () -> onFailure.accept("API failure"));
    }

    public RESTTaskDeleteGoal(String goalName) { this.goalName = goalName; }

    @Override public String getMethod() { return "DELETE"; }
    @Override public String getEndpoint() { return "goals"; }
    @Override public String getMessage() { return "Deleting goal..."; }

    @Override
    public JSONObject getParameters() throws JSONException {
        return new JSONObject().accumulate("name", goalName);
    }

    @Override
    public Boolean process(int responseCode, JSONObject json, Map<String, List<String>> headers) throws JSONException {
        if(responseCode == HttpsURLConnection.HTTP_OK) {
            return json.getBoolean("removed");
        }

        return false;
    }
}
