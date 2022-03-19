package com.example.healthapp.ui;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.healthapp.R;

public class AccountSettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}