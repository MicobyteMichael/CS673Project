package com.example.healthapp.ui;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.example.healthapp.APIDemos;
import com.example.healthapp.R;
import com.example.healthapp.backend.bodycomp.BodyComposition;
import com.example.healthapp.backend.bodycomp.RESTTaskGetBodyComposition;
import com.example.healthapp.backend.bodycomp.RESTTaskSubmitBodyComposition;
import com.example.healthapp.backend.exercise.ExerciseSession;
import com.example.healthapp.backend.exercise.RESTTaskEndExerciseSession;
import com.example.healthapp.backend.exercise.RESTTaskGetExerciseSessions;
import com.example.healthapp.backend.exercise.RESTTaskGetSteps;
import com.example.healthapp.backend.exercise.RESTTaskSetSteps;
import com.example.healthapp.backend.exercise.RESTTaskStartExerciseSession;
import com.example.healthapp.backend.foodanddrink.Meal;
import com.example.healthapp.backend.foodanddrink.RESTTaskGetMeals;
import com.example.healthapp.backend.foodanddrink.RESTTaskGetWaters;
import com.example.healthapp.backend.foodanddrink.RESTTaskSetWaters;
import com.example.healthapp.backend.foodanddrink.RESTTaskSubmitMeal;
import com.example.healthapp.backend.sleeptracking.RESTTaskEndSleepSession;
import com.example.healthapp.backend.sleeptracking.RESTTaskGetSleep;
import com.example.healthapp.backend.sleeptracking.RESTTaskStartSleepSession;
import com.example.healthapp.backend.sleeptracking.SleepSession;

import java.util.function.Consumer;

public class HomeScreenFragment extends Fragment {

    public HomeScreenFragment() {
        super(R.layout.home_screen_fragment);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Consumer<String> msgGenerator = System.err::println;//msg -> new AlertDialog.Builder(getActivity()).setMessage(msg).setNeutralButton("OK", null).show();

        // Demos for interacting with all the various APIs - see APIDemos.java for example code
        //APIDemos.watersAPIDemo(msgGenerator);
        //APIDemos.mealsAPIDemo(msgGenerator);
        //APIDemos.sleepTrackingAPIDemo(msgGenerator);
        //APIDemos.bodyCompositionTrackingAPIDemo(msgGenerator);
        //APIDemos.stepTrackingAPIDemo(msgGenerator);
        //APIDemos.exerciseSessionTrackingDemo(msgGenerator);
    }
}
