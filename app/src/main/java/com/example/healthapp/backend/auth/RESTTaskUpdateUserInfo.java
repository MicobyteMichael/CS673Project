package com.example.healthapp.backend.auth;

import com.example.healthapp.HealthApplication;
import com.example.healthapp.backend.RESTTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.net.ssl.HttpsURLConnection;

public class RESTTaskUpdateUserInfo implements RESTTask<Boolean> {

    private final String username, password, email, phone;

    public static void enqueue(String username, String password, String email, String phone, Runnable onSuccess, Consumer<String> onFailed) {
        String error = AuthDataValidator.validate(username, email, phone, password, null);

        if(error == null) {
            Consumer<Boolean> onSuccessProxy = success -> {
                if(success) onSuccess.run();
                else onFailed.accept("No value has been changed");
            };

            HealthApplication.getInstance().getAPIClient().<Boolean>submitTask(new RESTTaskUpdateUserInfo(username, password, email, phone), onSuccessProxy, () -> onFailed.accept("API Failure"));
        } else {
            onFailed.accept(error);
        }
    }

    public RESTTaskUpdateUserInfo(String username, String password, String email, String phone) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }

    @Override public String getMethod() { return "POST"; }
    @Override public String getEndpoint() { return "userinfo"; }
    @Override public String getMessage() { return "Updating user data..."; }

    @Override
    public JSONObject getParameters() throws JSONException {
        JSONObject data = new JSONObject();
        if(username != null) data = data.accumulate("username", AuthDataValidator.stripUsername(username));
        if(password != null) data = data.accumulate("password",                                (password));
        if(email    != null) data = data.accumulate("email",    AuthDataValidator.stripEmail   (email   ));
        if(phone    != null) data = data.accumulate("phone",    AuthDataValidator.stripPhone   (phone   ));
        return data;
    }

    @Override
    public Boolean process(int responseCode, JSONObject json, Map<String, List<String>> headers) throws JSONException {
        if(responseCode == HttpsURLConnection.HTTP_OK) {
            return json.getBoolean("changed");
        }

        return false;
    }
}
