package com.example.healthapp.backend.auth;

import com.example.healthapp.HealthApplication;
import com.example.healthapp.backend.RESTTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.net.ssl.HttpsURLConnection;

public class RESTTaskRegister implements RESTTask<Object[]> {

    private final String username, password, email, phone;

    public static void enqueue(String username, String password, String email, String phone, Runnable onSuccess, Consumer<String> onFailed) {
        Consumer<Object[]> onSuccessProxy = createdAndReason -> {
            boolean created = (boolean)createdAndReason[0];
            String reason = (String)createdAndReason[1];

            if(created) onSuccess.run();
            else onFailed.accept(reason);
        };

        HealthApplication.getInstance().getAPIClient().<Object[]>submitTask(new RESTTaskRegister(username, password, email, phone), onSuccessProxy, () -> onFailed.accept(null));
    }

    public RESTTaskRegister(String username, String password, String email, String phone) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }

    @Override public String getMethod() { return "POST"; }
    @Override public String getEndpoint() { return "register"; }

    @Override
    public JSONObject getParameters() throws JSONException {
        return new JSONObject().accumulate("username", username).accumulate("password", password).accumulate("email", email).accumulate("phone", phone);
    }

    @Override
    public Object[] process(int responseCode, JSONObject json, Map<String, List<String>> headers) throws JSONException {
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            boolean created = json.getBoolean("created");
            String reason = null;

            if(json.has("reason")) {
                reason = json.getString("reason");
            }

            return new Object[] { created, reason };
        }

        return new Object[] { false, null };
    }
}
