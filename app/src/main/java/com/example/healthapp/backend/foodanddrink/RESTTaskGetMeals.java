package com.example.healthapp.backend.foodanddrink;

import com.example.healthapp.HealthApplication;
import com.example.healthapp.backend.RESTTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.net.ssl.HttpsURLConnection;

public class RESTTaskGetMeals implements RESTTask<Meal[]> {

    private final LocalDate day;

    public static void enqueue(Consumer<Meal[]> onSuccess, Consumer<String> onFailure) {
        enqueue(null, onSuccess, onFailure);
    }

    public static void enqueue(LocalDate day, Consumer<Meal[]> onSuccess, Consumer<String> onFailure) {
        if(day == null) day = LocalDate.now();

        Consumer<Meal[]> onSuccessProxy = val -> {
          if(val == null) onFailure.accept("API failure");
          else onSuccess.accept(val);
        };

        HealthApplication.getInstance().getAPIClient().submitTask(new RESTTaskGetMeals(day), onSuccessProxy, () -> onFailure.accept("API failure"));
    }

    public RESTTaskGetMeals(LocalDate day) { this.day = day; }

    @Override public String getMethod() { return "POST"; }
    @Override public String getEndpoint() { return "mealintake"; }
    @Override public String getMessage() { return "Getting meal intake..."; }
    @Override public JSONObject getParameters() throws JSONException { return new JSONObject().accumulate("year", day.getYear()).accumulate("day", day.getDayOfYear()); }

    @Override
    public Meal[] process(int responseCode, JSONObject json, Map<String, List<String>> headers) throws JSONException {
        if(responseCode == HttpsURLConnection.HTTP_OK) {
            JSONArray mealsRaw = json.getJSONArray("meals");
            Meal[] meals = new Meal[mealsRaw.length()];

            for(int i = 0; i < meals.length; i++) {
                JSONObject mealRaw = mealsRaw.getJSONObject(i);
                meals[i] = new Meal(mealRaw.getString("meal"), mealRaw.getInt("calories"));
            }

            return meals;
        }

        return null;
    }
}
