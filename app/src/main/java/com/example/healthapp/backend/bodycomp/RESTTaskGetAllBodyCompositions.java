package com.example.healthapp.backend.bodycomp;

import com.example.healthapp.HealthApplication;
import com.example.healthapp.backend.RESTTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.net.ssl.HttpsURLConnection;

public class RESTTaskGetAllBodyCompositions implements RESTTask<List<BodyComposition>> {

    public static void enqueue(Consumer<List<BodyComposition>> onSuccess, Consumer<String> onFailure) {
        Consumer<List<BodyComposition>> onSuccessProxy = val -> {
          if(val == null) onFailure.accept("API failure");
          else onSuccess.accept(val);
        };

        HealthApplication.getInstance().getAPIClient().submitTask(new RESTTaskGetAllBodyCompositions(), onSuccessProxy, () -> onFailure.accept("API failure"));
    }

    @Override public String getMethod() { return "GET"; }
    @Override public String getEndpoint() { return "bodycomp"; }
    @Override public String getMessage() { return "Getting body composition history..."; }
    @Override public JSONObject getParameters() throws JSONException { return null; }

    @Override
    public List<BodyComposition> process(int responseCode, JSONObject json, Map<String, List<String>> headers) throws JSONException {
        if(responseCode == HttpsURLConnection.HTTP_OK) {
            JSONArray historyRaw = json.getJSONArray("comp_history");
            ArrayList<BodyComposition> history = new ArrayList<>();

            for(int i = 0; i < historyRaw.length(); i++) {
                JSONObject compRaw = historyRaw.getJSONObject(i);
                history.add(new BodyComposition(compRaw.getInt("weight"), compRaw.getInt("fatpercentage"), compRaw.getInt("muscle"), LocalDate.ofYearDay(compRaw.getInt("year"), compRaw.getInt("day"))));
            }

            return history;
        }

        return null;
    }
}
