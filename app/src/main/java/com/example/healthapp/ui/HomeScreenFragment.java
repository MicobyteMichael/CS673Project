package com.example.healthapp.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.example.healthapp.APIDemos;
import com.example.healthapp.R;

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
