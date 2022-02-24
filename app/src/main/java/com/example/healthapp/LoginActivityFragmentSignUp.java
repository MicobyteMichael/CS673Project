package com.example.healthapp;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;

public class LoginActivityFragmentSignUp extends Fragment {

    public LoginActivityFragmentSignUp() {
        super(R.layout.signup_fragment);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ((LoginActivity)getActivity()).assignButtonToToggle(R.id.textView4);
    }
}
