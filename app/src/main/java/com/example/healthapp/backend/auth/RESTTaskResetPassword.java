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

    public static void enqueue(String username, String email, String phone, String newPass, String newPass2, Runnable onSuccess, Consumer<String> onFailed) {
        String error = AuthDataValidator.validate(username, email, phone, newPass, newPass2);

        if(error == null) {
            Consumer<Boolean> onSuccessProxy = success -> {
                if (success) onSuccess.run();
                else onFailed.accept("No account found with the specified username, email, and phone number.");
            };

            HealthApplication.getInstance().getAPIClient().<Boolean>submitTask(new RESTTaskResetPassword(username, email, phone, newPass), onSuccessProxy, () -> onFailed.accept("API Failure"));
        } else {
            onFailed.accept(error);
        }
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
        return new JSONObject()
            .accumulate("username", AuthDataValidator.stripUsername(username))
            .accumulate("email",    AuthDataValidator.stripEmail(email))
            .accumulate("phone",    AuthDataValidator.stripPhone(phone))
            .accumulate("newPass",  newPass);
    }

    @Override
    public Boolean process(int responseCode, JSONObject json, Map<String, List<String>> headers) throws JSONException {
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            return json.getBoolean("reset");
        }

        return false;
    }
}
