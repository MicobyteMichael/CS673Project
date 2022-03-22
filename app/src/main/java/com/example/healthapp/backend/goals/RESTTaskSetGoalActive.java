package com.example.healthapp.backend.goals;

import com.example.healthapp.HealthApplication;
import com.example.healthapp.backend.RESTTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.net.ssl.HttpsURLConnection;

public class RESTTaskSetGoalActive implements RESTTask<Boolean> {

    private final String name;
    private final boolean active;

    public static void enqueue(String name, boolean active, Runnable onSuccess, Consumer<String> onFailure) {
        Consumer<Boolean> onSuccessProxy = val -> {
          if(val) onSuccess.run();
          else onFailure.accept("API failure again");
        };

        HealthApplication.getInstance().getAPIClient().submitTask(new RESTTaskSetGoalActive(name, active), onSuccessProxy, () -> onFailure.accept("API failure"));
    }

    public RESTTaskSetGoalActive(String name, boolean active) { this.name = name; this.active = active; }

    @Override public String getMethod() { return "PATCH"; }
    @Override public String getEndpoint() { return "goals"; }
    @Override public String getMessage() { return (active ? "En" : "Dis") + "abling goal..."; }
    @Override public JSONObject getParameters() throws JSONException { return new JSONObject().accumulate("name", name).accumulate("active", active); }

    @Override
    public Boolean process(int responseCode, JSONObject json, Map<String, List<String>> headers) throws JSONException {
        if(responseCode == HttpsURLConnection.HTTP_OK) {
            return json.getBoolean("updated");
        }

        return false;
    }
}
