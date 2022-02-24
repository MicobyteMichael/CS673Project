package com.example.healthapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

public class LoginActivityFragmentLogIn extends Fragment {

    public LoginActivityFragmentLogIn() {
        super(R.layout.login_tab_fragment);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ((LoginActivity)getActivity()).assignButtonToToggle(R.id.logInFragSignUp);
        ((Button)getActivity().findViewById(R.id.logInFragSignIn)).setOnClickListener(v -> onSignInClicked());
        ((Button)getActivity().findViewById(R.id.logInFragForgotPassword)).setOnClickListener(v -> onForgotPasswordClicked());
    }

    private void onForgotPasswordClicked() {
        System.out.println("forgot password");
    }

    private String getTextFieldContents(int id) {
        return ((EditText)getActivity().findViewById(id)).getText().toString();
    }

    private void onSignInClicked() {
        String email = getTextFieldContents(R.id.logInFragEmail);
        String password = getTextFieldContents(R.id.logInFragPassword);

        System.out.println("sign in: " + email + "|" + password);
    }
}
