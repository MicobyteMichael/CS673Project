package com.example.healthapp.ui.goals;

import android.widget.EditText;

import com.example.healthapp.R;
import com.example.healthapp.backend.goals.Goal;
import com.example.healthapp.backend.goals.WatersConsumedGoal;

import java.util.function.Consumer;

public class WaterGoalFragment extends GoalTypeFragment {

    public WaterGoalFragment() { super(R.layout.fragment_water_goal); }

    @Override
    public void onSubmitClicked(Consumer<Goal> onSuccess, Consumer<String> onFailure) {
        int w;
        try {
            w = Integer.parseInt(((EditText)getActivity().findViewById(R.id.waterGoalNumGlasses)).getText().toString());
        } catch(Exception e) {
            onFailure.accept("Please input a valid number");
            return;
        }

        onSuccess.accept(WatersConsumedGoal.create(null, w));
    }
}