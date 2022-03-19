package com.example.healthapp.ui.auth;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.healthapp.R;
import com.example.healthapp.backend.auth.RESTTaskSignIn;
import com.example.healthapp.ui.MainActivity;

import java.util.function.Consumer;

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
        String user = getTextFieldContents(R.id.logInFragUsername);
        String pass  = getTextFieldContents(R.id.logInFragPassword);

        Consumer<String> errHandler = error -> new AlertDialog.Builder(getContext()).setNeutralButton("Ok", null).setMessage(error).show();

        if(user == null || user.isEmpty() || pass == null || pass.isEmpty()) {
            errHandler.accept("Please fill out all fields.");
        } else {
            RESTTaskSignIn.enqueue(user, pass, () -> { startActivity(new Intent(getActivity(), MainActivity.class)); }, () -> errHandler.accept("Invalid credentials, please try again."));
        }
    }
}
