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
import com.example.healthapp.backend.auth.RESTTaskRegister;
import com.example.healthapp.ui.MainActivity;

import java.util.Locale;
import java.util.function.Consumer;

public class LoginActivityFragmentSignUp extends Fragment {

    public LoginActivityFragmentSignUp() {
        super(R.layout.signup_fragment);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ((LoginActivity)getActivity()).assignButtonToToggle(R.id.signUpFragSignIn);
        ((Button)getActivity().findViewById(R.id.signUpFragSignUp)).setOnClickListener(v -> onSignUpClicked());
    }

    private String getTextFieldContents(int id) {
        return ((EditText)getActivity().findViewById(id)).getText().toString().trim();
    }

    private void onSignUpClicked() {
        String user  = getTextFieldContents(R.id.signUpFragUsername       );
        String email = getTextFieldContents(R.id.signUpFragEmail          );
        String phone = getTextFieldContents(R.id.signUpFragPhoneNumber    );
        String pass  = getTextFieldContents(R.id.signUpFragPassword       );
        String pass2 = getTextFieldContents(R.id.signUpFragConfirmPassword);

        Consumer<String> errHandler = error -> new AlertDialog.Builder(getContext()).setNeutralButton("Ok", null).setMessage(error).show();
        String error = null;

        if(user.isEmpty() || email.isEmpty() || phone.isEmpty() || pass.isEmpty() || pass2.isEmpty()) {
            error = "Please fill out all fields.";
        } else if(!pass.equals(pass2)) {
            error = "Passwords don't match!";
        } else if(pass.length() < 10) {
            error = "Password is too short!";
        } else if(user.length() < 10) {
            error = "Username is too short!";
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            error = "Invalid email address!";
        } else if(!Patterns.PHONE.matcher(phone).matches()) {
            error = "Invalid phone number!";
        }

        if(error == null) {
            String phoneFiltered = "";
            for(char ch : phone.toCharArray()) if(Character.isDigit(ch)) phoneFiltered += ch;

            RESTTaskRegister.enqueue(user.toLowerCase(Locale.ROOT), pass, email.toLowerCase(Locale.ROOT), phoneFiltered,
                () -> startActivity(new Intent(getActivity(), MainActivity.class)),
                failedReason -> errHandler.accept(failedReason == null ? "API failure" : failedReason)
            );
        } else {
            errHandler.accept(error);
        }
    }
}
