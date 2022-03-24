package com.example.healthapp.ui.meals;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.fragment.app.Fragment;

import com.example.healthapp.R;
import com.example.healthapp.backend.foodanddrink.Meal;
import com.example.healthapp.backend.foodanddrink.RESTTaskSubmitMeal;
import com.example.healthapp.ui.MainActivity;

import java.util.function.Consumer;

public class SubmitMealFragment extends Fragment {

    public SubmitMealFragment() {
        super(R.layout.fragment_submit_meal);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ((Button)getActivity().findViewById(R.id.buttonSubmitMealSubmit)).setOnClickListener(v -> onSubmitClicked());
        ((Button)getActivity().findViewById(R.id.buttonCancelSubmitMeal)).setOnClickListener(v -> backToMealsList());
    }

    private void error(String msg) {
        new AlertDialog.Builder(getActivity()).setMessage(msg).setNeutralButton("OK", null).show();
    }

    private void backToMealsList() {
        ((MainActivity)getActivity()).showFrag(MealsFragment.class);
    }

    private boolean isSelected(int radioID) {
        return ((RadioButton)getActivity().findViewById(radioID)).isChecked();
    }

    private Integer getValue(int id) {
        String text = ((EditText)getActivity().findViewById(id)).getText().toString().trim();
        if(text.length() > 0) {
            try {
                return Integer.parseInt(text);
            } catch(Exception e) {}
        }

        return null;
    }

    private void onSubmitClicked() {
        Integer calories  = getValue  (R.id.editTextCalories);
        boolean breakfast = isSelected(R.id.radio_Breakfast );
        boolean lunch     = isSelected(R.id.radio_Lunch     );
        boolean dinner    = isSelected(R.id.radio_Dinner    );
        boolean other     = isSelected(R.id.radio_other_meal);

        if(calories == null) {
            error("Please input a number of calories");
        } else {
            Consumer<String> mealNameAcceptor = meal -> {
                RESTTaskSubmitMeal.enqueue(new Meal(meal, calories), () -> {
                    error("Submitted!");
                    backToMealsList();
                }, msg -> {
                    error("The meal \"" + meal + "\" has already been input for today, please choose another.");
                });
            };

            if(breakfast)   mealNameAcceptor.accept("Breakfast");
            else if(lunch)  mealNameAcceptor.accept("Lunch"    );
            else if(dinner) mealNameAcceptor.accept("Dinner"   );
            else if(other) {
                EditText mealNameInputter = new EditText(getActivity());
                mealNameInputter.setInputType(InputType.TYPE_CLASS_TEXT);

                new AlertDialog.Builder(getActivity())
                        .setTitle("Please input a meal name")
                        .setView(mealNameInputter)
                        .setPositiveButton("OK", (d, w) -> mealNameAcceptor.accept(mealNameInputter.getText().toString()))
                        .setNegativeButton("Cancel", (d, w) -> d.cancel())
                        .show();
            } else {
                error("Please select a meal type.");
            }
        }
    }
}
