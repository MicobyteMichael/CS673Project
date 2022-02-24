package com.example.healthapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

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
        return ((EditText)getActivity().findViewById(id)).getText().toString();
    }

    private void onSignUpClicked() {
        String email = getTextFieldContents(R.id.signUpFragEmail          );
        String phone = getTextFieldContents(R.id.signUpFragPhoneNumber    );
        String pass1 = getTextFieldContents(R.id.signUpFragPassword       );
        String pass2 = getTextFieldContents(R.id.signUpFragConfirmPassword);

        System.out.println("sign up: " + email + "|" + phone + "|" + pass1 + "|" + pass2);
    }
}
