package com.example.healthapp;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;

public class LoginActivityFragmentLogIn extends Fragment {

    public LoginActivityFragmentLogIn() {
        super(R.layout.login_tab_fragment);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ((LoginActivity)getActivity()).assignButtonToToggle(R.id.logInFragSignUp);
    }
}
