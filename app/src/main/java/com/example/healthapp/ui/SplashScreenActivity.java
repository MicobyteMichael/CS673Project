package com.example.healthapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.example.healthapp.R;
import com.example.healthapp.ui.auth.LoginActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Handler.createAsync(Looper.getMainLooper()).postDelayed(this::showNextScreen, 1500);
    }

    public void showNextScreen() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void onBackPressed() {}
}