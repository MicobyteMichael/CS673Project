package com.example.healthapp.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.healthapp.R;
import com.example.healthapp.backend.auth.AuthDataValidator;
import com.example.healthapp.backend.auth.RESTTaskGetUserInfo;

public class AccountSettingsFragment extends PreferenceFragmentCompat {

    private EditTextPreference username, email, phone;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        username = getPreferenceManager().<EditTextPreference>findPreference("username");
        email = getPreferenceManager().<EditTextPreference>findPreference("email");
        phone = getPreferenceManager().<EditTextPreference>findPreference("phone");

        username.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
               String error = AuthDataValidator.validate((String)newValue, null, null, null, null);

               if(error == null) {
                   return true;
               } else {
                   new AlertDialog.Builder(getActivity()).setMessage(error).setNeutralButton("OK", null).show();
                   return false;
               }
            }
        });

        RESTTaskGetUserInfo.enqueue(
            data -> {
                String usernameData = data[0];
                String phoneData = data[1];
                String emailData = data[2];

                username.setText(usernameData);
                email.setText(emailData);
                phone.setText(phoneData);
            },
            () -> new AlertDialog.Builder(getActivity()).setNeutralButton("Ok", null).setMessage("Failed to download user preferences!").show()
        );
    }
}