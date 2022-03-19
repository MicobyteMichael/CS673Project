package com.example.healthapp.ui.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.Button;

import com.example.healthapp.R;

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

    public void showFragment(Class<? extends Fragment> frag) {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.logInActFragHolder, frag, null)
                .addToBackStack(null)
                .commit();
    }

    public void toggleSignInSignUp() {
        showFragment(isOnLogInPage ? LoginActivityFragmentSignUp.class : LoginActivityFragmentLogIn.class);
        isOnLogInPage = !isOnLogInPage;
    }

    public void assignButtonToToggle(int id) {
        ((Button)findViewById(id)).setOnClickListener(view -> toggleSignInSignUp());
    }

    @Override
    public void onBackPressed() {}
}