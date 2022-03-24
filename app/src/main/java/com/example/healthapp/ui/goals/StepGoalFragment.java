package com.example.healthapp.ui.goals;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import com.example.healthapp.R;
import com.example.healthapp.backend.goals.Goal;
import com.example.healthapp.backend.goals.StepsGoal;

import java.util.function.Consumer;

public class StepGoalFragment extends GoalTypeFragment {

    private SwitchCompat milesOrSteps;
    private TextView milesOrStepsDisplay;
    private EditText distance;

    public StepGoalFragment() { super(R.layout.fragment_step_goal); }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        milesOrSteps = getActivity().findViewById(R.id.stepsOrMiles);
        milesOrStepsDisplay = getActivity().findViewById(R.id.displayStepsOrMiles);
        distance = getActivity().findViewById(R.id.stepGoalDistance);

        milesOrSteps.setOnCheckedChangeListener((b, v) -> {
            milesOrStepsDisplay.setText("Number of " + (milesOrSteps.isChecked() ? "Miles" : "Steps") + ":");
        });
    }

    @Override
    public void onSubmitClicked(Consumer<Goal> onSuccess, Consumer<String> onFailure) {
        boolean miles = milesOrSteps.isChecked();
        float dist;

        try {
            dist = Float.parseFloat(distance.getText().toString());
        } catch(Exception e) {
            onFailure.accept("Please input a valid distance");
            return;
        }

        if(!miles && (Math.round(dist) != dist)) {
            onFailure.accept("No fractional steps!");
        } else {
            onSuccess.accept(StepsGoal.create(null, dist, miles));
        }
    }
}