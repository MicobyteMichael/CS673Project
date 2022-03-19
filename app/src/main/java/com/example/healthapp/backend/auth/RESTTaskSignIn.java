package com.example.healthapp.backend.auth;

import com.example.healthapp.HealthApplication;
import com.example.healthapp.backend.RESTTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.net.ssl.HttpsURLConnection;

public class RESTTaskSignIn implements RESTTask<Boolean> {

    private final String username, password;

    public static void enqueue(String username, String password, Runnable onSuccess, Runnable onFailed) {
        Consumer<Boolean> onSuccessProxy = success -> {
            if(success) onSuccess.run();
            else onFailed.run();
        };

        HealthApplication.getInstance().getAPIClient().<Boolean>submitTask(new RESTTaskSignIn(username, password), onSuccessProxy, onFailed);
    }

    public RESTTaskSignIn(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override public String getMethod() { return "POST"; }
    @Override public String getEndpoint() { return "login"; }
    @Override public String getMessage() { return "Logging in..."; }

    @Override
    public JSONObject getParameters() throws JSONException {
        return new JSONObject().accumulate("username", username).accumulate("password", password);
    }

    @Override
    public Boolean process(int responseCode, JSONObject json, Map<String, List<String>> headers) throws JSONException {
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            return json.getBoolean("authenticated");
        }

        return false;
    }
}
