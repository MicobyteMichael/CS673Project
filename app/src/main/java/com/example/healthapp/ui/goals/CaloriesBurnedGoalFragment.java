package com.example.healthapp.ui.goals;

import android.widget.EditText;

import com.example.healthapp.R;
import com.example.healthapp.backend.goals.ExerciseCaloriesBurnedGoal;
import com.example.healthapp.backend.goals.Goal;
import com.example.healthapp.backend.goals.WatersConsumedGoal;

import java.util.function.Consumer;

public class CaloriesBurnedGoalFragment extends GoalTypeFragment {

    public CaloriesBurnedGoalFragment() { super(R.layout.fragment_calories_burned_goal); }

    @Override
    public void onSubmitClicked(Consumer<Goal> onSuccess, Consumer<String> onFailure) {
        int cals;
        try {
            cals = Integer.parseInt(((EditText)getActivity().findViewById(R.id.numCalories)).getText().toString());
        } catch(Exception e) {
            onFailure.accept("Please input a valid number");
            return;
        }

        onSuccess.accept(ExerciseCaloriesBurnedGoal.create(null, cals));
    }
}