package com.example.healthapp.backend.auth;

import com.example.healthapp.HealthApplication;
import com.example.healthapp.backend.RESTTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.net.ssl.HttpsURLConnection;

public class RESTTaskGetUserInfo implements RESTTask<String[]> {

    public static void enqueue(Consumer<String[]> onSuccess, Runnable onFailure) {
        Consumer<String[]> onSuccessProxy = data -> {
            if(data == null) onFailure.run();
            else onSuccess.accept(data);
        };

        HealthApplication.getInstance().getAPIClient().submitTask(new RESTTaskGetUserInfo(), onSuccessProxy, onFailure);
    }

    @Override public String getMethod() { return "GET"; }
    @Override public String getEndpoint() { return "userinfo"; }
    @Override public String getMessage() { return "Downloading user info..."; }
    @Override public JSONObject getParameters() throws JSONException { return null; }

    @Override
    public String[] process(int responseCode, JSONObject json, Map<String, List<String>> headers) throws JSONException {
        if(responseCode == HttpsURLConnection.HTTP_OK) {
            if(json.getBoolean("loggedIn")) {
                String username = json.getString("username");
                String email = json.getString("email");
                String phone = json.getString("phone");

                return new String[] { username, email, phone };
            }
        }

        return null;
    }
}
