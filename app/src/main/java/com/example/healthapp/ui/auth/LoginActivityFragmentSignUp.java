package com.example.healthapp.ui.auth;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.healthapp.HealthApplication;
import com.example.healthapp.R;
import com.example.healthapp.backend.auth.AuthDataValidator;
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

        RESTTaskRegister.enqueue(user, pass, pass2, email, phone,
            ()  -> startActivity(new Intent(getActivity(), MainActivity.class)),
            err -> new AlertDialog.Builder(getActivity()).setMessage(err).setNeutralButton("OK", null).show()
        );
    }
}
