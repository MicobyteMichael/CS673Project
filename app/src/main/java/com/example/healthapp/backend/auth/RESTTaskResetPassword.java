package com.example.healthapp.backend.auth;

import com.example.healthapp.HealthApplication;
import com.example.healthapp.backend.RESTTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.net.ssl.HttpsURLConnection;

public class RESTTaskResetPassword implements RESTTask<Boolean> {

    private final String username, email, phone, newPass;

    public static void enqueue(String username, String email, String phone, String newPass, Runnable onSuccess, Runnable onFailed) {
        Consumer<Boolean> onSuccessProxy = success -> {
            if(success) onSuccess.run();
            else onFailed.run();
        };

        HealthApplication.getInstance().getAPIClient().<Boolean>submitTask(new RESTTaskResetPassword(username, email, phone, newPass), onSuccessProxy, onFailed);
    }

    public RESTTaskResetPassword(String username, String email, String phone, String newPass) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.newPass = newPass;
    }

    @Override public String getMethod() { return "POST"; }
    @Override public String getEndpoint() { return "resetpass"; }
    @Override public String getMessage() { return "Attempting reset..."; }

    @Override
    public JSONObject getParameters() throws JSONException {
        return new JSONObject().accumulate("username", username).accumulate("email", email).accumulate("phone", phone).accumulate("newPass", newPass);
    }

    @Override
    public Boolean process(int responseCode, JSONObject json, Map<String, List<String>> headers) throws JSONException {
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            return json.getBoolean("reset");
        }

        return false;
    }
}
