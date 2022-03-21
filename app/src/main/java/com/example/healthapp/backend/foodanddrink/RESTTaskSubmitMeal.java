package com.example.healthapp.backend.foodanddrink;

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

public class RESTTaskSubmitMeal implements RESTTask<Boolean> {

    private final Date day;
    private final Meal meal;

    public static void enqueue(Meal meal, Runnable onSuccess, Consumer<String> onFailure) {
        enqueue(null, meal, onSuccess, onFailure);
    }

    public static void enqueue(Date day, Meal meal, Runnable onSuccess, Consumer<String> onFailure) {
        if(day == null) {
            day = Date.from(Instant.now());
        }

        Consumer<Boolean> onSuccessProxy = val -> {
          if(val) onSuccess.run();
          else onFailure.accept("API failure again");
        };

        HealthApplication.getInstance().getAPIClient().submitTask(new RESTTaskSubmitMeal(day, meal), onSuccessProxy, () -> onFailure.accept("API failure"));
    }

    public RESTTaskSubmitMeal(Date day, Meal meal) { this.day = day; this.meal = meal; }

    @Override public String getMethod() { return "PUT"; }
    @Override public String getEndpoint() { return "mealintake"; }
    @Override public String getMessage() { return "Saving meal..."; }
    @Override public JSONObject getParameters() throws JSONException { return new JSONObject().accumulate("year", day.getYear() + 1900).accumulate("day", day.getDay()).accumulate("mealname", meal.getName()).accumulate("calories", meal.getCalories()); }

    @Override
    public Boolean process(int responseCode, JSONObject json, Map<String, List<String>> headers) throws JSONException {
        if(responseCode == HttpsURLConnection.HTTP_OK) {
            return json.getBoolean("added");
        }

        return false;
    }
}
