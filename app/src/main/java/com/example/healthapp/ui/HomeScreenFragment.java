package com.example.healthapp.ui;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.example.healthapp.R;
import com.example.healthapp.backend.goals.Goal;
import com.example.healthapp.backend.goals.RESTTaskGetGoals;
import com.example.healthapp.backend.goals.StepsGoal;
import com.example.healthapp.ui.meals.SubmitMealFragment;
import com.example.healthapp.ui.sleep.SleepingFragment;

import java.util.function.Consumer;

public class HomeScreenFragment extends Fragment implements StepListener {
    public static final int DEFAULT_MAX_STEPS = 6000;

    private ProgressBar steps;
    private TextView stepsLabel, maxStepsLabel;

    private int maxSteps = 0;

    public HomeScreenFragment() { super(R.layout.home_screen_fragment); }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Consumer<String> msgGenerator = msg -> new AlertDialog.Builder(getActivity()).setMessage(msg).setNeutralButton("OK", null).show();

        steps = getActivity().findViewById(R.id.progressBarSteps);
        stepsLabel = getActivity().findViewById(R.id.textViewSteps);
        maxStepsLabel = getActivity().findViewById(R.id.textViewMaxSteps);

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

        RESTTaskGetGoals.enqueue(goals -> {
            for(Goal g : goals) {
                int maxSteps = -1;
                if(g instanceof StepsGoal) {
                    int steps = ((StepsGoal)g).getSteps();
                    if(steps > maxSteps) maxSteps = steps;
                }

                if(maxSteps == -1) maxSteps = DEFAULT_MAX_STEPS;
                this.maxSteps = maxSteps;

                onStepCountChanged(((MainActivity)getActivity()).getSteps());
            }
        }, err -> msgGenerator.accept("Failed to synchronize!"));

        getActivity().findViewById(R.id.buttonMainScreenAddMeal)    .setOnClickListener(v -> ((MainActivity)getActivity()).showFrag(SubmitMealFragment.class));
        getActivity().findViewById(R.id.buttonMainScreenRecordSleep).setOnClickListener(v -> SleepingFragment.startSleepSession(getActivity(), msgGenerator));
        getActivity().findViewById(R.id.mainScreenStartRunning)     .setOnClickListener(v -> { System.out.println("jog!!"); });
        getActivity().findViewById(R.id.mainScreenStartSwimming)    .setOnClickListener(v -> { System.out.println("swim!!"); });
        getActivity().findViewById(R.id.mainScreenStartCycling)     .setOnClickListener(v -> { System.out.println("bike!!"); });
    }

    @Override
    public void onStepCountChanged(int numSteps) {
        steps.setProgress(Math.round(numSteps / (float)maxSteps * 100F));
        stepsLabel.setText(String.valueOf(numSteps));
        maxStepsLabel.setText(String.valueOf(maxSteps));
    }
}
