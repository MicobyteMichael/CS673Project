package com.example.healthapp.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.example.healthapp.R;

public class HomeScreenFragment extends Fragment {

    private ProgressBar steps;
    private TextView stepsLabel;

    public HomeScreenFragment() { super(R.layout.home_screen_fragment); }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        steps = getActivity().findViewById(R.id.progressBarSteps);
        stepsLabel = getActivity().findViewById(R.id.textViewSteps);

        View exercises = getActivity().findViewById(R.id.layoutHomeExercises);
        View food = getActivity().findViewById(R.id.layoutHomeFood);
        View sleep = getActivity().findViewById(R.id.layoutHomeSleep);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if(!prefs.getBoolean("exerciseOnHome", true)) exercises.setVisibility(View.GONE);
        if(!prefs.getBoolean("foodOnHome", true)) food.setVisibility(View.GONE);
        if(!prefs.getBoolean("sleepOnHome", true)) sleep.setVisibility(View.GONE);

        if(!prefs.getBoolean("stepsOnHome", true)) {
            steps.setVisibility(View.GONE);
            stepsLabel.setVisibility(View.GONE);
        }
    }
}
