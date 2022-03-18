package com.example.healthapp.ui.auth;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.healthapp.R;

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
        return ((EditText)getActivity().findViewById(id)).getText().toString().trim();
    }

    private void onSignInClicked() {
        String email = getTextFieldContents(R.id.logInFragEmail);
        String pass  = getTextFieldContents(R.id.logInFragPassword);

        String error = null;

        if(email == null || email.isEmpty() || pass == null || pass.isEmpty()) {
            error = "Please fill out all fields.";
        } else if(pass.length() < 10) {
            error = "Password is too short!";
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            error = "Invalid email address!";
        }

        if(error == null) {
            System.out.println("sign in: " + email + "|" + pass);
        } else {
            new AlertDialog.Builder(getContext()).setNeutralButton("Ok", null).setMessage(error).show();
        }
    }
}
