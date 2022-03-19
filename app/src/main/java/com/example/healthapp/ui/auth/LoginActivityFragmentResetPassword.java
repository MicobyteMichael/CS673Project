package com.example.healthapp.ui.auth;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.healthapp.HealthApplication;
import com.example.healthapp.R;
import com.example.healthapp.backend.auth.AuthDataValidator;
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

        RESTTaskResetPassword.enqueue(user, email, phone, pass, pass2,
            ()  -> startActivity(new Intent(getActivity(), MainActivity.class)),
            err -> new AlertDialog.Builder(getActivity()).setMessage(err).setNeutralButton("OK", null).show()
        );
    }
}