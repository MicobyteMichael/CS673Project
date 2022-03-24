package com.example.healthapp.ui.goals;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.healthapp.R;
import com.example.healthapp.backend.exercise.ExerciseType;
import com.example.healthapp.backend.goals.ExerciseDistanceGoal;
import com.example.healthapp.backend.goals.ExerciseDurationGoal;
import com.example.healthapp.backend.goals.Goal;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ExerciseQuantityGoalFragment extends GoalTypeFragment {
    private Spinner exerciseTypeSpinner;

    public ExerciseQuantityGoalFragment() { super(R.layout.fragment_exercise_quantity_goal); }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ArrayList<String> exerciseTypes = new ArrayList<>();
        for(ExerciseType et : ExerciseType.values()) exerciseTypes.add(et.name());

        ArrayAdapter<String> adap = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, exerciseTypes);
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exerciseTypeSpinner = (Spinner)getActivity().findViewById(R.id.spinnerExerciseType2);
        exerciseTypeSpinner.setAdapter(adap);

        exerciseTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { updateExerciseType(); }
            @Override public void onNothingSelected(AdapterView<?> adapterView) { updateExerciseType(); }
        });
    }

    private ExerciseType getType() {
        return ExerciseType.valueOf((String)exerciseTypeSpinner.getSelectedItem());
    }

    private void updateExerciseType() {
        ExerciseType t = getType();

        String desc = t.getMeasuredValueName();
        String unit = t.getUnitName();
        if(unit.length() > 0) desc += (" (" + unit + ")");

        ((TextView)getActivity().findViewById(R.id.textViewMinQuant)).setText("Target " + desc + ":");
    }

    @Override
    public void onSubmitClicked(Consumer<Goal> onSuccess, Consumer<String> onFailure) {
        float quant;
        try {
            quant = Float.parseFloat(((EditText)getActivity().findViewById(R.id.exerciseGoalQuantity)).getText().toString());
        } catch(Exception e) {
            onFailure.accept("Please input a valid number");
            return;
        }

        if(getType().isIntegerQuantity() && (Math.round(quant) != quant)) {
            onFailure.accept("Please input a whole number");
        } else {
            onSuccess.accept(ExerciseDistanceGoal.create(null, quant, getType()));
        }
    }
}