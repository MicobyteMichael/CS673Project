package com.example.healthapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class SpalshScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh_screen);

        Handler.createAsync(Looper.getMainLooper()).postDelayed(this::showNextScreen, 1500);
    }

    public void showNextScreen() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void onBackPressed() {}
}