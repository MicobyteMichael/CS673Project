package com.example.healthapp.ui.auth;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.healthapp.R;
import com.example.healthapp.backend.auth.RESTTaskResetPassword;
import com.example.healthapp.ui.MainActivity;

import java.util.Locale;
import java.util.function.Consumer;

public class LoginActivityFragmentResetPassword extends Fragment {

    public LoginActivityFragmentResetPassword() {
        super(R.layout.reset_password_fragment);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ((Button)getActivity().findViewById(R.id.resetPassFragCancel)).setOnClickListener(v -> onCancelClicked());
        ((Button)getActivity().findViewById(R.id.resetPassFragReset)).setOnClickListener(v -> onResetClicked());
    }

    private void onCancelClicked() {
        ((LoginActivity)getActivity()).showFragment(LoginActivityFragmentLogIn.class);
    }

    private String getTextFieldContents(int id) {
        return ((EditText)getActivity().findViewById(id)).getText().toString().trim();
    }

    private void onResetClicked() {
        String user  = getTextFieldContents(R.id.resetPassFragUsername       );
        String email = getTextFieldContents(R.id.resetPassFragEmail          );
        String phone = getTextFieldContents(R.id.resetPassFragPhone          );
        String pass  = getTextFieldContents(R.id.resetPassFragPassword       );
        String pass2 = getTextFieldContents(R.id.resetPassFragPasswordConfirm);

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

            RESTTaskResetPassword.enqueue(user.toLowerCase(Locale.ROOT), email.toLowerCase(Locale.ROOT), phoneFiltered, pass,
                    () -> startActivity(new Intent(getActivity(), MainActivity.class)),
                    () -> errHandler.accept("No account found with the specified username, email, and phone number.")
            );
        } else {
            errHandler.accept(error);
        }
    }
}