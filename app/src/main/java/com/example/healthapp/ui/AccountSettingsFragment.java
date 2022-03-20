package com.example.healthapp.ui;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.healthapp.R;
import com.example.healthapp.backend.auth.AuthDataValidator;
import com.example.healthapp.backend.auth.RESTTaskGetUserInfo;
import com.example.healthapp.backend.auth.RESTTaskUpdateUserInfo;

import java.util.function.BiFunction;
import java.util.function.Consumer;

public class AccountSettingsFragment extends PreferenceFragmentCompat {

    private final String DEFAULT_HIDDEN_PASSWORD_TEXT = "Change this if desired";
    private EditTextPreference username, password, email, phone;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        username = getPreferenceManager().<EditTextPreference>findPreference("username");
        password = getPreferenceManager().<EditTextPreference>findPreference("password");
        email = getPreferenceManager().<EditTextPreference>findPreference("email");
        phone = getPreferenceManager().<EditTextPreference>findPreference("phone");

        BiFunction<String, Integer, Preference.OnPreferenceChangeListener> changeListenerFactory = (desc, argPos) ->
            new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if(argPos == 3 && DEFAULT_HIDDEN_PASSWORD_TEXT.equals(newValue)) {
                        return false;
                    }

                    String[] params = { null, null, null, null };
                    params[argPos] = (String)newValue;

                    String error = AuthDataValidator.validate(params[0], params[1], params[2], params[3], null);
                    Consumer<String> alertCreator = msg -> new AlertDialog.Builder(getActivity()).setMessage(msg).setNeutralButton("OK", null).show();

                    if(error == null) {
                        RESTTaskUpdateUserInfo.enqueue(params[0], params[3], params[1], params[2],
                            () -> alertCreator.accept(desc + " updated!"),
                            alertCreator
                        );

                        return argPos != 3;
                    } else {
                        alertCreator.accept(error);
                        return false;
                    }
                }
            };

        username.setOnPreferenceChangeListener(changeListenerFactory.apply("Username",     0));
        password.setOnPreferenceChangeListener(changeListenerFactory.apply("Password",     3));
        email   .setOnPreferenceChangeListener(changeListenerFactory.apply("Email",        1));
        phone   .setOnPreferenceChangeListener(changeListenerFactory.apply("Phone number", 2));

        RESTTaskGetUserInfo.enqueue(
            data -> {
                String usernameData = data[0];
                String emailData = data[1];
                String phoneData = data[2];

                username.setText(usernameData);
                email.setText(emailData);
                phone.setText(phoneData);
            },
            () -> new AlertDialog.Builder(getActivity()).setNeutralButton("Ok", null).setMessage("Failed to download user preferences!").show()
        );

        password.setText(DEFAULT_HIDDEN_PASSWORD_TEXT);
    }
}