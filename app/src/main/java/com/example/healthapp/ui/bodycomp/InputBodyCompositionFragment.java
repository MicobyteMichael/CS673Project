package com.example.healthapp.ui.bodycomp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.healthapp.R;
import com.example.healthapp.backend.bodycomp.BodyComposition;
import com.example.healthapp.backend.bodycomp.RESTTaskSubmitBodyComposition;
import com.example.healthapp.ui.MainActivity;

public class InputBodyCompositionFragment extends Fragment {

    public static int calcFatPercent(int heightInInches, int waistCircumference, int neckCircumference) {
        // Based on the Navy Body Fat Formula: https://www.omnicalculator.com/health/navy-body-fat#what-formula-does-the-us-navy-body-fat-calculator-use
        return (int)Math.round(495 / (1.0324 - 0.19077 * Math.log10((waistCircumference - neckCircumference) * 2.85) + 0.15456 * Math.log10(heightInInches * 2.85)) - 450);
    }

    public static int calcMuscleWeight(int weight, int fatPercentage) {
        // Fat percentage + muscle percentage = 100% --->> Muscle weight = weight * muscle percentage --->> Muscle weight = weight * (100% - fat percentage)
        return Math.round((100 - fatPercentage) * weight / 100F);
    }

    public InputBodyCompositionFragment() {
        super(R.layout.fragment_input_body_composition);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ((Button)getActivity().findViewById(R.id.buttonSubmitBodyComp)).setOnClickListener(v -> onSubmitClicked());
    }

    private void error(String msg) {
        new AlertDialog.Builder(getActivity()).setMessage(msg).setNeutralButton("OK", null).show();
    }

    private Integer getValue(int id, String name) {
        String text = ((EditText)getActivity().findViewById(id)).getText().toString().trim();
        if(text.length() > 0) {
           try {
               return Integer.parseInt(text);
           } catch(Exception e) {}
        }

        error("Please input a valid " + name);
        return null;
    }

    private void onSubmitClicked() {
        Integer weight = getValue(R.id.editTextNumberWeight, "weight");
        if(weight != null) {
            Integer height = getValue(R.id.editTextNumberHeight, "height");
            if(height != null) {
                Integer neck = getValue(R.id.editTextNumberNeck, "neck circumference");
                if(neck != null) {
                    Integer waist = getValue(R.id.editTextNumberWaist, "waist circumference");
                    if(waist != null) {
                        submit(weight, height, neck, waist);
                    }
                }
            }
        }
    }

    private void submit(int weight, int height, int neck, int waist) {
        int fat = calcFatPercent(height, waist, neck);
        int muscle = calcMuscleWeight(weight, fat);
        BodyComposition comp = new BodyComposition(weight, fat, muscle);

        new AlertDialog.Builder(getActivity())
            .setMessage("Found weight is " + comp.getDescription() + ", proceed?")
            .setPositiveButton("Yes", (v1, v2) -> {
                RESTTaskSubmitBodyComposition.enqueue(comp, () -> {
                    error("Submitted!");
                    ((MainActivity)getActivity()).showFrag(BodyCompositionFragment.class);
                }, this::error);
            })
            .setNegativeButton("No", null)
            .show();
    }
}