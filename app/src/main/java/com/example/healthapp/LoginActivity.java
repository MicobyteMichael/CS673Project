package com.example.healthapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    private boolean isOnLogInPage = false;

    public LoginActivity() {
        super(R.layout.activity_login);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null) {
            toggleSignInSignUp();
        }
    }

    public void toggleSignInSignUp() {
        Class<? extends Fragment> frag = isOnLogInPage ? LoginActivityFragmentSignUp.class : LoginActivityFragmentLogIn.class;
        isOnLogInPage = !isOnLogInPage;

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.logInActFragHolder, frag, null)
                .addToBackStack(null)
                .commit();
    }

    public void assignButtonToToggle(int id) {
        ((Button)findViewById(id)).setOnClickListener(view -> toggleSignInSignUp());
    }


    @Override
    public void onBackPressed() {}
}