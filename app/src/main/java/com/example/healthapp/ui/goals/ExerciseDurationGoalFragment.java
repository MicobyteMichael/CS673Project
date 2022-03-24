package com.example.healthapp.ui.goals;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.healthapp.R;
import com.example.healthapp.backend.exercise.ExerciseType;
import com.example.healthapp.backend.goals.ExerciseDurationGoal;
import com.example.healthapp.backend.goals.Goal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ExerciseDurationGoalFragment extends GoalTypeFragment {
    public static final String NO_FILTER = "(Any)";

    private Spinner exerciseTypeSpinner;

    public ExerciseDurationGoalFragment() { super(R.layout.fragment_exercise_duration_goal); }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ArrayList<String> exerciseTypes = new ArrayList<>();
        for(ExerciseType et : ExerciseType.values()) exerciseTypes.add(et.name());
        exerciseTypes.add(NO_FILTER);

        ArrayAdapter<String> adap = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, exerciseTypes);
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exerciseTypeSpinner = (Spinner)getActivity().findViewById(R.id.spinnerExerciseType);
        exerciseTypeSpinner.setAdapter(adap);
    }

    @Override
    public void onSubmitClicked(Consumer<Goal> onSuccess, Consumer<String> onFailure) {
        String exerciseType = (String)exerciseTypeSpinner.getSelectedItem();
        ExerciseType type = null;
        if(!NO_FILTER.equals(exerciseType)) type = ExerciseType.valueOf(exerciseType);

        float hours;
        try {
            hours = Float.parseFloat(((EditText)getActivity().findViewById(R.id.exerciseDurationGoalDuration)).getText().toString());
        } catch(Exception e) {
            onFailure.accept("Please input a valid number of hours");
            return;
        }

        onSuccess.accept(ExerciseDurationGoal.create(null, hours, type));
    }
}