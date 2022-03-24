package com.example.healthapp.ui.goals;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import com.example.healthapp.R;
import com.example.healthapp.backend.goals.Goal;
import com.example.healthapp.backend.goals.SleepGoal;

import java.util.function.Consumer;

public class SleepGoalFragment extends GoalTypeFragment {

    private SwitchCompat minOrMax;
    private TextView minOrMaxDisplay;
    private EditText hours;

    public SleepGoalFragment() { super(R.layout.fragment_sleep_goal); }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        minOrMax = getActivity().findViewById(R.id.sleepGoalMostOrLeast);
        minOrMaxDisplay = getActivity().findViewById(R.id.sleepGoalDisplayMostOrLeast);
        hours = getActivity().findViewById(R.id.sleepGoalHours);

        minOrMax.setOnCheckedChangeListener((b, v) -> {
            minOrMaxDisplay.setText((minOrMax.isChecked() ? "Min." : "Max.") + " hours:");
        });
    }

    @Override
    public void onSubmitClicked(Consumer<Goal> onSuccess, Consumer<String> onFailure) {
        boolean minOrMaxChecked = minOrMax.isChecked();
        float h;

        try {
            h = Float.parseFloat(hours.getText().toString());
        } catch(Exception e) {
            onFailure.accept("Please input a valid number");
            return;
        }

        onSuccess.accept(SleepGoal.create(null, h, minOrMaxChecked));
    }
}