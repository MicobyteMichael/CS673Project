package com.example.healthapp.ui.goals;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import com.example.healthapp.R;
import com.example.healthapp.backend.goals.CaloriesConsumedGoal;
import com.example.healthapp.backend.goals.Goal;
import com.example.healthapp.backend.goals.StepsGoal;

import java.util.function.Consumer;

public class CaloriesConsumedGoalFragment extends GoalTypeFragment {

    private SwitchCompat minOrMax;
    private TextView minOrMaxDisplay;
    private EditText calories;

    public CaloriesConsumedGoalFragment() { super(R.layout.fragment_calories_consumed_goal); }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        minOrMax = getActivity().findViewById(R.id.mostOrLeast);
        minOrMaxDisplay = getActivity().findViewById(R.id.displayMostOrLeast);
        calories = getActivity().findViewById(R.id.calories);

        minOrMax.setOnCheckedChangeListener((b, v) -> {
            minOrMaxDisplay.setText((minOrMax.isChecked() ? "Min." : "Max.") + " calories:");
        });
    }

    @Override
    public void onSubmitClicked(Consumer<Goal> onSuccess, Consumer<String> onFailure) {
        boolean minOrMaxChecked = minOrMax.isChecked();
        int cals;

        try {
            cals = Integer.parseInt(calories.getText().toString());
        } catch(Exception e) {
            onFailure.accept("Please input a valid number");
            return;
        }

        onSuccess.accept(CaloriesConsumedGoal.create(null, cals, minOrMaxChecked));
    }
}