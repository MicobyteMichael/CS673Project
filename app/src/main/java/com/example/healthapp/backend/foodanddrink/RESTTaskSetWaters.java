package com.example.healthapp.backend.foodanddrink;

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

public class RESTTaskSetWaters implements RESTTask<Boolean> {

    private final LocalDate day;
    private final int numGlasses;

    public static void enqueue(int numGlasses, Runnable onSuccess, Consumer<String> onFailure) {
        enqueue(null, numGlasses, onSuccess, onFailure);
    }

    public static void enqueue(LocalDate day, int numGlasses, Runnable onSuccess, Consumer<String> onFailure) {
        if(day == null) day = LocalDate.now();

        Consumer<Boolean> onSuccessProxy = val -> {
          if(val) onSuccess.run();
          else onFailure.accept("API failure again");
        };

        HealthApplication.getInstance().getAPIClient().submitTask(new RESTTaskSetWaters(day, numGlasses), onSuccessProxy, () -> onFailure.accept("API failure"));
    }

    public RESTTaskSetWaters(LocalDate day, int numGlasses) { this.day = day; this.numGlasses = numGlasses; }

    @Override public String getMethod() { return "PUT"; }
    @Override public String getEndpoint() { return "waterintake"; }
    @Override public String getMessage() { return "Saving water intake..."; }
    @Override public JSONObject getParameters() throws JSONException { return new JSONObject().accumulate("year", day.getYear()).accumulate("day", day.getDayOfYear()).accumulate("glasses", numGlasses); }

    @Override
    public Boolean process(int responseCode, JSONObject json, Map<String, List<String>> headers) throws JSONException {
        if(responseCode == HttpsURLConnection.HTTP_OK) {
            return json.getBoolean("updated");
        }

        return false;
    }
}
